package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestComment extends HttpRequest {
	
	public RequestComment(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.requestMethod = METHOD_POST;
		this.URL = "comment.php";
	}

	@Override
	public Object parseResponseResult(String result) throws JSONException {
		JSONObject obj = new JSONObject(result);
		String status = obj.getString("status");
		if ("1".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

}
