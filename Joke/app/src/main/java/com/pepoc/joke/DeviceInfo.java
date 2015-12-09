package com.pepoc.joke;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by yangchen on 15-12-9.
 */
public class DeviceInfo {

    private static int[] screen = null;

    public static int[] getScreenSize(Activity context) {
        if (screen == null) {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            screen = new int[2];
            screen[0] = dm.widthPixels;
            screen[1] = dm.heightPixels;
        }
        return screen;
    }
}
