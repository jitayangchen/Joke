package com.pepoc.joke.presenter;

import android.content.Context;

import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestLogin;
import com.pepoc.joke.view.iview.ILoginView;

/**
 * Created by Yangchen on 2015/12/29.
 */
public class LoginPresenter {

    private ILoginView iLoginView;

    public LoginPresenter(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
    }

    public void login(String accountNumber, String password) {

        RequestLogin requestLogin = new RequestLogin((Context)iLoginView, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                boolean isLoginSuccess = (Boolean) result;
                if (isLoginSuccess) {
                    iLoginView.onLoginSuccess();
                } else {
                    iLoginView.onLoginFail();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub
                iLoginView.onError();
            }
        });

        requestLogin.putParam("accountNumber", accountNumber);
        requestLogin.putParam("password", password);

        HttpRequestManager.getInstance().sendRequest(requestLogin);
    }
}
