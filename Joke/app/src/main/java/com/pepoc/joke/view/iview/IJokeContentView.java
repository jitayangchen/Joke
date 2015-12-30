package com.pepoc.joke.view.iview;

import java.util.List;

/**
 * Created by yangchen on 15-12-28.
 */
public interface IJokeContentView<T> {

    void showLoading();

    void hideLoading();

    void updateCommentData(List<T> datas);

    void commentSuccess();

    void onError();
}
