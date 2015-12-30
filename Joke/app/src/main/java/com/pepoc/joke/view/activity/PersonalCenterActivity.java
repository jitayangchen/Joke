package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.net.ImageLoadding;
import com.pepoc.joke.presenter.PersonalCenterPresenter;
import com.pepoc.joke.view.adapter.PersonalCenterAdapter;
import com.pepoc.joke.view.iview.IPersonalCenterView;

import java.util.List;

public class PersonalCenterActivity extends BaseSwipeBackActivity implements View.OnClickListener, IPersonalCenterView<JokeContent> {

    private RecyclerView recyclerviewPublishedJoke;

    private PersonalCenterAdapter jokeListAdapter;

    private ImageView ivAvatar;

    private ImageView ivPersonalBackground;

    private CollapsingToolbarLayout collapsingToolbar;

    private PersonalCenterPresenter personalCenterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        personalCenterPresenter = new PersonalCenterPresenter(this);
        init();
    }

    @Override
    public void init() {
        super.init();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("UserId");
        String nickName = intent.getStringExtra("NickName");
        String avatar = intent.getStringExtra("avatar");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(nickName);
        ivPersonalBackground = (ImageView) findViewById(R.id.iv_personal_background);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);

        recyclerviewPublishedJoke = (RecyclerView) findViewById(R.id.recyclerview_published_joke);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerviewPublishedJoke.setLayoutManager(linearLayoutManager);
        jokeListAdapter = new PersonalCenterAdapter(context);
        recyclerviewPublishedJoke.setAdapter(jokeListAdapter);

        ivPersonalBackground.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);

        Glide.with(this).load(R.mipmap.cheese_4).centerCrop().into(ivPersonalBackground);
        ImageLoadding.loadAvatar(context, avatar, ivAvatar);

        personalCenterPresenter.getData(context, true, userId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_personal_background:

                break;
            case R.id.iv_avatar:

                break;
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void updateData(List<JokeContent> datas, boolean isRefresh) {
        if (isRefresh) {
            jokeListAdapter.getDatas().clear();
        }
        jokeListAdapter.setDatas(datas);
        jokeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
