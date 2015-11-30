package com.pepoc.joke.net.http;

import android.content.Context;

import com.pepoc.joke.Config;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequest {

	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String DESCRIPTION = "";
	
	/**
	 * 默认Http请求方式为get
	 */
	protected String requestMethod = METHOD_GET;
	protected String URL = null;
	private OnHttpResponseListener onHttpResponseListener;
	protected Map<String, String> requestParams = new HashMap<>();
	protected Context context;

	public HttpRequest(Context context) {
		this.context = context;
	}

	public HttpRequest(Context context, OnHttpResponseListener onHttpResponseListener) {
		this.context = context;
		this.onHttpResponseListener = onHttpResponseListener;
	}

	/**
	 * 获取请求Url
	 * @return
	 */
	public String getURL() {
		return Config.HOST + URL;
	}
	
	/**
	 * 获取Http请求类型  get post or other
	 * @return
	 */
	public String getRequestMethod() {
		return requestMethod;
	}

	/**
	 * 设置请求参数
	 * @param key
	 * @param value
	 */
	public void putParam(String key, String value) {
		requestParams.put(key, value);
	}

	/**
	 * 获取请求参数
	 * @return
	 */
	public Map<String, String> getParams() {
		return requestParams;
	}
	
	public OnHttpResponseListener getOnHttpResponseListener() {
		return onHttpResponseListener;
	}

	/**
	 * 解析服务器返回数据
	 * @param result
	 */
	public abstract Object parseResponseResult(String result) throws JSONException;
	
}
