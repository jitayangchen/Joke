package com.pepoc.joke.view.activity;

import android.content.Intent;
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
import com.pepoc.joke.R;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.presenter.PublishJokePresenter;
import com.pepoc.joke.view.iview.IPublishJokeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class PublishJokeActivity extends BaseSwipeBackActivity implements View.OnClickListener, IPublishJokeView {

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
    private String imageUrl = null;
    private PublishJokePresenter publishJokePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_joke);
        ButterKnife.bind(this);
        publishJokePresenter = new PublishJokePresenter(this);
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
            String content = etJokeContent.getText().toString();
            publishJokePresenter.addJokeContent(context, content, null);
        } else {
            publishJokePresenter.upLoadImage(context, imageUrl);
        }
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

    @Override
    public void onSuccess() {
        Toast.makeText(context, "send success", Toast.LENGTH_SHORT).show();
        etJokeContent.setText("");
    }

    @Override
    public void onFail() {

    }

    @Override
    public void onUploadImageSuccess() {
        Log.i("qiniu", "=== upload success ===");
        Toast.makeText(context, "upload success", Toast.LENGTH_SHORT).show();
        String content = etJokeContent.getText().toString();
        publishJokePresenter.addJokeContent(context, content, imageUrl);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
