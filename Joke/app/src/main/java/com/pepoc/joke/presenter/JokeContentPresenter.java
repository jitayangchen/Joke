package com.pepoc.joke.presenter;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.pepoc.joke.data.bean.JokeComment;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestCollectJoke;
import com.pepoc.joke.net.http.request.RequestComment;
import com.pepoc.joke.net.http.request.RequestDeleteJoke;
import com.pepoc.joke.net.http.request.RequestGetComment;
import com.pepoc.joke.net.http.request.RequestLikeJoke;
import com.pepoc.joke.view.iview.IJokeContentView;

import java.util.List;

/**
 * Created by yangchen on 15-12-29.
 */
public class JokeContentPresenter {

    private IJokeContentView iJokeContentView;

    public JokeContentPresenter(IJokeContentView iJokeContentView) {
        this.iJokeContentView = iJokeContentView;
    }

    /**
     * 获取评论
     */
    public void getComment(Context context, String jokeId) {
        RequestGetComment request = new RequestGetComment(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                List<JokeComment> jokeComments = (List<JokeComment>) result;
                iJokeContentView.updateCommentData(jokeComments);
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub
                iJokeContentView.onError();
            }
        });

        request.putParam("jokeId", jokeId);

        HttpRequestManager.getInstance().sendRequest(request);
    }

    /**
     * 发表评论
     */
    public void comment(Context context, String jokeId, String commentContent) {
        RequestComment request = new RequestComment(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                boolean isSuccess = (Boolean) result;
                if (isSuccess) {
                    iJokeContentView.commentSuccess();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub
                iJokeContentView.onError();
            }
        });

        request.putParam("content", commentContent);
        request.putParam("jokeId", jokeId);
        request.putParam("userId", UserManager.getCurrentUser().getUserId());

        HttpRequestManager.getInstance().sendRequest(request);
    }

    /**
     * 收藏
     * @param context
     */
    public void collectJoke(final Context context, String jokeId) {
        RequestCollectJoke request = new RequestCollectJoke(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                String status = (String) result;
                if ("1".equals(status)) {
                    Toast.makeText(context, "Collect Success", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        request.putParam("jokeId", jokeId);
        request.putParam("userId", UserManager.getCurrentUser().getUserId());
        HttpRequestManager.getInstance().sendRequest(request);
    }

    /**
     * 喜欢
     * @param context
     */
    public void likeJoke(final Context context, String jokeId) {
        RequestLikeJoke request = new RequestLikeJoke(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                String status = (String) result;
                if ("1".equals(status)) {
                    Toast.makeText(context, "Like Success", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        request.putParam("jokeId", jokeId);
        request.putParam("userId", UserManager.getCurrentUser().getUserId());
        HttpRequestManager.getInstance().sendRequest(request);
    }

    /**
     * 删除
     * @param context
     */
    public void deleteJoke(final Context context, String jokeId) {
        RequestDeleteJoke requestDeleteJoke = new RequestDeleteJoke(context, new HttpRequestManager.OnHttpResponseListener() {
            @Override
            public void onHttpResponse(Object result) {
                String status = (String) result;
                if ("1".equals(status)) {
                    Toast.makeText(context, "Delete Success", Toast.LENGTH_LONG).show();
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onError() {

            }
        });

        requestDeleteJoke.putParam("jokeId", jokeId);
        HttpRequestManager.getInstance().sendRequest(requestDeleteJoke);
    }
}
