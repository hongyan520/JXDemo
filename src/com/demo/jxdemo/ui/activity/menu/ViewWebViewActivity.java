package com.demo.jxdemo.ui.activity.menu;

import ui.listener.OnClickAvoidForceListener;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseActivity;

public class ViewWebViewActivity extends BaseActivity
{
	private WebView webview;

	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_webview);

		url = getIntent().getStringExtra("url");

		findViews();
		setViewClick();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText("浏览网页");
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);

		webview = (WebView) findViewById(R.id.webView1);
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webview.getSettings().setLoadWithOverviewMode(true);
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webview.loadUrl(url);
		// 设置Web视图
		webview.setWebViewClient(new HelloWebViewClient());

	}

	private class HelloWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.layout_return:
					finish();
					break;
				default:
					break;
			}
		}
	};

	// @Override
	// // 设置回退
	// // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	// public boolean onKeyDown(int keyCode, KeyEvent event)
	// {
	// if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack())
	// {
	// webview.goBack(); // goBack()表示返回WebView的上一页面
	// return true;
	// }
	// return false;
	// }
}
