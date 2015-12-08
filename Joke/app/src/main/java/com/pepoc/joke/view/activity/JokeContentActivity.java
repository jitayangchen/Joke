package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeComment;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestComment;
import com.pepoc.joke.net.http.request.RequestGetComment;
import com.pepoc.joke.util.Util;
import com.pepoc.joke.view.adapter.JokeContentAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JokeContentActivity extends BaseSwipeBackActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview_joke_content)
    RecyclerView recyclerviewJokeContent;
    @Bind(R.id.swiperefresh_joke_content)
    SwipeRefreshLayout swiperefreshJokeContent;
    @Bind(R.id.et_joke_comment)
    EditText etJokeComment;
    @Bind(R.id.btn_send_comment)
    Button btnSendComment;
    @Bind(R.id.rl_comment)
    RelativeLayout rlComment;

    private JokeContent jokeContent;
    private JokeContentAdapter jokeContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_content);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        jokeContent = intent.getParcelableExtra("JokeContent");
        init();
        getComment();
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.activity_joke_content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swiperefreshJokeContent.setColorSchemeResources(R.color.colorAccent);
        swiperefreshJokeContent.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerviewJokeContent.setLayoutManager(linearLayoutManager);

        jokeContentAdapter = new JokeContentAdapter(context);
        jokeContentAdapter.setJokeContent(jokeContent);
        recyclerviewJokeContent.setAdapter(jokeContentAdapter);

        btnSendComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_comment:
                if (UserManager.getCurrentUser() != null) {
                    String commentContent = etJokeComment.getText().toString();
                    if (TextUtils.isEmpty(commentContent)) {
                        Toast.makeText(context, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        comment(commentContent);
                    }
                } else {
                    Toast.makeText(context, "登录后才能评论", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 获取评论
     */
    private void getComment() {
        RequestGetComment request = new RequestGetComment(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                List<JokeComment> jokeComments = (List<JokeComment>) result;
                jokeContentAdapter.setJokeComments(jokeComments);
                jokeContentAdapter.notifyDataSetChanged();
                swiperefreshJokeContent.setRefreshing(false);
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub
                swiperefreshJokeContent.setRefreshing(false);
            }
        });

        request.putParam("jokeId", jokeContent.getJokeId());

        HttpRequestManager.getInstance().sendRequest(request);
    }

    /**
     * 发表评论
     */
    private void comment(String commentContent) {
        RequestComment request = new RequestComment(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                boolean isSuccess = (Boolean) result;
                if (isSuccess) {
                    Toast.makeText(context, "comment success", Toast.LENGTH_SHORT).show();
                    etJokeComment.setText("");
                    Util.hiddenSoftKeyborad(etJokeComment, context);
                    getComment();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        request.putParam("content", commentContent);
        request.putParam("jokeId", jokeContent.getJokeId());
        request.putParam("userId", UserManager.getCurrentUser().getUserId());

        HttpRequestManager.getInstance().sendRequest(request);
    }

    @Override
    public void onRefresh() {
        getComment();
    }
}
