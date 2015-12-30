package com.pepoc.joke.presenter;

import android.content.Context;

import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestGetJokes;
import com.pepoc.joke.view.adapter.JokeListAdapter;
import com.pepoc.joke.view.iview.IJokeListView;

import java.util.List;

/**
 * Created by yangchen on 15-12-29.
 */
public class JokeListPresenter {

    private IJokeListView iJokeListView;

    private JokeListAdapter jokeListAdapter;

    private int page = 1;

    /** 是否还有更多数据 */
    private boolean isHasMoreData = true;

    /** 是否正在请求数据 */
    private boolean isRequesting = false;

    public JokeListPresenter(IJokeListView iJokeListView, JokeListAdapter jokeListAdapter) {
        this.iJokeListView = iJokeListView;
        this.jokeListAdapter = jokeListAdapter;
    }

    public void getJokeData(Context context, final boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        }
        isRequesting = true;
        RequestGetJokes requestGetJokes = new RequestGetJokes(context, new HttpRequestManager.OnHttpResponseListener() {
            @Override
            public void onHttpResponse(Object result) {
                iJokeListView.updateData();
                isRequesting = false;

                List<JokeContent> datas = (List<JokeContent>) result;
                if (datas.size() < 20) {
                    isHasMoreData = false;
                } else {
                    isHasMoreData = true;
                }
                if (isRefresh) {
                    jokeListAdapter.getDatas().clear();
                }
                jokeListAdapter.setDatas(datas);
                jokeListAdapter.notifyDataSetChanged();
                page++;
            }

            @Override
            public void onError() {
                iJokeListView.onError();
                isRequesting = false;
            }
        });

        requestGetJokes.putParam("page", String.valueOf(page));
        if (UserManager.getCurrentUser() == null) {
            requestGetJokes.putParam("userId", "-1");
        } else {
            requestGetJokes.putParam("userId", UserManager.getCurrentUser().getUserId());
        }
        HttpRequestManager.getInstance().sendRequest(requestGetJokes);
    }

    public boolean isHasMoreData() {
        return isHasMoreData;
    }

    public boolean isRequesting() {
        return isRequesting;
    }
}
