package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

public class RequestCollectJoke extends HttpRequest {
	
	public RequestCollectJoke(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.URL = "collectjoke.php";
	}

	@Override
	public Object parseResponseResult(String result) {
		return result;
	}

}
