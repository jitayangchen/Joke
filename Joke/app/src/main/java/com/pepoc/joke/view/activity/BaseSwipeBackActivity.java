package com.pepoc.joke.view.activity;

import android.content.Context;
import android.os.Bundle;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Yangchen on 2015/11/30.
 */
public class BaseSwipeBackActivity extends SwipeBackActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    /**
     * init views or data
     */
    public void init() {
    }
}
