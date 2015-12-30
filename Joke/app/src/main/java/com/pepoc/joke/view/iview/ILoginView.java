package com.pepoc.joke.view.iview;

/**
 * Created by yangchen on 15-12-28.
 */
public interface ILoginView {

    void onLoginSuccess();

    void onLoginFail();

    void onError();

    void showLoading();

    void hideLoading();
}
