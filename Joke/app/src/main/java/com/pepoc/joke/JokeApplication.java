package com.pepoc.joke;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by yangchen on 15-11-28.
 */
public class JokeApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    /**
     * App init
     */
    private void init() {

        LeakCanary.install(this);

        Logger.init("Joke")                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(2);                // default 0
    }
}
