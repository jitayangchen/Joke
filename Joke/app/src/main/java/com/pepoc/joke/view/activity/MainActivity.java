package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepoc.joke.R;
import com.pepoc.joke.data.user.UserInfo;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.ImageLoadding;
import com.pepoc.joke.presenter.MainPresenter;
import com.pepoc.joke.util.Preference;
import com.pepoc.joke.view.adapter.MainViewPagerAdapter;
import com.pepoc.joke.view.fragment.JokeListFragment;
import com.pepoc.joke.view.iview.IMainView;

import java.util.List;
import java.util.Observable;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends BaseActivity implements IMainView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @Bind(R.id.main_view_pager)
    ViewPager mainViewPager;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private TextView tvNickName;
    private ImageView ivUserAvatar;
    private final static int REQUEST_IMAGE = 1000;

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainPresenter = new MainPresenter(this);
        init();

        mainPresenter.autoLogin(this);
    }

    @Override
    public void init() {
        super.init();

//        toolbar.setTitle(R.string.main_home_page);
        setSupportActionBar(toolbar);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mActionBarDrawerToggle);

        if (mainViewPager != null) {
            setupViewPager(mainViewPager);
        }

        mainTabLayout.setupWithViewPager(mainViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(2);
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new JokeListFragment(), "逗");
        adapter.addFragment(new JokeListFragment(), "你");
        adapter.addFragment(new JokeListFragment(), "玩");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        tvNickName = (TextView) headerView.findViewById(R.id.tv_nick_name);
        ivUserAvatar = (ImageView) headerView.findViewById(R.id.iv_user_avatar);
        tvNickName.setText(R.string.login_or_register);
        tvNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.getCurrentUser() == null) {
                    startActivity(new Intent(context, LoginActivity.class));
                } else {
                    Intent intent = new Intent(context, PersonalCenterActivity.class);
                    intent.putExtra("UserId", UserManager.getCurrentUser().getUserId());
                    intent.putExtra("NickName", UserManager.getCurrentUser().getNickName());
                    intent.putExtra("avatar", UserManager.getCurrentUser().getAvatar());
                    startActivity(intent);
                }
            }
        });
        ivUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        menuItem.setChecked(true);
//                        drawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        if (id == R.id.menu_write_joke) {
                            if (isLogin()) {
                                startActivity(new Intent(context, PublishJokeActivity.class));
                            }
                        } else if (id == R.id.menu_published) {
                            if (isLogin()) {
                                startActivity(new Intent(context, PublishedJokeActivity.class));
                            }
                        } else if (id == R.id.menu_collected) {
                            if (isLogin()) {
                                startActivity(new Intent(context, CollectedJokeActivity.class));
                            }
                        } else if (id == R.id.action_settings) {
                            startActivity(new Intent(context, SettingActivity.class));
                        } else if (id == R.id.action_about) {
                            startActivity(new Intent(context, AboutActivity.class));
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mainPresenter.upLoadAvatar(context, path.get(0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(context, SettingActivity.class));
        } else if (id == R.id.action_publish) {
            if (isLogin()) {
                startActivity(new Intent(context, PublishJokeActivity.class));
            }
        } else if (id == R.id.action_about) {
            startActivity(new Intent(context, AboutActivity.class));
        }

        return true;
    }

    private boolean isLogin() {
        if (!Preference.isAutoLogin() || UserManager.getCurrentUser() == null) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            startActivity(loginIntent);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);

        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void setAvatar(String url) {
        ImageLoadding.loadAvatar(context, url, ivUserAvatar);
    }

    @Override
    public void setUserName(String userName) {
        tvNickName.setText(userName);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

}
