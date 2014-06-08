package com.demo.jxdemo.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Title:ToastManager.class
 * Description: 弹出信息框
 * Copyright:Copyright (c) 2012
 * Company:湖南科创
 * 
 * @author wanru.chen
 * @version 1.0
 * @date 2012-7-17
 */
public class ToastManager
{

	private Toast toast;

	public static ToastManager instance;

	private Context context;

	public static ToastManager getInstance(Context con)
	{

		if (instance == null)
		{
			instance = new ToastManager(con);
		}
		return instance;
	}

	private ToastManager(Context context)
	{
		this.context = context;
	}

	public void showToast(String msg, int time)
	{

		if (Looper.myLooper() != Looper.getMainLooper())
		{
			return;
		}

		if (toast == null)
		{
			toast = Toast.makeText(context, msg, time);
		}
		else
		{

			toast.setText(msg);
			toast.setDuration(time);

		}
		toast.show();
	}

	public void showToast(String msg)
	{

		if (Looper.myLooper() != Looper.getMainLooper())
		{
			return;
		}

		if (toast == null)
		{
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}
		else
		{

			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);

		}
		toast.show();
	}
}
