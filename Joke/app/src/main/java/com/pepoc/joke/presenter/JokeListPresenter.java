package com.pepoc.joke.presenter;

import com.pepoc.joke.view.iview.IJokeListView;

/**
 * Created by yangchen on 15-12-29.
 */
public class JokeListPresenter {

    private IJokeListView iJokeListView;

    public JokeListPresenter(IJokeListView iJokeListView) {
        this.iJokeListView = iJokeListView;
    }
}
