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
import android.widget.Toast;

import com.pepoc.joke.R;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestGetJokes;
import com.pepoc.joke.view.adapter.MainViewPagerAdapter;
import com.pepoc.joke.view.fragment.JokeListFragment;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void init() {
        super.init();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_home_page);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new JokeListFragment(), "Category 1");
        adapter.addFragment(new JokeListFragment(), "Category 2");
        adapter.addFragment(new JokeListFragment(), "Category 3");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            getData();
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
//        if (isRefresh) {
//            page = 1;
//            isHasMoreData = true;
//        } else {
//            if (!isHasMoreData) {
////				Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
//                return ;
//            }
//            page++;
//        }

//        isRequesting = true;

        RequestGetJokes request = new RequestGetJokes(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
//                List<JokeContent> datas = (List<JokeContent>) result;
//                if (datas.size() < 20) {
//                    isHasMoreData = false;
//                }
//                if (isRefresh) {
//                    adapter.getDatas().clear();
//                }
//                adapter.setDatas(datas);
//                adapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//                footerView.setVisibility(View.VISIBLE);
//
//                isRequesting = false;
            }

            @Override
            public void onError() {
//                page--;
//                isRequesting = false;
//                log.e("----------onError()---------");
            }
        });

        request.putParam("page", String.valueOf(1));

        if (UserManager.getCurrentUser() == null) {
            request.putParam("userId", "-1");
        } else {
            request.putParam("userId", UserManager.getCurrentUser().getUserId());
        }

        HttpRequestManager.getInstance().sendRequest(request);
    }
}
