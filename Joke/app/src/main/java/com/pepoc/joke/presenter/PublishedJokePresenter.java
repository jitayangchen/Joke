package com.pepoc.joke.presenter;

import android.content.Context;

import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestGetPublishedJokes;
import com.pepoc.joke.view.iview.IPublishedJokeView;

import java.util.List;

/**
 * Created by Yangchen on 2015/12/29.
 */
public class PublishedJokePresenter {

    private IPublishedJokeView iPublishedJokeView;

    /** 是否还有更多数据 */
    private boolean isHasMoreData = true;

    /** 是否正在请求数据 */
    private boolean isRequesting = false;

    private int page = 1;

    public PublishedJokePresenter(IPublishedJokeView iPublishedJokeView) {
        this.iPublishedJokeView = iPublishedJokeView;
    }

    /**
     * 获取数据
     * @param isRefresh true:是刷新     false:加载更多
     */
    public void getData(Context context, final boolean isRefresh) {
        if (isRefresh) {
            page = 1;
            isHasMoreData = true;
        } else {
            if (!isHasMoreData) {
//				Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return ;
            }
            page++;
        }

        RequestGetPublishedJokes request = new RequestGetPublishedJokes(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                List<JokeContent> datas = (List<JokeContent>) result;
                if (datas.size() < 20) {
                    isHasMoreData = false;
                }
                iPublishedJokeView.updateData(datas, isRefresh);
                iPublishedJokeView.onSuccess();
            }

            @Override
            public void onError() {
                iPublishedJokeView.onFail();
            }
        });

        request.putParam("page", String.valueOf(page));
        request.putParam("userId", UserManager.getCurrentUser().getUserId());

        HttpRequestManager.getInstance().sendRequest(request);
    }

}
