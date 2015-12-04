package com.pepoc.joke.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pepoc.joke.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PublishedJokeActivity extends BaseSwipeBackActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published_joke);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.menu_published);
    }
}
