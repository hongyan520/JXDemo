package com.demo.jxdemo.ui.activity;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.demo.base.global.ActivityTaskManager;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.customviews.CustomProgressDialog;
import com.demo.jxdemo.ui.customviews.slide.SlidingMenu;

public class BaseFragmentActivity extends FragmentActivity
{
	protected SlidingMenu side_drawer;

	/**
	 * 加载等待圈
	 */
	private CustomProgressDialog myProgressDialog;

	/**
	 * 显示等待框
	 */
	private static final int SHOW_PROGRESS_DIALOG = 10;

	/**
	 * 关闭等待框
	 */
	private static final int DISMISS_PROGRESS = 11;


	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActivityTaskManager.getInstance().putActivity(this.getClass().getName(), this);
	}

	public OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.layout_return:
					closeKeyboard();
					if (side_drawer.isMenuShowing())
					{
						side_drawer.showContent();
					}
					else
					{
						side_drawer.showMenu();
					}

					break;
				// case R.id.layout_remark:
				//
				// if (side_drawer.isSecondaryMenuShowing())
				// {
				// side_drawer.showContent();
				// }
				// else
				// {
				// side_drawer.showSecondaryMenu();
				// }
				// break;
				default:
					break;
			}

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{// 屏幕返回键处理
			SharedPreferencesConfig.saveConfig(getApplicationContext(), Constant.CURRENT_LEFTMENU, 0 + "");
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
		return false;
	}

	public void closeKeyboard()
	{
		if (getCurrentFocus() != null)
		{
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
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
							myProgressDialog = new CustomProgressDialog(BaseFragmentActivity.this, R.style.CustomProgressDialog);
							myProgressDialog.createDialog(BaseFragmentActivity.this);
							myProgressDialog.gettView().setText(getResources().getString(R.string.dealing));
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

}
