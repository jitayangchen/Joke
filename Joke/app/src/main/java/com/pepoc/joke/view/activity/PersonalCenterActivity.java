package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.ImageLoadding;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestGetPublishedJokes;
import com.pepoc.joke.view.adapter.PersonalCenterAdapter;

import java.util.List;

public class PersonalCenterActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    private RecyclerView recyclerviewPublishedJoke;

    private PersonalCenterAdapter jokeListAdapter;

    private ImageView ivAvatar;

    private ImageView ivPersonalBackground;

    private CollapsingToolbarLayout collapsingToolbar;

    /** 是否还有更多数据 */
    private boolean isHasMoreData = true;

    /** 是否正在请求数据 */
    private boolean isRequesting = false;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);

        init();
    }

    @Override
    public void init() {
        super.init();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("UserId");
        String nickName = intent.getStringExtra("NickName");
        String avatar = intent.getStringExtra("avatar");
        UserManager.getCurrentUser().getUserId();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        ImageLoadding.load(context, avatar, ivAvatar);

        getData(true, userId);
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

    /**
     * 获取数据
     * @param isRefresh true:是刷新     false:加载更多
     */
    private void getData(final boolean isRefresh, String userId) {
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
                if (isRefresh) {
                    jokeListAdapter.getDatas().clear();
                }
                jokeListAdapter.setDatas(datas);
                jokeListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        request.putParam("page", String.valueOf(page));
        request.putParam("userId", userId);

        HttpRequestManager.getInstance().sendRequest(request);
    }
}
