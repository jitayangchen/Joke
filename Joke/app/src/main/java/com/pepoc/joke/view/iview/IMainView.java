package com.pepoc.joke.view.iview;

/**
 * Created by yangchen on 15-12-28.
 */
public interface IMainView {

    void setAvatar(String url);

    void setUserName(String userName);

    void showLoading();

    void hideLoading();
}
