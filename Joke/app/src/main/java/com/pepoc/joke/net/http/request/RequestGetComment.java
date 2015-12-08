package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.data.bean.JokeComment;
import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestGetComment extends HttpRequest {
	
	public RequestGetComment(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.URL = "getcomment.php";
	}

	@Override
	public Object parseResponseResult(String result) throws JSONException {
		List<JokeComment> jokeComments = new ArrayList<>();
		JSONObject obj = new JSONObject(result);
		String status = obj.getString("status");
		if ("1".equals(status)) {
			JSONArray commentListArr = obj.getJSONArray("commentList");
			for (int i = 0; i < commentListArr.length(); i++) {
				JSONObject commentListObj = commentListArr.getJSONObject(i);
				JokeComment jokeComment = new JokeComment();
				jokeComment.setCommentId(commentListObj.getString("commentId"));
				jokeComment.setContent(commentListObj.getString("content"));
				jokeComment.setCreateTime(commentListObj.getString("createTime"));
				jokeComment.setLikeNumber(commentListObj.getString("likeNum"));
				jokeComment.setUserId(commentListObj.getString("userId"));
				jokeComment.setUserNickName(commentListObj.getString("nickName"));
				jokeComment.setUserAvatar(commentListObj.getString("avatar"));

				jokeComments.add(jokeComment);
			}
		}
		return jokeComments;
	}

}
