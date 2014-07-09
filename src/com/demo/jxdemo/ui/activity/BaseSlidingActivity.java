package com.demo.jxdemo.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.demo.base.global.ActivityTaskManager;
import com.demo.base.util.Utils;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.customviews.CustomProgressDialog;
import com.demo.jxdemo.ui.customviews.slide.SlidingActivity;
import com.demo.jxdemo.ui.customviews.slide.SlidingMenu;
import com.demo.jxdemo.ui.customviews.slide.SlidingMenu.OnOpenListener;
import com.demo.jxdemo.ui.fragment.main.LeftFragment;

public abstract class BaseSlidingActivity extends SlidingActivity
{

	private LeftFragment leftFragment;

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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActivityTaskManager.getInstance().putActivity(this.getClass().getName(), this);
	}

	public void loadMenu()
	{
		setBehindContentView(R.layout.slidemenu_back_content); // 后面的布局(机构)
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		leftFragment = new LeftFragment();
		fragmentTransaction.replace(R.id.content, leftFragment);// 实例化，但是被actvity_main布局覆盖。
		fragmentTransaction.commit();

		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.sidepanebackground);

		DisplayMetrics dm = Utils.getDisplayMetrics(this);

		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		int screenHight = dm.heightPixels;

		int offset = screenWidth - bitmapOrg.getWidth();
		float currentWidth = 0;
		//if (screenHight < bitmapOrg.getHeight())
		//{
			currentWidth = ((float) bitmapOrg.getWidth()) / (((float) bitmapOrg.getHeight()) / ((float) screenHight));
			offset = (int) (screenWidth - currentWidth);
		//}

		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidth(50);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		sm.setOnOpenListener(new OnOpenListener() {
			
			@Override
			public void onOpen() {
				// TODO Auto-generated method stub
				System.out.println("open...");
				closeKeyboard();
			}
		});
	}
	

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
							myProgressDialog = new CustomProgressDialog(BaseSlidingActivity.this,R.style.CustomProgressDialog);
							myProgressDialog.createDialog(BaseSlidingActivity.this);
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
