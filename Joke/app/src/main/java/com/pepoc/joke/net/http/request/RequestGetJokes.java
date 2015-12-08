package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestGetJokes extends HttpRequest {
	
	public RequestGetJokes(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.URL = "getjokes.php";
	}

	@Override
	public Object parseResponseResult(String result) {
		List<JokeContent> datas = new ArrayList<JokeContent>();
		try {
			JSONObject obj = new JSONObject(result);
			String status = obj.getString("status");
			if ("1".equals(status)) {
				JSONArray jokesLists = obj.getJSONArray("jokesList");
				for (int i = 0; i < jokesLists.length(); i++) {
					JokeContent jokeContent = new JokeContent();
					JSONObject jokesList = jokesLists.getJSONObject(i);
					jokeContent.setJokeId(jokesList.getString("jokeId"));
					jokeContent.setContent(jokesList.getString("jokeContent"));
					jokeContent.setCreateTime(jokesList.getString("createTime"));
					jokeContent.setUserId(jokesList.getString("userId"));
					jokeContent.setUserNickName(jokesList.getString("nickName"));
					jokeContent.setUserAvatar(jokesList.getString("avatar"));
					jokeContent.setIscollect(jokesList.getString("iscollect"));
					jokeContent.setCollectCount(jokesList.getString("collectCount"));
					jokeContent.setIslike(jokesList.getString("islike"));
					jokeContent.setLikeCount(jokesList.getString("likeCount"));
					jokeContent.setImageUrl(jokesList.getString("imagesUrl"));
					datas.add(jokeContent);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

}
