package com.pepoc.joke.net;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pepoc.joke.Config;

/**
 * Created by yangchen on 15-12-4.
 */
public class ImageLoadding {

    public static void loadAvatar(Context context, String url, ImageView imageView) {
        String imageUrl = Config.IMAGE_HOST + url + Config.IMAGE_SIZE_AVATAR;
        Glide.with(context).load(imageUrl).centerCrop().into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        String imageUrl = Config.IMAGE_HOST + url + Config.IMAGE_SIZE_JOKE_IMAGE;
//        Logger.i(imageUrl);
        Glide.with(context).load(imageUrl).fitCenter().into(imageView);
    }
}
