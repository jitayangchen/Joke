package com.pepoc.joke.net.http.request;

import android.content.Context;
import android.text.TextUtils;

import com.pepoc.joke.data.bean.JokeContent;
import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestGetCollectedJokes extends HttpRequest {
	
	public RequestGetCollectedJokes(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.URL = "getcollectjokes.php";
	}

	@Override
	public Object parseResponseResult(String result) throws JSONException {
		List<JokeContent> datas = new ArrayList<JokeContent>();
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
				jokeContent.setCollectCount(jokesList.getString("collectCount"));
				jokeContent.setIslike(jokesList.getString("islike"));
				jokeContent.setLikeCount(jokesList.getString("likeCount"));

				String imageUrl = jokesList.getString("imagesUrl");
				if (!TextUtils.isEmpty(imageUrl)) {
					String[] str = imageUrl.split("&");
					jokeContent.setImageUrl(str[0]);
					if (str.length >= 3) {
						jokeContent.setImageWidth(Integer.valueOf(str[1]));
						jokeContent.setImageHeight(Integer.valueOf(str[2]));
					}
				}
				datas.add(jokeContent);
			}
		}
		return datas;
	}

}
