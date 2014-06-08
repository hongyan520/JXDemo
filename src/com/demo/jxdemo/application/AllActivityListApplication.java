package com.demo.jxdemo.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.WindowManager;

import com.demo.jxdemo.ui.activity.main.MainActivity;

public class AllActivityListApplication extends Application
{

	private List<Activity> list = new ArrayList<Activity>();

	/**
	 * 单例模式
	 */
	private static AllActivityListApplication instance;

	/**
	 * 全局的布局属性
	 */
	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getMywmParams()
	{
		return wmParams;
	}

	@Override
	public void onCreate()
	{

		setInstance(this);
		CrashHandler handler = CrashHandler.getInstance();
		handler.init(getApplicationContext());

		Thread.setDefaultUncaughtExceptionHandler(handler);
		Log.d("", "---CrashHandler init ok---");
		super.onCreate();
	}

	public static void setInstance(AllActivityListApplication i)
	{
		instance = i;
	}

	public static AllActivityListApplication getInstance()
	{
		return instance;
	}

	public void addActivity(Activity activity)
	{
		if (list == null)
		{
			list = new ArrayList<Activity>();
		}
		list.add(activity);
	}

	public void finishAll()
	{
		for (Activity activity : list)
		{
			if (!activity.isFinishing())
			{
				activity.finish();
			}
		}
		list = null;
	}

	/**
	 * 获取集合大小
	 * 
	 * @author yin.liu
	 * @return
	 */
	public int getListSize()
	{
		if (list == null)
		{
			return 0;
		}
		return list.size();
	}

	/**
	 * 移除指定activity
	 * 
	 * @author yin.liu
	 * @param activity
	 */
	public void removeActivity(Activity activity)
	{
		if (list != null)
		{
			list.remove(activity);
		}
	}

	/**
	 * 判断是否有此activity
	 * 
	 * @param activity
	 * @author yin.liu
	 * @return
	 */
	public boolean isHasActivity(Activity activity)
	{
		if (list != null)
		{
			return list.contains(activity);
		}
		return false;
	}

	/**
	 * 结束指定的同类activity
	 * 
	 * @param activity
	 * @author yin.liu
	 */
	public void finishActivityByName(Activity activity)
	{
		String className = activity.getClass().getName();
		List<Activity> removeAcList = new ArrayList<Activity>();
		if (list != null)
		{
			for (Activity ac : list)
			{
				if (className.equals(ac.getClass().getName()))
				{
					ac.finish();
					removeAcList.add(ac);
				}
			}
			list.removeAll(removeAcList);
		}

	}

	/**
	 * 利用intent的flag位的特点，关闭当前activitytask中的的所有activity实例，如果出现内存溢出
	 * 请使用该方法
	 * 
	 * @param context
	 */
	public void exit(Context context)
	{

		Intent mIntent = new Intent();
		mIntent.setClass(context, MainActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mIntent.putExtra("finish", true);
		context.startActivity(mIntent);

	}

	/**
	 * 获得当前应用的版本号
	 * 
	 * @param context
	 * @return
	 */
	public String getVersionCode(Context context)
	{
		try
		{
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			;
			return null;
		}

	}

}
