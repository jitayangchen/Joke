package com.pepoc.joke.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestGetJokes;
import com.pepoc.joke.view.adapter.JokeListAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JokeListFragment extends BaseFragment {

    @Bind(R.id.recyclerview_joke_list)
    RecyclerView recyclerviewJokeList;
    @Bind(R.id.swiperefresh_joke_list)
    SwipeRefreshLayout swiperefreshJokeList;

    private JokeListAdapter jokeListAdapter;

    /** 是否还有更多数据 */
    private boolean isHasMoreData = true;

    /** 是否正在请求数据 */
    private boolean isRequesting = false;

    private int page = 1;

    public static JokeListFragment newInstance() {
        JokeListFragment fragment = new JokeListFragment();
        return fragment;
    }

    public JokeListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke_list, container, false);
        ButterKnife.bind(this, view);

        init();
        onLoadData();
        return view;
    }

    @Override
    public void init() {
        super.init();
        swiperefreshJokeList.setColorSchemeResources(R.color.colorAccent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerviewJokeList.setLayoutManager(linearLayoutManager);
        jokeListAdapter = new JokeListAdapter(getContext());
        recyclerviewJokeList.setAdapter(jokeListAdapter);
    }

    @Override
    public void onLoadData() {
        super.onLoadData();

        getJokeData();
    }

    private void getJokeData() {
        RequestGetJokes requestGetJokes = new RequestGetJokes(getContext(), new HttpRequestManager.OnHttpResponseListener() {
            @Override
            public void onHttpResponse(Object result) {

                List<JokeContent> datas = (List<JokeContent>) result;
                if (datas.size() < 20) {
                    isHasMoreData = false;
                }
                jokeListAdapter.setDatas(datas);
                jokeListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                page--;
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
        ButterKnife.unbind(this);
    }
}
