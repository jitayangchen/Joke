package com.pepoc.joke.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pepoc.joke.DeviceInfo;
import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.net.ImageLoadding;
import com.pepoc.joke.util.Util;
import com.pepoc.joke.view.activity.JokeContentActivity;
import com.pepoc.joke.view.activity.PersonalCenterActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yangchen on 2015/11/30.
 */
public class JokeListAdapter extends RecyclerView.Adapter<JokeListAdapter.ViewHolder> {

    private Context context;
    private List<JokeContent> datas = new ArrayList<>();
    private int screenWidth ,imageViewWidth;

    public JokeListAdapter(Context context) {
        this.context = context;
        screenWidth = DeviceInfo.getScreenSize((Activity) context)[0];
        imageViewWidth = screenWidth - Util.dp2px(context, 20);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JokeContent jokeContent = datas.get(position);
        if (TextUtils.isEmpty(jokeContent.getContent())) {
            holder.tvContent.setVisibility(View.GONE);
        } else {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(jokeContent.getContent());
        }
        ImageLoadding.loadAvatar(context, jokeContent.getUserAvatar(), holder.ivUserAvatar);
        holder.tvCreateTime.setText(jokeContent.getCreateTime());
        holder.tvJokeId.setText(jokeContent.getJokeId());
        holder.tvLikeCount.setText(jokeContent.getLikeCount());
        holder.tvCollectCount.setText(jokeContent.getCollectCount());
        holder.tvUserName.setText(jokeContent.getUserNickName());

        if (TextUtils.isEmpty(jokeContent.getImageUrl())) {
            holder.ivJokeImage.setVisibility(View.GONE);
        } else {
            holder.ivJokeImage.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params;
            if (jokeContent.getImageWidth() > 0 && jokeContent.getImageHeight() > 0) {
                float imageHeight = jokeContent.getImageHeight() * ((float) imageViewWidth / (float) jokeContent.getImageWidth());
                params = new LinearLayout.LayoutParams(imageViewWidth, (int) imageHeight);

            } else {
                params = new LinearLayout.LayoutParams(imageViewWidth, imageViewWidth);
            }
            holder.ivJokeImage.setLayoutParams(params);
            ImageLoadding.loadImage(context, jokeContent.getImageUrl(), holder.ivJokeImage);
        }

        if ("1".equals(jokeContent.getIslike())) {
//            holder.btnLikeJoke.setText("喜欢:" + "OK");
        } else {
//            holder.btnLikeJoke.setText("喜欢:" + "NO");
        }

        if ("1".equals(jokeContent.getIscollect())) {
//            holder.btnCollectJoke.setText("收藏:" + "OK");
        } else {
//            holder.btnCollectJoke.setText("收藏:" + "NO");
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public List<JokeContent> getDatas() {
        return datas;
    }

    public void setDatas(List<JokeContent> datas) {
        this.datas.addAll(datas);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_user_avatar)
        ImageView ivUserAvatar;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_create_time)
        TextView tvCreateTime;
        @Bind(R.id.tv_joke_id)
        TextView tvJokeId;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.iv_joke_image)
        ImageView ivJokeImage;
        @Bind(R.id.btn_like_joke)
        ImageButton btnLikeJoke;
        @Bind(R.id.tv_like_count)
        TextView tvLikeCount;
        @Bind(R.id.btn_collect_joke)
        ImageButton btnCollectJoke;
        @Bind(R.id.tv_collect_count)
        TextView tvCollectCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JokeContentActivity.class);
                    intent.putExtra("JokeContent", datas.get(getLayoutPosition()));
                    context.startActivity(intent);
                }
            });

            ivUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JokeContent jokeContent = datas.get(getLayoutPosition());
                    Intent intent = new Intent(context, PersonalCenterActivity.class);
                    intent.putExtra("UserId", jokeContent.getUserId());
                    intent.putExtra("NickName", jokeContent.getUserNickName());
                    intent.putExtra("avatar", jokeContent.getUserAvatar());
                    context.startActivity(intent);
                }
            });
        }
    }
}
