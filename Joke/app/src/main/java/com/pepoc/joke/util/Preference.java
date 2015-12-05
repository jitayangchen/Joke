package com.pepoc.joke.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.pepoc.joke.JokeApplication;

public class Preference {
	
	private static final String ACCOUNT_NUMBER = "account_number";
	private static final String PASSWORD = "password";
	private static final String USER_ID = "user_id";
	private static final String IS_AUTO_LOGIN = "is_auto_login";

	public static void saveAccountNumber(String accountNumber) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JokeApplication.context);
		Editor editor = sp.edit();
		editor.putString(ACCOUNT_NUMBER, accountNumber);
		editor.commit();
	}
	
	public static String getAccountNumber() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JokeApplication.context);
		return sp.getString(ACCOUNT_NUMBER, null);
	}
	
	public static void savePassword(String password) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JokeApplication.context);
		Editor editor = sp.edit();
		editor.putString(PASSWORD, password);
		editor.commit();
	}
	
	public static String getPassword() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JokeApplication.context);
		return sp.getString(PASSWORD, null);
	}
	
	public static void saveUserId(String userId) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JokeApplication.context);
		Editor editor = sp.edit();
		editor.putString(USER_ID, userId);
		editor.commit();
	}
	
	public static String getUserId() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JokeApplication.context);
		return sp.getString(USER_ID, null);
	}
	
	public static void saveAutoLogin(boolean isAutoLogin) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JokeApplication.context);
		Editor editor = sp.edit();
		editor.putBoolean(IS_AUTO_LOGIN, isAutoLogin);
		editor.commit();
	}
	
	public static boolean isAutoLogin() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(JokeApplication.context);
		return sp.getBoolean(IS_AUTO_LOGIN, false);
	}
	
}
