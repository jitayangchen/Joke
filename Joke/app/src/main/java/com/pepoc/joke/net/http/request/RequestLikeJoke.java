package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

public class RequestLikeJoke extends HttpRequest {
	
	public RequestLikeJoke(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.URL = "likejoke.php";
	}

	@Override
	public Object parseResponseResult(String result) {
		return result;
	}

}
