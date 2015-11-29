package com.pepoc.joke.view.activity;

import android.os.Bundle;

import com.pepoc.joke.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class LoginActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
