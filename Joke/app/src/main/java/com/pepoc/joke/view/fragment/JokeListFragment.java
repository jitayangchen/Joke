package com.pepoc.joke.view.fragment;

import android.content.Context;
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
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestGetJokes;
import com.pepoc.joke.observer.LoginObservable;
import com.pepoc.joke.view.adapter.JokeListAdapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class JokeListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, Observer {

//    @Bind(R.id.recyclerview_joke_list)
    private RecyclerView recyclerviewJokeList;
//    @Bind(R.id.swiperefresh_joke_list)
    private SwipeRefreshLayout swiperefreshJokeList;

    private JokeListAdapter jokeListAdapter;

    /** 是否还有更多数据 */
    private boolean isHasMoreData = true;

    /** 是否正在请求数据 */
    private boolean isRequesting = false;

    private int page = 1;

    private LinearLayoutManager linearLayoutManager;

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
                recyclerviewJokeList.scrollToPosition(2);
            }
        });
        init();
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
                if (!isRequesting && RecyclerView.SCROLL_STATE_IDLE == 0 && linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() - 1) {
                    if (!isHasMoreData) {
                        Toast.makeText(getContext(), "No More Data", Toast.LENGTH_SHORT).show();
                    } else {
                        Logger.i("-----------------LoadMore------------------");
                        getJokeData(false);
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

        getJokeData(true);
    }

    private void getJokeData(final boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        }
        isRequesting = true;
        RequestGetJokes requestGetJokes = new RequestGetJokes(getContext(), new HttpRequestManager.OnHttpResponseListener() {
            @Override
            public void onHttpResponse(Object result) {
                swiperefreshJokeList.setRefreshing(false);
                isRequesting = false;

                List<JokeContent> datas = (List<JokeContent>) result;
                if (datas.size() < 20) {
                    isHasMoreData = false;
                } else {
                    isHasMoreData = true;
                }
                if (isRefresh) {
                    jokeListAdapter.getDatas().clear();
                }
                jokeListAdapter.setDatas(datas);
                jokeListAdapter.notifyDataSetChanged();
                page++;
            }

            @Override
            public void onError() {
                swiperefreshJokeList.setRefreshing(false);
                isRequesting = false;
            }
        });

        requestGetJokes.putParam("page", String.valueOf(page));
        if (UserManager.getCurrentUser() == null) {
            requestGetJokes.putParam("userId", "-1");
        } else {
            requestGetJokes.putParam("userId", UserManager.getCurrentUser().getUserId());
        }
        HttpRequestManager.getInstance().sendRequest(requestGetJokes);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        getJokeData(true);
    }

    @Override
    public void update(Observable observable, Object data) {
        getJokeData(true);
    }
}
