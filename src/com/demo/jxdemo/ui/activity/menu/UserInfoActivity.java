package com.demo.jxdemo.ui.activity.menu;

import ui.listener.OnClickAvoidForceListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;
import com.demo.jxdemo.ui.activity.login.LoginActivity;
import com.demo.jxdemo.utils.ToastManager;

public class UserInfoActivity extends BaseSlidingActivity
{
	private LinearLayout backLayout;

	private Button saveButton;

	private RelativeLayout cleanLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_userinfo);
		loadMenu();

		findViews();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_self));
		backLayout = (LinearLayout) findViewById(R.id.returnBtn);
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
		backLayout.setVisibility(View.VISIBLE);

		saveButton = (Button) findViewById(R.id.remarkBtn);
		saveButton.setText(getResources().getString(R.string.left_self_save));
		saveButton.setVisibility(View.VISIBLE);

		cleanLayout = (RelativeLayout) findViewById(R.id.rlayout_cleancache);
	}

	private void setViewClick()
	{
		backLayout.setOnClickListener(onClickAvoidForceListener);
		cleanLayout.setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClickAvoidForceListener);
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
				case R.id.rlayout_cleancache:
					ToastManager.getInstance(UserInfoActivity.this).showToast("清除........");
					break;
				case R.id.layout_remark:
					ToastManager.getInstance(UserInfoActivity.this).showToast("保存.......");
					break;
				default:
					break;
			}
		}
	};
}
