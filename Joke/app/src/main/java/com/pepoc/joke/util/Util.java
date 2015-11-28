package com.pepoc.joke.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Util {
	
	public static String getBaseFilePath() {
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProgrammerJoke";
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		} 
		return file.getAbsolutePath();
	}
	
	public static void hiddenSoftKeyborad(View v, Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} catch (Exception e) {
			Logger.e("have no getCurrentFocus", e);
		}
	}
	
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 
	
	public static int px2dip(Context context, float pxValue){ 
	        final float scale = context.getResources().getDisplayMetrics().density; 
	        return (int)(pxValue / scale + 0.5f); 
	}
	
	/**
	 * get package version name
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String ver = "";
		try {
			PackageManager pkgMgr = context.getPackageManager();
			PackageInfo info = pkgMgr.getPackageInfo(context.getPackageName(), 0);
			ver = info.versionName;
		} catch (NameNotFoundException e) {
			Logger.e("get Version Name", e);
		}
		return ver;
	}
	
	/**
	 * get package version code
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionCode(Context context) {
		int ver = 0;
		try {
			PackageManager pkgMgr = context.getPackageManager();
			PackageInfo info = pkgMgr.getPackageInfo(context.getPackageName(), 0);
			ver = info.versionCode;
		} catch (NameNotFoundException e) {
			Logger.e("get Version Name", e);
		}
		return ver + "";
	}
	
	/**
	 * get system version
	 * 
	 * @return
	 */
	public static String getSysVer() {
		return Build.VERSION.RELEASE;
	}
	
	/**
	 * get mac address
	 * @param ctx
	 * @return
	 */
	public static String getMacAddress(Context ctx) {
		
		String macAddress = null;
		WifiManager wifiMgr = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
		    macAddress = info.getMacAddress();
		}
		
		return macAddress;
	}
	
	public static String getDeviceModel() {
		return Build.MODEL;
	}
	
	/**
	 * get device IMEI, for cell phone only
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getDeviceIMEI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
