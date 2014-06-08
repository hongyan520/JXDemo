package com.demo.jxdemo.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;

import com.demo.base.global.ActivityTaskManager;
import com.demo.jxdemo.R;

public abstract class BaseActivity extends Activity
{
	/**
	 * 上下文
	 */
	public Context mContext;

	private boolean isBaseOnCreate = true;

	/**
	 * 加载等待圈
	 */
	private ProgressDialog myProgressDialog;

	/**
	 * 显示等待框
	 */
	private static final int SHOW_PROGRESS_DIALOG = 10;

	/**
	 * 关闭等待框
	 */
	private static final int DISMISS_PROGRESS = 11;

	private  String VERSION_NAME = "1.0";

	private Object object;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 去掉顶上的标题框
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;

		// 添加活动Activity
		ActivityTaskManager.getInstance().putActivity(this.getClass().getName(), this);

		PackageManager pm = getPackageManager();
		PackageInfo pi = null;
		try
		{
			pi = pm.getPackageInfo(getPackageName(), 0);
		}
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VERSION_NAME = pi.versionName;

		// onCreateFindView(savedInstanceState);
		// onCreateAddListener(savedInstanceState);
	}

	// /**
	// * 初始化界面 （加载控件View对象）
	// *
	// * @param savedInstanceState
	// */
	// protected abstract void onCreateFindView(Bundle savedInstanceState);
	//
	// /**
	// * 加载操作事件（View控件的点击事件响应）
	// *
	// * @param savedInstanceState
	// */
	// protected abstract void onCreateAddListener(Bundle savedInstanceState);

	@Override
	protected void onResume()
	{
		super.onResume();
		// onResume更新当前活动的Activity对象

		if (isBaseOnCreate)
		{
			isBaseOnCreate = false;
		}
		else
		{
			dismissProgress();
			onBaseResume();
		}

	}

	/**
	 * onCreate进入时不调用 用于onResume()处理
	 */
	protected void onBaseResume()
	{

	}

	private Handler progressHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			// 切换横竖屏会异常
			try
			{
				if (msg != null)
				{
					int what = msg.what;

					if (what == SHOW_PROGRESS_DIALOG)
					{
						if (myProgressDialog == null)
						{
							myProgressDialog = new ProgressDialog(BaseActivity.this);
							myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							myProgressDialog.setMessage(getResources().getString(R.string.dealing));
							myProgressDialog.setCancelable(true);
							myProgressDialog.setCanceledOnTouchOutside(false);
						}

						if (!myProgressDialog.isShowing())
						{
							myProgressDialog.show();
						}
					}
					else if (what == DISMISS_PROGRESS && null != myProgressDialog && myProgressDialog.isShowing())
					{
						myProgressDialog.dismiss();
					}

				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	};

	/**
	 * 等待圈显示
	 */
	public void showProgress()
	{
		progressHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
		// 10秒后自动关闭等待框
		progressHandler.sendEmptyMessageDelayed(DISMISS_PROGRESS, 10 * 1000);
	}

	/**
	 * 等待圈显示
	 * 
	 * @param waittime
	 *            等待时间
	 */
	public void showProgress(long waittime)
	{
		progressHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
		// waittime后自动关闭等待框
		progressHandler.sendEmptyMessageDelayed(DISMISS_PROGRESS, waittime);
	}

	/**
	 * 等待圈消失
	 */
	public void dismissProgress()
	{
		progressHandler.sendEmptyMessage(DISMISS_PROGRESS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{// 屏幕返回键处理
			finishMyActivity();
		}
		return false;
	}

	/**
	 * 关闭Activity
	 * 
	 * @param
	 * @return void
	 */
	public void finishMyActivity()
	{
		finish();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		dismissProgress();
		finishMyActivity();
	}

	public String getVERSION_NAME()
	{
		return VERSION_NAME;
	}

}
