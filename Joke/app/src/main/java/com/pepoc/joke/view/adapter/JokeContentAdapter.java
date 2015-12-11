package com.pepoc.joke.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pepoc.joke.R;
import com.pepoc.joke.data.bean.JokeComment;
import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.ImageLoadding;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestCollectJoke;
import com.pepoc.joke.net.http.request.RequestLikeJoke;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangchen on 15-12-5.
 */
public class JokeContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private JokeContent jokeContent;
    private List<JokeComment> jokeComments = new ArrayList<>();

    public JokeContentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View headerJokeContent = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_joke_content, parent, false);
        View itemJokeComment = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke_comment, parent, false);
        if (viewType == 0) {
            return new ContentViewHolder(headerJokeContent);
        } else {
            return new CommentViewHolder(itemJokeComment);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            ImageLoadding.loadAvatar(context, jokeContent.getUserAvatar(), contentViewHolder.ivUserAvatar);
            contentViewHolder.tvUserName.setText(jokeContent.getUserNickName());

            if (TextUtils.isEmpty(jokeContent.getContent())) {
                contentViewHolder.tvContent.setVisibility(View.GONE);
            } else {
                contentViewHolder.tvContent.setText(jokeContent.getContent());
            }

            if (TextUtils.isEmpty(jokeContent.getImageUrl())) {
                contentViewHolder.ivJokeImage.setVisibility(View.GONE);
            } else {
                contentViewHolder.ivJokeImage.setVisibility(View.VISIBLE);
                ImageLoadding.loadImage(context, jokeContent.getImageUrl(), contentViewHolder.ivJokeImage);
            }
        } else {
            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            JokeComment jokeComment = jokeComments.get(position - 1);
            commentViewHolder.tvComment.setText(jokeComment.getContent());
            commentViewHolder.tvUserName.setText(jokeComment.getUserNickName());
            ImageLoadding.loadAvatar(context, jokeComment.getUserAvatar(), commentViewHolder.ivUserAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return jokeComments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setJokeContent(JokeContent jokeContent) {
        this.jokeContent = jokeContent;
    }

    public void setJokeComments(List<JokeComment> jokeComments) {
        this.jokeComments.addAll(jokeComments);
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_user_avatar)
        ImageView ivUserAvatar;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.btn_collect_joke)
        Button btnCollectJoke;
        @Bind(R.id.btn_like_joke)
        Button btnLikeJoke;
        @Bind(R.id.iv_joke_image)
        ImageView ivJokeImage;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            btnLikeJoke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeJoke();
                }
            });

            btnCollectJoke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collectJoke();
                }
            });
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_user_avatar)
        ImageView ivUserAvatar;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_comment)
        TextView tvComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void collectJoke() {
        RequestCollectJoke request = new RequestCollectJoke(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                String status = (String) result;
                if ("1".equals(status)) {
                    Toast.makeText(context, "Collect Success", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        request.putParam("jokeId", jokeContent.getJokeId());
        request.putParam("userId", UserManager.getCurrentUser().getUserId());
        HttpRequestManager.getInstance().sendRequest(request);
    }

    private void likeJoke() {
        RequestLikeJoke request = new RequestLikeJoke(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                String status = (String) result;
                if ("1".equals(status)) {
                    Toast.makeText(context, "Like Success", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        request.putParam("jokeId", jokeContent.getJokeId());
        request.putParam("userId", UserManager.getCurrentUser().getUserId());
        HttpRequestManager.getInstance().sendRequest(request);
    }
}
