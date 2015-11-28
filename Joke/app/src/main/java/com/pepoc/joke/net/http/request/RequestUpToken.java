package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

public class RequestUpToken extends HttpRequest {
	
	public RequestUpToken(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.requestMethod = METHOD_POST;
		this.URL = "uptoken.php";
	}

	@Override
	public Object parseResponseResult(String result) {
		return result;
	}

}
