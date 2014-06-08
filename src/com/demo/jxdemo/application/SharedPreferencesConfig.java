package com.demo.jxdemo.application;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.demo.jxdemo.constant.Constant;

public class SharedPreferencesConfig
{

	// 键值对存储文件名称
	public final static String CONFIG_NAME = "KC_MOBILE_CONFIG";

	/**
	 * 键值对存储文件值初始化
	 * 
	 * @param context
	 * @return Map<String, String>
	 */
	public synchronized static Map<String, String> config(Context context)
	{
		Map<String, String> configMap = new HashMap<String, String>();
		SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		// 用户名登录名
		configMap.put(Constant.USER_NAME, sharedPreferences.getString(Constant.USER_NAME, Constant.EMPTY_STRING));
		// 用户显示名
		configMap.put(Constant.USER_NICKNAME, sharedPreferences.getString(Constant.USER_NICKNAME, Constant.EMPTY_STRING));
		// 用户ID
		configMap.put(Constant.USER_ID, sharedPreferences.getString(Constant.USER_ID, Constant.EMPTY_STRING));
		// 登录记录令牌
		configMap.put(Constant.USER_TOKEN, sharedPreferences.getString(Constant.USER_TOKEN, Constant.EMPTY_STRING));
		// 用户密码
		configMap.put(Constant.USER_PASSWORD, sharedPreferences.getString(Constant.USER_PASSWORD, Constant.EMPTY_STRING));
		// 反馈意见
		configMap.put(Constant.OPTION, sharedPreferences.getString(Constant.OPTION, Constant.EMPTY_STRING));
		// 手机号码
		configMap.put(Constant.USER_TEL, sharedPreferences.getString(Constant.USER_TEL, Constant.EMPTY_STRING));
		// 服务器IP
		configMap.put(Constant.SERVER_IP, sharedPreferences.getString(Constant.SERVER_IP, Constant.DEFAULT_SERVER_IP));
		// 服务器端口
		configMap.put(Constant.SERVER_PORT, sharedPreferences.getString(Constant.SERVER_PORT, Constant.DEFAULT_SERVER_PORT));
		// 工程名称
		configMap.put(Constant.PROJECT_NAME, sharedPreferences.getString(Constant.PROJECT_NAME, Constant.DEFAULT_PROJECT_NAME));
		// 手机号码
		configMap.put(Constant.MOBILEPHONE, sharedPreferences.getString(Constant.MOBILEPHONE, Constant.EMPTY_STRING));
		// 主题(内部)
		configMap.put(Constant.THEMEKEY, sharedPreferences.getString(Constant.THEMEKEY, Constant.THEMEVALUE));
		// 是否看过介绍
		configMap.put(Constant.LOOK_INTRDOUCE, sharedPreferences.getString(Constant.LOOK_INTRDOUCE, Constant.ZERO));
		// 自动登录
		configMap.put(Constant.ISAUTOLOGIN, sharedPreferences.getString(Constant.ISAUTOLOGIN, "OFF"));

		// // 和服务器通讯串文件（交互）
		// configMap.put(
		// Constant.MOBILE_FILE_POST_URL,
		// String.format("http://%s:%s/%s/androidFileUploadServlet",
		// sharedPreferences.getString(Constant.SERVER_IP, Constant.DEFAULT_SERVER_IP),
		// sharedPreferences.getString(Constant.SERVER_PORT, Constant.DEFAULT_SERVER_PORT),
		// sharedPreferences.getString(Constant.PROJECT_NAME, Constant.DEFAULT_PROJECT_NAME)));
		//
		// // 和服务器通讯串文件（交互）
		// configMap.put(Constant.MOBILE_FILE_URL, String.format("http://%s:%s/%s",
		// sharedPreferences.getString(Constant.SERVER_IP, Constant.DEFAULT_SERVER_IP),
		// sharedPreferences.getString(Constant.SERVER_PORT, Constant.DEFAULT_SERVER_PORT),
		// sharedPreferences.getString(Constant.PROJECT_SERVER_NAME, Constant.DEFAULT_PROJECT_SERVER_NAME)));
		//
		// 和服务器通讯串（交互）
		configMap.put(Constant.MOBILE_POST_URL, String.format("http://%s:%s/%s/execute.jsp",
				sharedPreferences.getString(Constant.SERVER_IP, Constant.DEFAULT_SERVER_IP),
				sharedPreferences.getString(Constant.SERVER_PORT, Constant.DEFAULT_SERVER_PORT),
				sharedPreferences.getString(Constant.PROJECT_NAME, Constant.DEFAULT_PROJECT_NAME)));

		// 和服务器通讯串（交互）
		configMap.put(Constant.MOBILE_ROOT_URL, String.format("http://%s:%s/%s/",
				sharedPreferences.getString(Constant.SERVER_IP, Constant.DEFAULT_SERVER_IP),
				sharedPreferences.getString(Constant.SERVER_PORT, Constant.DEFAULT_SERVER_PORT),
				sharedPreferences.getString(Constant.PROJECT_NAME, Constant.DEFAULT_PROJECT_NAME)));

		// // 安卓安装文件存放地址
		// configMap.put(
		// Constant.UPDATE_ANDROID_APK_URL,
		// String.format("http://%s:%s/%s/androidversionupload/",
		// sharedPreferences.getString(Constant.SERVER_IP, Constant.DEFAULT_SERVER_IP),
		// sharedPreferences.getString(Constant.SERVER_PORT, Constant.DEFAULT_SERVER_PORT),
		// sharedPreferences.getString(Constant.PROJECT_NAME, Constant.DEFAULT_PROJECT_NAME)));
		// // 和服务器通讯串（下载）
		// configMap.put(
		// Constant.UPLOAD_NAME,
		// String.format("http://%s:%s/%s/android_upload.jsp",
		// sharedPreferences.getString(Constant.SERVER_IP, Constant.DEFAULT_SERVER_IP),
		// sharedPreferences.getString(Constant.SERVER_PORT, Constant.DEFAULT_SERVER_PORT),
		// sharedPreferences.getString(Constant.PROJECT_NAME, Constant.DEFAULT_PROJECT_NAME)));
		//
		// // 附件对应服务端的图片下载地址URL
		// configMap.put(Constant.DOWNLOADFILE_URL, String.format("http://%s:%s/hyzl",
		// sharedPreferences.getString(Constant.SERVER_IP, Constant.DEFAULT_SERVER_IP),
		// sharedPreferences.getString(Constant.SERVER_PORT, Constant.DEFAULT_SERVER_PORT)));
		//
		// configMap.put(Constant.IS_REMIND, sharedPreferences.getString(Constant.IS_REMIND, Constant.IS_REMIND_VALUE));
		//
		// configMap.put(Constant.IS_AUTO_LOGIN, sharedPreferences.getString(Constant.IS_AUTO_LOGIN, Constant.ZERO));// 0手动 1自动
		// configMap.put(Constant.DOCUMENTNOTICESYNNAME,
		// sharedPreferences.getString(Constant.DOCUMENTNOTICESYNNAME, Constant.DOCUMENTNOTICESYNTIME));
		// configMap.put(Constant.CONNECTION_STRING_NAME,
		// sharedPreferences.getString(Constant.CONNECTION_STRING_NAME, Constant.CONNECTION_STRING));
		return configMap;
	}

	/**
	 * 覆盖文件存储库的值
	 * 
	 * @param context
	 * @param keyName
	 * @param keyValue
	 */
	public synchronized static void saveConfig(Context context, String keyName, String keyValue)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(keyName, keyValue);
		editor.commit();
	}
}
