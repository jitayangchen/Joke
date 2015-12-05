package com.pepoc.joke.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestGetCollectedJokes;
import com.pepoc.joke.view.adapter.JokeListAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CollectedJokeActivity extends BaseSwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview_collected_joke_)
    RecyclerView recyclerviewCollectedJoke;
    @Bind(R.id.swiperefresh_collected_joke)
    SwipeRefreshLayout swiperefreshCollectedJoke;

    private JokeListAdapter jokeListAdapter;

    /** 是否还有更多数据 */
    private boolean isHasMoreData = true;

    /** 是否正在请求数据 */
    private boolean isRequesting = false;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_joke);
        ButterKnife.bind(this);

        init();

        getData(true);
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.menu_collected);

        swiperefreshCollectedJoke.setColorSchemeResources(R.color.colorAccent);
        swiperefreshCollectedJoke.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerviewCollectedJoke.setLayoutManager(linearLayoutManager);
        jokeListAdapter = new JokeListAdapter(context);
        recyclerviewCollectedJoke.setAdapter(jokeListAdapter);
    }

    @Override
    public void onRefresh() {
        getData(true);
    }

    /**
     * 获取数据
     * @param isRefresh true:是刷新     false:加载更多
     */
    private void getData(final boolean isRefresh) {
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

        RequestGetCollectedJokes request = new RequestGetCollectedJokes(context, new HttpRequestManager.OnHttpResponseListener() {

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
                swiperefreshCollectedJoke.setRefreshing(false);
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        request.putParam("page", String.valueOf(page));
        request.putParam("userId", UserManager.getCurrentUser().getUserId());

        HttpRequestManager.getInstance().sendRequest(request);
    }
}
