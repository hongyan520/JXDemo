package com.demo.jxdemo.ui.activity.menu;

import java.util.Timer;
import java.util.TimerTask;

import ui.listener.OnClickAvoidForceListener;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;
import com.demo.jxdemo.utils.ToastManager;

public class OptionActivity extends BaseSlidingActivity
{
	private Button submitButton;

	private EditText optionEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_option);
		loadMenu();

		findViews();
		initView();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_option));
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);

		optionEditText = (EditText) findViewById(R.id.edit_option);
		optionEditText.setFocusableInTouchMode(true);
		optionEditText.requestFocus();
		submitButton = (Button) findViewById(R.id.btn_option);

		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				InputMethodManager inputManager = (InputMethodManager) optionEditText.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(optionEditText, 0);
			}
		}, 555);
	}

	private void initView()
	{
		optionEditText.setText(SharedPreferencesConfig.config(OptionActivity.this).get(Constant.OPTION));
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		submitButton.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.layout_return:
					closeKeyboard();
					getSlidingMenu().toggle();
					break;
				case R.id.btn_option:
					ToastManager.getInstance(OptionActivity.this).showToast("提交........");
					break;
				default:
					break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		SharedPreferencesConfig.saveConfig(OptionActivity.this, Constant.OPTION, optionEditText.getText().toString());
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop()
	{
		SharedPreferencesConfig.saveConfig(OptionActivity.this, Constant.OPTION, optionEditText.getText().toString());
		super.onStop();
	}

}
