package com.pepoc.joke.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.pepoc.joke.R;
import com.pepoc.joke.observer.LoginObservable;
import com.pepoc.joke.presenter.JokeListPresenter;
import com.pepoc.joke.view.adapter.JokeListAdapter;
import com.pepoc.joke.view.iview.IJokeListView;

import java.util.Observable;
import java.util.Observer;

public class JokeListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IJokeListView, Observer {

    private RecyclerView recyclerviewJokeList;
    private SwipeRefreshLayout swiperefreshJokeList;

    private JokeListAdapter jokeListAdapter;

    private LinearLayoutManager linearLayoutManager;

    private JokeListPresenter jokeListPresenter;

    public static JokeListFragment newInstance() {
        JokeListFragment fragment = new JokeListFragment();
        return fragment;
    }

    public JokeListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginObservable.getInstance().addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke_list, container, false);
        recyclerviewJokeList = (RecyclerView) view.findViewById(R.id.recyclerview_joke_list);
        swiperefreshJokeList = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_joke_list);

        view.findViewById(R.id.fab_scroll_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerviewJokeList.scrollToPosition(0);
            }
        });
        init();
        jokeListPresenter = new JokeListPresenter(this, jokeListAdapter);
        onLoadData();
        return view;
    }

    @Override
    public void init() {
        super.init();
        swiperefreshJokeList.setColorSchemeResources(R.color.colorAccent);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerviewJokeList.setLayoutManager(linearLayoutManager);
        jokeListAdapter = new JokeListAdapter(getContext());
        recyclerviewJokeList.setAdapter(jokeListAdapter);

        swiperefreshJokeList.setOnRefreshListener(this);

        recyclerviewJokeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!jokeListPresenter.isRequesting() && RecyclerView.SCROLL_STATE_IDLE == 0 && linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() - 1) {
                    if (!jokeListPresenter.isHasMoreData()) {
                        Toast.makeText(getContext(), "No More Data", Toast.LENGTH_SHORT).show();
                    } else {
                        Logger.i("-----------------LoadMore------------------");
                        jokeListPresenter.getJokeData(getContext(), false);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Logger.i("findLastVisibleItemPosition() === " + linearLayoutManager.findLastVisibleItemPosition());
            }
        });
    }

    @Override
    public void onLoadData() {
        super.onLoadData();

        jokeListPresenter.getJokeData(getContext(), true);
    }

    @Override
    public void onRefresh() {
        jokeListPresenter.getJokeData(getContext(), true);
    }

    @Override
    public void update(Observable observable, Object data) {
        jokeListPresenter.getJokeData(getContext(), true);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void updateData() {
        swiperefreshJokeList.setRefreshing(false);
    }

    @Override
    public void onError() {
        swiperefreshJokeList.setRefreshing(false);
    }
}
