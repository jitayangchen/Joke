package com.pepoc.joke.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.pepoc.joke.data.user.UserInfo;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestLogin;
import com.pepoc.joke.net.http.request.RequestUpToken;
import com.pepoc.joke.net.http.request.RequestUpdateUserInfo;
import com.pepoc.joke.util.Preference;
import com.pepoc.joke.view.iview.IMainView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangchen on 15-12-28.
 */
public class MainPresenter {

    private IMainView iMainView;
    private String key;
    private String uploadToken;

    public MainPresenter(IMainView iMainView) {
        this.iMainView = iMainView;
    }

    public void autoLogin(final Context context) {
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
                    UserInfo currentUser = UserManager.getCurrentUser();
                    iMainView.setAvatar(currentUser.getAvatar());
                    iMainView.setUserName(currentUser.getNickName());
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

    /**
     * 上传头像
     */
    public void upLoadAvatar(final Context context, final String path) {
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
                            uploadAvatarKey(context);
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
    private void uploadAvatarKey(Context context) {
        RequestUpdateUserInfo requestUpdateUserInfo = new RequestUpdateUserInfo(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                iMainView.setAvatar(key);
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
