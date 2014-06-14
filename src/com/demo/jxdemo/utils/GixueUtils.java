package com.demo.jxdemo.utils;

import com.demo.jxdemo.constant.Constant;

public class GixueUtils {

	/**
	 * 静态资源 服务器路径转化为本地缓存路径
	 * 逻辑：将UUID的目录部分作为此文件的文件名
	 * @param serverUrl
	 * @return
	 */
	public static String staticServerUrlConvertToCachePath(String serverUrl) {
		if("".equals(serverUrl)){
			return "";
		}
		String localUrl = "";
		int j = serverUrl.lastIndexOf("/")-1;
		int i = serverUrl.lastIndexOf("/",j);
		localUrl = serverUrl.substring(i+1,j)+serverUrl.substring(serverUrl.lastIndexOf("."));
		return Constant.BASE_CACHE_PATH+Constant.STATIC_PATH+localUrl;
	}
	
	/**
	 * 获取动态资源url转化为规范的文件名缓存路径
	 * 逻辑：将所有特殊字符替换成“-”
	 * @param apiStr
	 * @return
	 */
	public static String getApiStrConvertToCachePath(String apiStr) {
		if("".equals(apiStr)){
			return "";
		}
		return Constant.BASE_CACHE_PATH+ Constant.API_PATH + apiStr.replaceAll("[?,=,&]", "-");
	}
	
}
