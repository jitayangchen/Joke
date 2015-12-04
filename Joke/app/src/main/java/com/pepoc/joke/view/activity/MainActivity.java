package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import com.pepoc.joke.observer.LoginObservable;
import com.pepoc.joke.view.adapter.MainViewPagerAdapter;
import com.pepoc.joke.view.fragment.JokeListFragment;

import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements Observer {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LoginObservable.getInstance().addObserver(this);
        init();
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

                }
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
                            startActivity(new Intent(context, PublishJokeActivity.class));
                        } else if (id == R.id.menu_published) {
                            startActivity(new Intent(context, PublishedJokeActivity.class));
                        } else if (id == R.id.menu_collected) {
                            startActivity(new Intent(context, CollectedJokeActivity.class));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(context, SettingActivity.class));
            return true;
        } else if (id == R.id.action_publish) {
            startActivity(new Intent(context, PublishJokeActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(context, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {
        UserInfo currentUser = UserManager.getCurrentUser();
        tvNickName.setText(currentUser.getNickName());
        ImageLoadding.load(context, currentUser.getAvatar(), ivUserAvatar);
    }
}
