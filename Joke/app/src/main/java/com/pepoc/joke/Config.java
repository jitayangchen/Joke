package com.pepoc.joke;

/**
 * 全局配置
 * @author yangchen
 *
 */
public class Config {

	/**
	 * is release
	 */
	public final static boolean IS_RELEASE = false;

	/**
	 * Http 程序服务器主机地址
	 */
	public final static String HOST = "http://www.pepoc.com/programmerjoke/";
	
	/**
	 * 图片服务器主机地址
	 */
	public final static String IMAGE_HOST = "http://source.findfine.com.cn/";
	
	/**
	 * 七牛图片尺寸100*100
	 */
	public final static String IMAGE_SIZE_AVATAR = "?imageView2/2/w/200/h/200/q/100/format/JPG";

	public final static String IMAGE_SIZE_JOKE_IMAGE = "?imageView2/2/w/800/h/800/q/100/format/JPG";
	
	/**
	 * 微信AppID
	 */
	public final static String WX_APP_ID = "wxa1b185c7d104a652";
	
	/**
	 * 分享链接
	 */
	public final static String SHARE_SINGLE_JOKE_URL = "http://pepoc.com/programmerjoke/singlejoke.php?joke_id=";

}
