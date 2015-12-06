package com.pepoc.joke.view.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.pepoc.joke.R;

import java.lang.reflect.Field;

/**
 * Created by yangchen on 15-12-5.
 */
@CoordinatorLayout.DefaultBehavior(FabImageView.Behavior.class)
public class FabImageView extends ImageView {

    private Context context;
    private boolean isShow = true;
    private WindowInsetsCompat mLastInsets = null;
    private int minimumHeight = -1;

    public FabImageView(Context context) {
        super(context);
        this.context = context;
    }

    public FabImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public FabImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public static class Behavior extends CoordinatorLayout.Behavior<FabImageView> {
        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, FabImageView child, View dependency) {
            return super.layoutDependsOn(parent, child, dependency);
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, FabImageView child, View dependency) {
            if (dependency instanceof Snackbar.SnackbarLayout) {
//                updateFabTranslationForSnackbar(parent, child, dependency);
            } else if (dependency instanceof AppBarLayout) {
                // If we're depending on an AppBarLayout we will show/hide it automatically
                // if the FAB is anchored to the AppBarLayout
                updateFabVisibility(parent, (AppBarLayout) dependency, child);
            }
            return false;
        }

        private boolean updateFabVisibility(CoordinatorLayout parent,
                                            AppBarLayout appBarLayout, FabImageView child) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if (lp.getAnchorId() != appBarLayout.getId()) {
                // The anchor ID doesn't match the dependency, so we won't automatically
                // show/hide the FAB
                return false;
            }
            Rect mTmpRect = null;
            if (mTmpRect == null) {
                mTmpRect = new Rect();
            }

            // First, let's get the visible rect of the dependency
            final Rect rect = mTmpRect;
            ViewGroupUtils.getDescendantRect(parent, appBarLayout, rect);
//            Logger.i("child.getMinimumHeightForVisibleOverlappingContent(appBarLayout) = " + child.getMinimumHeightForVisibleOverlappingContent(appBarLayout));
            if (rect.bottom <= child.getMinimumHeightForVisibleOverlappingContent(appBarLayout)) {
                // If the anchor's bottom is below the seam, we'll animate our FAB out
                child.hide();
            } else {
                // Else, we'll animate our FAB back in
                child.show();
            }
            return true;
        }
    }

    private void hide() {
        if (!isShow) {
            return ;
        }
        isShow = false;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fab_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animation);
    }

    private void show() {
        if (isShow) {
            return ;
        }
        isShow = true;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fab_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animation);
    }

    private void getLastInsets(AppBarLayout view) {
        if (mLastInsets != null) {
            return ;
        }
        try {
            Field fieldmLastInsets = AppBarLayout.class.getDeclaredField("mLastInsets");
            fieldmLastInsets.setAccessible(true);
            mLastInsets = (WindowInsetsCompat) fieldmLastInsets.get(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final int getMinimumHeightForVisibleOverlappingContent(AppBarLayout appBarLayout) {
        if (minimumHeight != -1) {
            return minimumHeight;
        }
        getLastInsets(appBarLayout);
        final int topInset = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
        final int minHeight = ViewCompat.getMinimumHeight(appBarLayout);
        if (minHeight != 0) {
            // If this layout has a min height, use it (doubled)
            return (minHeight * 2) + topInset;
        }

        // Otherwise, we'll use twice the min height of our last child
        final int childCount = appBarLayout.getChildCount();
        minimumHeight = childCount >= 1 ? (ViewCompat.getMinimumHeight(appBarLayout.getChildAt(childCount - 1)) * 2) + topInset : 0;
        return minimumHeight;
    }

}
