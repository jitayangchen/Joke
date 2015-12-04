package com.pepoc.joke.net;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.pepoc.joke.Config;

/**
 * Created by yangchen on 15-12-4.
 */
public class ImageLoadding {

    public static void load(Context context, String url, ImageView imageView) {
        String imageUrl = Config.IMAGE_HOST + url + Config.IMAGE_SIZE;
        Logger.i(imageUrl);
        Glide.with(context).load(imageUrl).centerCrop().into(imageView);
    }
}
