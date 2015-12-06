package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.pepoc.joke.R;
import com.pepoc.joke.data.user.UserInfo;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.ImageLoadding;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestLogin;
import com.pepoc.joke.net.http.request.RequestUpToken;
import com.pepoc.joke.net.http.request.RequestUpdateUserInfo;
import com.pepoc.joke.observer.LoginObservable;
import com.pepoc.joke.util.Preference;
import com.pepoc.joke.view.adapter.MainViewPagerAdapter;
import com.pepoc.joke.view.fragment.JokeListFragment;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

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
    private final static int REQUEST_IMAGE = 1000;

    private String key;
    private String uploadToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LoginObservable.getInstance().addObserver(this);
        init();

        autoLogin();
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
                    startActivity(new Intent(context, PersonalCenterActivity.class));
                }
            }
        });
        ivUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MultiImageSelectorActivity.class);

// whether show camera
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

// max select image amount
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);

// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

                startActivityForResult(intent, REQUEST_IMAGE);
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
                upLoadAvatar(path.get(0));
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

    @Override
    public void update(Observable observable, Object data) {
        UserInfo currentUser = UserManager.getCurrentUser();
        tvNickName.setText(currentUser.getNickName());
        Logger.d("---------LoginObservable.getInstance().countObservers()------- " + LoginObservable.getInstance().countObservers());
        ImageLoadding.load(context, currentUser.getAvatar(), ivUserAvatar);
    }

    private void autoLogin() {
        final String accountNumber = Preference.getAccountNumber();
        final String password = Preference.getPassword();
        if (!Preference.isAutoLogin() || TextUtils.isEmpty(accountNumber) || TextUtils.isEmpty(password)) {
            Preference.saveAutoLogin(false);
            return ;
        }

        RequestLogin requestLogin = new RequestLogin(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                boolean isLoginSuccess = (Boolean) result;
                if (isLoginSuccess) {
                    Toast.makeText(context, "login success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        requestLogin.putParam("accountNumber", accountNumber);
        requestLogin.putParam("password", password);

        HttpRequestManager.getInstance().sendRequest(requestLogin);
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
    protected void onDestroy() {
        super.onDestroy();
        LoginObservable.getInstance().deleteObserver(this);
    }

    /**
     * 上传头像
     */
    private void upLoadAvatar(final String path) {
        RequestUpToken requestUpToken = new RequestUpToken(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                try {
                    JSONObject obj = new JSONObject((String)result);
                    String status = obj.getString("status");
                    if ("1".equals(status)) {
                        uploadToken = obj.getString("upToken");
                    }
                } catch (JSONException e) {
                    Logger.e("get uptoken");
                }

                // 七牛上传
                UploadManager uploadManager = new UploadManager();
                uploadManager.put(path, key, uploadToken, new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            Log.i("qiniu", "=== upload success ===");
                            Toast.makeText(context, "upload success", Toast.LENGTH_SHORT).show();
                            uploadAvatarKey();
                        } else {
                            Log.i("qiniu", "fail");
                            Toast.makeText(context, "upload fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, null);
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        key = "pj_avatar_" + System.currentTimeMillis();
        requestUpToken.putParam("key", key);

        HttpRequestManager.getInstance().sendRequest(requestUpToken);
    }

    /**
     * 上传头像
     */
    private void uploadAvatarKey() {
        RequestUpdateUserInfo requestUpdateUserInfo = new RequestUpdateUserInfo(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                ImageLoadding.load(context, key, ivUserAvatar);
                UserManager.getCurrentUser().setAvatar(key);
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        requestUpdateUserInfo.putParam("userId", UserManager.getCurrentUser().getUserId());
        requestUpdateUserInfo.putParam("avatar", key);

        HttpRequestManager.getInstance().sendRequest(requestUpdateUserInfo);
    }
}
