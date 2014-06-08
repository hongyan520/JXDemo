package com.demo.jxdemo.ui.activity.login;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.customviews.MainPagerAdapter;
import com.demo.jxdemo.ui.customviews.MyViewPager;
import com.demo.jxdemo.R;

public class SplashActivity extends BaseActivity
{

	private MyViewPager viewPager;

	private MainPagerAdapter pagerAdapter;

	private ArrayList<View> pagerContents = new ArrayList<View>();

	private String configUserName;

	private Map<String, String> mapconfig;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash);

		// mapconfig = SharedPreferencesConfig.config(SplashActivity.this);
		// configUserName = mapconfig.get(Constant.LOOK_INTRDOUCE);
		//
		// // 版本号与之前存储的不一致
		// if (!getVERSION_NAME().equals(configUserName))
		// {
		// mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000);
		// return;
		// }

		// 闪屏进入登录界面
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}, 1000);//TODO 记住改为4秒
	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			init();
		}
	};

	private void init()
	{

		findViewById(R.id.spalsh_img).setVisibility(View.GONE);
		viewPager = (MyViewPager) findViewById(R.id.vPage_introduce);
		viewPager.setHorizontalScrollBarEnabled(false);
		LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(LP_FW);
		linearLayout.setBackgroundResource(R.drawable.jieshao1);

		LinearLayout linearLayout2 = new LinearLayout(this);
		linearLayout2.setLayoutParams(LP_FW);
		linearLayout2.setBackgroundResource(R.drawable.jieshao2);

		LayoutInflater inflater = LayoutInflater.from(this);
		View linearLayout3 = inflater.inflate(R.layout.activity_splash_third_intrduce, null);

		linearLayout3.findViewById(R.id.enter_btn).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				SharedPreferencesConfig.saveConfig(SplashActivity.this, Constant.LOOK_INTRDOUCE, getVERSION_NAME());
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		});

		pagerContents.add(linearLayout);
		pagerContents.add(linearLayout2);
		pagerContents.add(linearLayout3);

		pagerAdapter = new MainPagerAdapter(this, pagerContents, 3, viewPager);
		pagerAdapter.setTypeFlag(1);
		viewPager.setOnPageChangeListener(pagerAdapter);

		viewPager.setAdapter(pagerAdapter);
		pagerAdapter.notifyDataSetChanged();

	}

}
