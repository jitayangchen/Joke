package com.pepoc.joke.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.presenter.CollectedJokePresenter;
import com.pepoc.joke.view.adapter.CollectedJokeAdapter;
import com.pepoc.joke.view.iview.ICollectedJokeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CollectedJokeActivity extends BaseSwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener, ICollectedJokeView<JokeContent> {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview_collected_joke_)
    RecyclerView recyclerviewCollectedJoke;
    @Bind(R.id.swiperefresh_collected_joke)
    SwipeRefreshLayout swiperefreshCollectedJoke;

    private CollectedJokeAdapter collectedJokeAdapter;

    private CollectedJokePresenter collectedJokePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_joke);
        ButterKnife.bind(this);

        collectedJokePresenter = new CollectedJokePresenter(this);
        init();

        collectedJokePresenter.getData(context, true);
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.menu_collected);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        swiperefreshCollectedJoke.setColorSchemeResources(R.color.colorAccent);
        swiperefreshCollectedJoke.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerviewCollectedJoke.setLayoutManager(linearLayoutManager);
        collectedJokeAdapter = new CollectedJokeAdapter(context);
        recyclerviewCollectedJoke.setAdapter(collectedJokeAdapter);
    }

    @Override
    public void onRefresh() {
        collectedJokePresenter.getData(context, true);
    }

    @Override
    public void onSuccess() {
        swiperefreshCollectedJoke.setRefreshing(false);
    }

    @Override
    public void onFail() {
        swiperefreshCollectedJoke.setRefreshing(false);
    }

    @Override
    public void updateData(List<JokeContent> datas, boolean isRefresh) {
        if (isRefresh) {
            collectedJokeAdapter.getDatas().clear();
        }
        collectedJokeAdapter.setDatas(datas);
        collectedJokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
