package com.pepoc.joke.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestAddJoke;
import com.pepoc.joke.net.http.request.RequestUpToken;
import com.pepoc.joke.view.iview.IPublishJokeView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yangchen on 2015/12/29.
 */
public class PublishJokePresenter {

    private IPublishJokeView iPublishJokeView;
    private String key;
    private String uploadToken;

    public PublishJokePresenter(IPublishJokeView iPublishJokeView) {
        this.iPublishJokeView = iPublishJokeView;
    }

    public void addJokeContent(Context context, String content, String imageUrl) {

        if (TextUtils.isEmpty(content) && TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(context, "content null", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = UserManager.getCurrentUser().getUserId();
        RequestAddJoke requestAddJoke = new RequestAddJoke(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                String status = (String) result;
                if ("1".equals(status)) {
                    iPublishJokeView.onSuccess();
                } else {
                    iPublishJokeView.onFail();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub
                iPublishJokeView.onFail();
            }
        });

        requestAddJoke.putParam("content", content);
        requestAddJoke.putParam("user_id", uid);
        if (!TextUtils.isEmpty(imageUrl)) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);

            key = key + "&" + bitmap.getWidth() + "&" + bitmap.getHeight();
            requestAddJoke.putParam("images_url", key);
        }

        HttpRequestManager.getInstance().sendRequest(requestAddJoke);
    }

    /**
     * 上传头像
     */
    public void upLoadImage(final Context context, final String path) {
        RequestUpToken requestUpToken = new RequestUpToken(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                try {
                    JSONObject obj = new JSONObject((String)result);
                    String status = obj.getString("status");
                    if ("1".equals(status)) {
                        uploadToken = obj.getString("upToken");
                    }
                } catch (JSONException e) {
                    Logger.e("get uptoken");
                }

                // 七牛上传
                UploadManager uploadManager = new UploadManager();
                uploadManager.put(path, key, uploadToken, new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            iPublishJokeView.onUploadImageSuccess();
                        } else {
                            iPublishJokeView.onFail();
                        }
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {

                    @Override
                    public void progress(String key, double percent) {
                        Logger.i(percent + "");
                    }
                }, null));
            }

            @Override
            public void onError() {
                iPublishJokeView.onFail();
            }
        });

        key = "pj_joke_image_" + System.currentTimeMillis();
        requestUpToken.putParam("key", key);

        HttpRequestManager.getInstance().sendRequest(requestUpToken);
    }
}
