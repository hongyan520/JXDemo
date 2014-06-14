package com.demo.jxdemo.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseActivity;

public class SplashActivity extends BaseActivity
{

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
		}, 1000);// TODO 记住改为4秒
	}

}
