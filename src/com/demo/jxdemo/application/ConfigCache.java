package com.demo.jxdemo.application;

import com.demo.base.util.FileUtils;
import com.demo.jxdemo.constant.Constant;

public class ConfigCache
{
	private static final String TAG = ConfigCache.class.getName();

	public static final int CONFIG_CACHE_MOBILE_TIMEOUT = 3600000; // 1 hour

	public static final int CONFIG_CACHE_WIFI_TIMEOUT = 300000; // 5 minute

	public static String getUrlCache(String url)
	{
		if (url == null)
		{
			return null;
		}

		String result = null;
		// File file = new File(Constant.CACHE_PATH + "/" + getCacheDecodeString(url));
		// if (file.exists() && file.isFile())
		// {
		// long expiredTime = System.currentTimeMillis() - file.lastModified();
		// Log.d(TAG, file.getAbsolutePath() + " expiredTime:" + expiredTime / 60000 + "min");
		// // 1. in case the system time is incorrect (the time is turn back long ago)
		// // 2. when the network is invalid, you can only read the cache
		// // if (CACHE_PATH.mNetWorkState != NetworkUtils.NETWORN_NONE && expiredTime < 0)
		// // {
		// // return null;
		// // }
		// // if (CACHE_PATH.mNetWorkState == NetworkUtils.NETWORN_WIFI && expiredTime > CONFIG_CACHE_WIFI_TIMEOUT)
		// // {
		// // return null;
		// // }
		// // else if (CACHE_PATH.mNetWorkState == NetworkUtils.NETWORN_MOBILE && expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT)
		// // {
		// // return null;
		// // }
		// try
		// {
		result = FileUtils.readFile(Constant.CACHE_PATH + url);
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }
		// }
		return result;
	}

	public static void setUrlCache(String data, String url)
	{
		// File file = new File(Constant.CACHE_PATH + "/" + getCacheDecodeString(url));
		// try
		// {
		// url =getCacheDecodeString(url);
		FileUtils.createFile(Constant.CACHE_PATH,url);
		// 创建缓存数据到磁盘，就是创建文件
		FileUtils.writeFile(data, Constant.CACHE_PATH + url);
		// }
		// catch (IOException e)
		// {
		// Log.d(TAG, "write " + file.getAbsolutePath() + " data failed!");
		// e.printStackTrace();
		// }
	}

	public static String getCacheDecodeString(String url)
	{
		// 1. 处理特殊字符
		// 2. 去除后缀名带来的文件浏览器的视图凌乱(特别是图片更需要如此类似处理，否则有的手机打开图库，全是我们的缓存图片)
		if (url != null)
		{
			return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
		}
		return null;
	}

}
