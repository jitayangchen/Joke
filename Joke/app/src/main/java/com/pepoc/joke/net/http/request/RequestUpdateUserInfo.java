package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

public class RequestUpdateUserInfo extends HttpRequest {
	
	public RequestUpdateUserInfo(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.URL = "updateuserinfo.php";
	}

	@Override
	public Object parseResponseResult(String result) {
		return result;
	}

}
