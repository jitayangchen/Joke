package com.pepoc.joke.net.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Http请求管理器
 * @author yangchen
 *
 */
public class HttpRequestManager {
	
	private static HttpRequestManager instance = null;
	private AsyncHttpClient asyncHttpClient;

	/**
	 * 普通Http请求超时时间，单位：毫秒      默认10000
	 */
	private final static int HTTP_REQUEST_TIMEOUT = 10000;
	
	/**
	 * 请求重试次数
	 */
	private final static int HTTP_RETRY_TIMES = 1;
	
	private HttpRequestManager() {
		asyncHttpClient = new AsyncHttpClient();
	}
	
	public static HttpRequestManager getInstance() {
		if (null == instance) {
			instance = new HttpRequestManager();
		}
		return instance;
	}

	/**
	 * 发送Http请求
	 * @param request
	 */
	public void sendRequest(HttpRequest request) {
		Logger.i("Http sendRequest >>>>>>>>>>>>>>> ");
		if (HttpRequest.METHOD_GET.equals(request.getRequestMethod())) {
			sendGetRequest(request);
		} else {
			sendPostRequest(request);
		}
	}

	/**
	 * 发送Get请求
	 * @param request
	 */
	private void sendGetRequest(final HttpRequest request) {

		asyncHttpClient.get(request.context, request.getURL(), requestParamsConvert(request.getParams()), new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				request.getOnHttpResponseListener().onError();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {

				Logger.i("Http response result <<<<<<<<<<<<<<< " + responseString);
				Object result = null;
				try {
					result = request.parseResponseResult(responseString);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				request.getOnHttpResponseListener().onHttpResponse(result);
			}

			@Override
			public void onStart() {
				super.onStart();
//				request.getOnHttpResponseListener().onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
//				request.getOnHttpResponseListener().onFinish();
			}
		});
	}

	/**
	 * 发送Post请求
	 * @param request
	 */
	private void sendPostRequest(final HttpRequest request) {
		asyncHttpClient.post(request.context, request.getURL(), requestParamsConvert(request.getParams()), new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				request.getOnHttpResponseListener().onError();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {

				Logger.i("Http response result <<<<<<<<<<<<<<< " + responseString);
				Object result = null;
				try {
					result = request.parseResponseResult(responseString);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				request.getOnHttpResponseListener().onHttpResponse(result);
			}

			@Override
			public void onStart() {
				super.onStart();
//				request.getOnHttpResponseListener().onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
//				request.getOnHttpResponseListener().onFinish();
			}
		});
	}

	/**
	 * Request params convert
	 * @param params
	 * @return
	 */
	private RequestParams requestParamsConvert(Map<String, String> params) {
		RequestParams requestParams = new RequestParams();
		if (params.size() > 0) {
			for (String key : params.keySet()) {
				requestParams.put(key, params.get(key));
			}
		}
		return requestParams;
	}

	public interface OnHttpResponseListener {
//		void onStart();
		void onHttpResponse(Object result);
		void onError();
//		void onFinish();
	}
	
}
