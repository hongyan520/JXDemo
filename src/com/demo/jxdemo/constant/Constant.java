package com.demo.jxdemo.constant;

import java.util.List;
import java.util.Map;

import android.os.Environment;

import com.demo.jxdemo.application.AllActivityListApplication;

public class Constant
{
	public final static String SYNTIME = "2014-01-24";

	public final static String USER_NAME = "USER_NAME";

	public final static String USER_NICKNAME = "USER_NICKNAME";

	public final static String USER_ID = "USER_ID";

	public final static String USER_TOKEN = "USER_TOKEN";

	public final static String USER_TEL = "USER_TEL";

	public final static String USER_PASSWORD = "USER_PASSWORD";

	public final static String USER_TYPE = "USER_TYPE";

	public final static String USER_AVATAR = "USER_AVATAR";

	public final static String USER_GENDER = "USER_GENDER";

	public final static String USER_LOCATION = "USER_LOCATION";

	public final static String USER_JOB = "USER_JOB";

	public final static String USER_INTRODUCTION = "USER_INTRODUCTION";

	public final static String USER_DIGEST = "Digest";

//	public final static String USER_COURSEARRAY_LIST = "USER_COURSEARRAY_LIST";//List<Map<String, Object>>

	public final static String OPTION = "OPTION";

	public final static String SERVER_IP = "SERVER_IP";

	public final static String SERVER_PORT = "SERVER_PORT";

	public final static String PROJECT_NAME = "PROJECT_NAME";

	public final static String MOBILEPHONE = "MOBILEPHONE";

	public final static String EMPTY_STRING = "";

	public final static String DEFAULT_SERVER_IP = "172.16.17.231";// "172.16.17.92";

	public final static String DEFAULT_SERVER_PORT = "7000";// "9090";

	public final static String DEFAULT_PROJECT_NAME = "jixue";

	/**
	 * 是否正在进行图标的移动，默认为false，意味着不会对触摸事件进行拦截
	 */
	public static boolean ISITEMSCROLL = false;

	public final static String THEMEKEY = "THEME";

	public final static String THEMEVALUE = "0";

	public static Integer PAGENUMHOW = 0;

	public static Integer PAGESIZE = 0;

	public final static String RESPONSEREADTIMEVALUE = "5000";// 设置请求超时时间值

	public final static String MOBILE_POST_URL = "MOBILE_POST_URL";

	public final static String MOBILE_ROOT_URL = "MOBILE_ROOT_URL";// 根路径

	public final static String FAIL = "1"; // 失败

	public final static String SUCCESS = "0"; // 成功

	/**
	 * 登录失败
	 */
	public static final String LOGIN_ERROR = "LOGIN_ERROR";

	/** 是否开启声音 */
	public final static String ISSOUND = "ON";

	/** 是否开启振动 */
	public final static String ISVIBRATE = "OFF";

	/**
	 * 看过介绍标志
	 */
	public final static String LOOK_INTRDOUCE = "look_introduce";

	public final static String ZERO = "0";

	public final static String ONE = "1";

	/** 自动登录 */
	public final static String ISAUTOLOGIN = "OFF";

	public final static String CACHE_PATH = AllActivityListApplication.getInstance().getApplicationContext().getCacheDir().getPath()+ "/";
	
	/**
	 * 缓存根目录
	 */
	public static final String BASE_CACHE_PATH = Environment.getExternalStorageDirectory().toString() + "/gixue/";
	
	/**
	 * 静态缓存目录
	 */
	public static final String STATIC_PATH = "static/";
	
	/**
	 * API动态缓存目录
	 */
	public static final String API_PATH = "Api/";
	
	/**
	 * 是否使用缓存
	 */
	public static final boolean isUseCache = true;

}
