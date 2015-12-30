package com.pepoc.joke.presenter;

import android.content.Context;

import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestGetPublishedJokes;
import com.pepoc.joke.view.iview.IPersonalCenterView;

import java.util.List;

/**
 * Created by Yangchen on 2015/12/29.
 */
public class PersonalCenterPresenter {

    private IPersonalCenterView iPersonalCenterView;

    /** 是否还有更多数据 */
    private boolean isHasMoreData = true;

    /** 是否正在请求数据 */
    private boolean isRequesting = false;

    private int page = 1;

    public PersonalCenterPresenter(IPersonalCenterView iPersonalCenterView) {
        this.iPersonalCenterView = iPersonalCenterView;
    }

    /**
     * 获取数据
     * @param isRefresh true:是刷新     false:加载更多
     */
    public void getData(Context context, final boolean isRefresh, String userId) {
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
                iPersonalCenterView.updateData(datas, isRefresh);
            }

            @Override
            public void onError() {
                iPersonalCenterView.onFail();
            }
        });

        request.putParam("page", String.valueOf(page));
        request.putParam("userId", userId);

        HttpRequestManager.getInstance().sendRequest(request);
    }
}
