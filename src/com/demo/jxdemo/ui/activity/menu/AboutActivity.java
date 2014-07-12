package com.demo.jxdemo.ui.activity.menu;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseFragmentActivity;
import com.demo.jxdemo.ui.customviews.SlidingView;

public class AboutActivity extends BaseFragmentActivity
{
	private String fromLoginString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_about);
		// loadMenu();

		fromLoginString = StringUtil.Object2String(getIntent().getStringExtra("fromLogin"));
		findViews();
		setViewClick();
	}

	protected void initSlidingMenu()
	{
		side_drawer = new SlidingView(this).initSlidingMenu();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_about));
		if (StringUtil.isBlank(fromLoginString))
		{
			((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
			initSlidingMenu();
		}
		else
		{
			((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.img_back);
			// setBehindContentView(R.layout.slidemenu_back_content); // 后面的布局(机构)
		}
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);

	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		((RelativeLayout) findViewById(R.id.rlayout_about_url)).setOnClickListener(onClickAvoidForceListener);
		((RelativeLayout) findViewById(R.id.rlayout_about_company)).setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.layout_return:
					if (StringUtil.isBlank(fromLoginString))
					{
						closeKeyboard();
						if (side_drawer.isMenuShowing())
						{
							side_drawer.showContent();
						}
						else
						{
							side_drawer.showMenu();
						}
					}
					else
						finish();
					break;
				case R.id.rlayout_about_url:
					Intent i = new Intent();
					i.setClass(AboutActivity.this, ViewWebViewActivity.class);
					i.putExtra("url", getResources().getString(R.string.left_about_url1));
					startActivity(i);
					break;
				case R.id.rlayout_about_company:
					Intent ii = new Intent();
					ii.setClass(AboutActivity.this, ViewWebViewActivity.class);
					ii.putExtra("url", getResources().getString(R.string.left_about_company2));
					startActivity(ii);
					break;
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
			if (!StringUtil.isBlank(fromLoginString))
			{
				finish();
			}
			else
				return super.onKeyDown(keyCode, event);
		}
		return false;
	}
}
