package com.demo.jxdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

import com.demo.base.global.ActivityTaskManager;
import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.customviews.slide.SlidingActivity;
import com.demo.jxdemo.ui.customviews.slide.SlidingMenu;
import com.demo.jxdemo.ui.fragment.main.LeftFragment;

public abstract class BaseSlidingActivity extends SlidingActivity
{

	private LeftFragment leftFragment;

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

		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidth(50);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(250);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();

		return super.onKeyDown(keyCode, event);
	}

}
