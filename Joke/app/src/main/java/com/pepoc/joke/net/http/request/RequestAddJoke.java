package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAddJoke extends HttpRequest {
	
	public RequestAddJoke(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.requestMethod = METHOD_POST;
		this.URL = "addjoke.php";
	}

	@Override
	public Object parseResponseResult(String result) throws JSONException {
		JSONObject obj = new JSONObject(result);
		String status = obj.getString("status");
		return status;
	}

}
