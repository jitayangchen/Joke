package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.pepoc.joke.R;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestAddJoke;
import com.pepoc.joke.net.http.request.RequestUpToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class PublishJokeActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    @Bind(R.id.et_joke_content)
    EditText etJokeContent;
    @Bind(R.id.btn_submit)
    ImageButton btnSubmit;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_add_photo)
    ImageButton btnAddPhoto;
    @Bind(R.id.iv_joke_photo)
    ImageView ivJokePhoto;

    private final static int REQUEST_IMAGE = 1000;
    private String key;
    private String uploadToken;
    private String imageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_joke);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.menu_write_joke);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSubmit.setOnClickListener(this);
        btnAddPhoto.setOnClickListener(this);
        ivJokePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                addJoke();
                break;
            case R.id.btn_add_photo:
                openGallery();
                break;
            case R.id.iv_joke_photo:
                openGallery();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                Glide.with(context).load(path.get(0)).centerCrop().into(ivJokePhoto);
                imageUrl = path.get(0);
            }
        }
    }

    private void addJoke() {
        if (null == UserManager.getCurrentUser()) {
            Toast.makeText(context, "not login", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(imageUrl)) {
            addJokeContent();
        } else {
            upLoadImage(imageUrl);
        }
    }

    private void addJokeContent() {

        String content = etJokeContent.getText().toString();
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
                    Toast.makeText(context, "send success", Toast.LENGTH_SHORT).show();
                    etJokeContent.setText("");
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

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
    private void upLoadImage(final String path) {
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
                            Log.i("qiniu", "=== upload success ===");
                            Toast.makeText(context, "upload success", Toast.LENGTH_SHORT).show();
                            addJokeContent();
                        } else {
                            Log.i("qiniu", "fail");
                            Toast.makeText(context, "upload fail", Toast.LENGTH_SHORT).show();
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
                // TODO Auto-generated method stub

            }
        });

        key = "pj_joke_image_" + System.currentTimeMillis();
        requestUpToken.putParam("key", key);

        HttpRequestManager.getInstance().sendRequest(requestUpToken);
    }

    private void openGallery() {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);

        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

        startActivityForResult(intent, REQUEST_IMAGE);
    }
}
