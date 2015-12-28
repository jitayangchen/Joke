package com.pepoc.joke.net.http.request;

import android.content.Context;

import com.pepoc.joke.data.user.UserInfo;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequest;
import com.pepoc.joke.net.http.HttpRequestManager.OnHttpResponseListener;
import com.pepoc.joke.observer.LoginObservable;
import com.pepoc.joke.util.Preference;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestLogin extends HttpRequest {
	
	public RequestLogin(Context context, OnHttpResponseListener onHttpResponseListener) {
		super(context, onHttpResponseListener);
		this.requestMethod = METHOD_POST;
		this.URL = "login.php";
	}

	@Override
	public Object parseResponseResult(String result) throws JSONException {
		JSONObject obj = new JSONObject(result);
		String status = obj.getString("status");
		if ("1".equals(status)) {
			JSONObject userInfoObj = obj.getJSONObject("userInfo");
			UserInfo userInfo = new UserInfo();
			userInfo.setAccountNumber(requestParams.get("accountNumber"));
			userInfo.setPassword(requestParams.get("password"));
			userInfo.setUserId(userInfoObj.getString("userId"));
			userInfo.setNickName(userInfoObj.getString("nickName"));
			userInfo.setSex(userInfoObj.getString("sex"));
			userInfo.setAge(userInfoObj.getString("age"));
			userInfo.setAvatar(userInfoObj.getString("avatar"));
			userInfo.setCity(userInfoObj.getString("city"));
			userInfo.setRegisterTime(userInfoObj.getString("registerTime"));
			userInfo.setLoginTime(userInfoObj.getString("loginTime"));
			userInfo.setLoginType(userInfoObj.getString("loginType"));

			UserManager.setCurrentUser(userInfo);
			Preference.saveAutoLogin(true);
			LoginObservable.getInstance().updateObserver(null);
			return true;
		}
		return false;
	}

}
