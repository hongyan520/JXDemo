package com.demo.jxdemo.application;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.demo.jxdemo.utils.NotificationUtils;

/**
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。
 * 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框
 * 实现该接口并注册为程序中的默认未捕获异常处理
 * 这样当未捕获异常发生时，就可以做些异常处理操作
 * 例如：收集异常信息，发送错误报告 等。
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告
 */
public class CrashHandler implements UncaughtExceptionHandler
{
	/** Debug Log Tag */
	public static final String TAG = "CrashHandler";

	/** 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能 */
	public static final boolean DEBUG = true;

	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;

	/** 程序的Context对象 */
	private Context mContext;

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	// private Map<String, String> config;

	private String message = "程序异常,请联系管理员!";

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler()
	{
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx)
	{
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// config = SharedPreferencesConfig.config(ctx);
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}
		else
		{
			// Sleep一会后结束程序
			// 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				Log.e(TAG, "Error : ", e);
			}

			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);

		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex)
	{
		if (ex == null)
		{
			NotificationUtils.cancelAllNotification(mContext);
			return true;
		}
		// 使用Toast来显示异常信息
		new Thread()
		{
			@Override
			public void run()
			{
				// Toast 显示需要出现在一个线程的消息队列中
				Looper.prepare();
				Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		// 保存错误报告文件
		saveCrashInfoToFile(ex);

		return true;
	}

	/**
	 * 保存错误信息到PC数据库中
	 * 
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex)
	{
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		// 将此 throwable 及其追踪输出到指定的 PrintWriter
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null)
		{
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		// 打印错误
		Log.e(TAG, info.toString());

		// TODO 执行将错误报告发送到后台数据库的操作

		return null;
	}

	/**
	 * 获取手机的硬件信息
	 * 
	 * @return
	 */
	private String getMobileInfo()
	{
		StringBuffer sb = new StringBuffer();
		// 通过反射获取系统的硬件信息
		try
		{
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields)
			{
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append("\n");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取手机的版本信息
	 * 
	 * @return
	 */
	private String getVersionInfo()
	{
		try
		{
			PackageManager pm = mContext.getPackageManager();
			PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
			return info.versionName;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "版本号未知";
		}
	}
}
