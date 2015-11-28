package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

public class RequestRegister extends HttpRequest {
	
	public RequestRegister(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.requestMethod = METHOD_POST;
		this.URL = "register.php";
	}

	@Override
	public Object parseResponseResult(String result) {
		
		return result;
	}

}
