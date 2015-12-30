package com.pepoc.joke.view.iview;

/**
 * Created by yangchen on 15-12-28.
 */
public interface IPublishJokeView {

    void onSuccess();

    void onFail();

    void onUploadImageSuccess();

    void showLoading();

    void hideLoading();
}
