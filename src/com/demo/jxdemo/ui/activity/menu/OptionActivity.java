package com.demo.jxdemo.ui.activity.menu;

import java.util.HashMap;
import java.util.Map;
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

import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseFragmentActivity;
import com.demo.jxdemo.ui.customviews.SlidingView;
import com.demo.jxdemo.utils.ToastManager;

public class OptionActivity extends BaseFragmentActivity
{
	private Button submitButton;

	private EditText optionEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_option);

		findViews();
		initSlidingMenu();
		initView();
		setViewClick();
	}

	protected void initSlidingMenu()
	{
		side_drawer = new SlidingView(this).initSlidingMenu();
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
		submitButton.setOnClickListener(onclicks);
	}

	private OnClickAvoidForceListener onclicks = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.btn_option:
					showProgress(5 * 60 * 1000);
					request();
					break;
				default:
					break;
			}
		}
	};

	private void request()
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(OptionActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("Feedback", optionEditText.getText().toString());

		new HttpPostAsync(OptionActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("SendFeedback=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(OptionActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(OptionActivity.this).showToast("连接不上服务器");
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					boolean isSuccess = false;
					if (!mapstr.containsKey(CommandConstants.ERRCODE))
						isSuccess = true;
					String desc = (String) mapstr.get(CommandConstants.ERRCODE);
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						dismissProgress();
						ToastManager.getInstance(OptionActivity.this).showToast(desc);
					}
					else
					{
						dismissProgress();
						// 成功后处理
						ToastManager.getInstance(OptionActivity.this).showToast("您的反馈意见已经成功发送，谢谢！");
						optionEditText.setText("");
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.SENDFEEDBACK, parasTemp);
	}

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
