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
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.pepoc.joke.R;
import com.pepoc.joke.view.adapter.MainViewPagerAdapter;
import com.pepoc.joke.view.fragment.JokeListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.main_home_page);
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
        adapter.addFragment(new JokeListFragment(), "Category 1");
        adapter.addFragment(new JokeListFragment(), "Category 2");
        adapter.addFragment(new JokeListFragment(), "Category 3");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        Logger.e("navigationView.getHeaderCount() === " + navigationView.getHeaderCount());
        View headerView = navigationView.getHeaderView(0);
        TextView tvNickName = (TextView) headerView.findViewById(R.id.tv_nick_name);
        tvNickName.setText("Yangchen");
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_publish) {
            startActivity(new Intent(context, PublishJokeActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
