package com.pepoc.joke.net;

import android.content.Context;
import android.widget.ImageView;

import com.pepoc.joke.Config;
import com.squareup.picasso.Picasso;

/**
 * Created by yangchen on 15-12-4.
 */
public class ImageLoadding {

    public static void loadAvatar(Context context, String url, ImageView imageView) {
        String imageUrl = Config.IMAGE_HOST + url + Config.IMAGE_SIZE_AVATAR;
//        Glide.with(context).load(imageUrl).centerCrop().into(imageView);
        Picasso.with(context).load(imageUrl).fit().centerCrop().into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        String imageUrl = Config.IMAGE_HOST + url;
//        Logger.i(imageUrl);
//        Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imageView);
        Picasso.with(context).load(imageUrl).into(imageView);
    }
}
