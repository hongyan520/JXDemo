package com.demo.jxdemo.ui.activity.menu;

import ui.listener.OnClickAvoidForceListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;

public class AboutActivity extends BaseSlidingActivity
{
	private LinearLayout backLayout;

	private TextView urlText;

	private TextView companyText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_about);
		loadMenu();

		findViews();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_about));
		backLayout = (LinearLayout) findViewById(R.id.returnBtn);
		if (StringUtil.isBlank(getIntent().getStringExtra("fromLogin")))
		{
			((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
			backLayout.setVisibility(View.VISIBLE);
		}

		urlText = (TextView) findViewById(R.id.text_about_url);
		companyText = (TextView) findViewById(R.id.text_about_company);

		// SpannableUtil.spannable(getResources().getString(R.string.left_about_url1), getResources().getString(R.string.left_about_url1),
		// urlText);
		// SpannableUtil.spannable(getResources().getString(R.string.left_about_company1), "http://shuzhiying.com", companyText);

		// SpannableString sp = new SpannableString(getResources().getString(R.string.left_about_url1));
		// // 设置超链接
		// sp.setSpan(new URLSpan(getResources().getString(R.string.left_about_url1)), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// // SpannableString对象设置给TextView
		// urlText.setText(sp);
		// // 设置TextView可点击
		// urlText.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private void setViewClick()
	{
		backLayout.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.returnBtn:
					getSlidingMenu().toggle();
					break;
				default:
					break;
			}
		}
	};
}
