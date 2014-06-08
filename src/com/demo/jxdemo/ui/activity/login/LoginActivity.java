package com.demo.jxdemo.ui.activity.login;

import java.util.HashMap;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.base.support.BaseConstants;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.services.HttpPostAsync;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.utils.ToastManager;

public class LoginActivity extends BaseActivity
{
	private Button aboutButton;

	private EditText numEditText;

	private EditText checkEditText;

	private Button checkButton;

	private Map<String, String> configMap;

	/** 验证码 */
	private String checkCode = "";

	private TimeCount time;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		configMap = SharedPreferencesConfig.config(this);
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		findViews();
		initView();
		setViewClick();
	}

	private void findViews()
	{

		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.app_name));
		((ImageView) findViewById(R.id.img_return)).setVisibility(View.GONE);
		aboutButton = (Button) findViewById(R.id.remarkBtn);
		aboutButton.setText(getResources().getString(R.string.about));
		aboutButton.setVisibility(View.VISIBLE);

		numEditText = (EditText) findViewById(R.id.edit_login_phonenum);
		checkEditText = (EditText) findViewById(R.id.edit_login_pwd);
		checkButton = (Button) findViewById(R.id.btn_getcheck);

	}

	private void initView()
	{
		if (!StringUtil.isBlank(SharedPreferencesConfig.config(LoginActivity.this).get(Constant.USER_TEL)))
		{
			numEditText.setText(SharedPreferencesConfig.config(LoginActivity.this).get(Constant.USER_TEL));
		}
	}

	private void setViewClick()
	{
		((RelativeLayout) findViewById(R.id.rlayout_into)).setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClickAvoidForceListener);
		checkButton.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.rlayout_into:
					// SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TEL, numEditText.getText().toString());
					// Intent intent = new Intent();
					// intent.setClass(LoginActivity.this, MainActivity.class);
					// startActivity(intent);
					// LoginActivity.this.finish();
					if (isPassCheck(1))
					{
						showProgress(2 * 60 * 1000);
						login();
					}
					break;
				case R.id.btn_getcheck:
					if (isPassCheck(0))
					{
						showProgress();
						getCheckCode();
						time.start();
					}
					break;
				case R.id.layout_remark:
					// Intent inAbout = new Intent();
					// inAbout.setClass(LoginActivity.this, AboutActivity.class);
					// inAbout.putExtra("fromLogin", "fromLogin");
					// startActivity(inAbout);
					ToastManager.getInstance(LoginActivity.this).showToast("关于.......");
					break;
				default:
					break;
			}

		}
	};

	/**
	 * 验证条件是否符合
	 * 
	 * @param flag
	 *            0:验证码 1:登录
	 * @return
	 */
	private boolean isPassCheck(int flag)
	{
		String num = numEditText.getText().toString();
		String pwd = checkEditText.getText().toString();
		if (StringUtil.isBlank(num))
		{
			// numEditText.setError(getResources().getString(R.string.register_phonenum_hint));
			// numEditText.requestFocus();
			ToastManager.getInstance(LoginActivity.this).showToast(getResources().getString(R.string.register_phonenum_hint));
			return false;
		}
		else if (num.length() < 11 || !num.substring(0, 1).equals("1"))
		{
			// numEditText.setError(getResources().getString(R.string.register_phonenum_err));
			// numEditText.requestFocus();
			ToastManager.getInstance(LoginActivity.this).showToast(getResources().getString(R.string.register_phonenum_err));
			return false;
		}
		if (flag != 0)
		{
			if (StringUtil.isBlank(pwd) || pwd.length() < 4)
			{
				// checkEditText.setError("请输入" + getResources().getString(R.string.register_messcheck_hint));
				// checkEditText.requestFocus();
				ToastManager.getInstance(LoginActivity.this).showToast("请输入" + getResources().getString(R.string.register_messcheck_hint));
				return false;
			}
		}
		return true;
	}

	/**
	 * 获得验证码
	 */
	private void getCheckCode()
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("PhoneNumber", numEditText.getText().toString().trim());

		new HttpPostAsync(LoginActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("SendAuthCode=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(LoginActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (Constant.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(LoginActivity.this).showToast("连接不上服务器");
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					// Map<String, Object> headMap = JsonUtil.getMapString(mapstr.get("head").toString());
					// Map<String, Object> dataMap = JsonUtil.getMapString(mapstr.get("data").toString());
					boolean isSuccess = false;
					if (!mapstr.containsKey(CommandConstants.ERRCODE))
						isSuccess = true;
					String desc = (String) mapstr.get(CommandConstants.ERRCODE);
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						dismissProgress();
						ToastManager.getInstance(LoginActivity.this).showToast(desc);
					}
					else
					{
						checkCodeSuccessDeal(mapstr);// 成功后处理
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.SENDAUTHCODE, parasTemp);

	}

	@SuppressWarnings("unchecked")
	private void checkCodeSuccessDeal(Map<String, Object> map)
	{
		new CheckCodeDealTask().execute(map);
	}

	class CheckCodeDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
	{

		@Override
		protected void onPreExecute()
		{
			showProgress(1 * 60 * 1000);
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{

		}

		@Override
		protected Void doInBackground(Map<String, Object>... map)
		{
			try
			{
				if (map != null)
				{
					checkCode = "1234";// StringUtil.Object2String(map[0].get(""));
					ToastManager.getInstance(LoginActivity.this).showToast("验证码:" + checkCode);
				}
				else
					ToastManager.getInstance(LoginActivity.this).showToast("获取验证码失败!");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				ToastManager.getInstance(LoginActivity.this).showToast("获取验证码失败!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			dismissProgress();
		}

	}

	private void login()
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("PhoneNumber", numEditText.getText().toString().trim());
		parasTemp.put("AuthCode", checkEditText.getText().toString().trim());

		new HttpPostAsync(LoginActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("SignIn=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(LoginActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (Constant.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(LoginActivity.this).showToast("连接不上服务器");
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					// Map<String, Object> headMap = JsonUtil.getMapString(mapstr.get("head").toString());
					// Map<String, Object> dataMap = JsonUtil.getMapString(mapstr.get("data").toString());
					boolean isSuccess = false;
					if (!mapstr.containsKey(CommandConstants.ERRCODE))
						isSuccess = true;
					String desc = (String) mapstr.get(CommandConstants.ERRCODE);
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						dismissProgress();
						ToastManager.getInstance(LoginActivity.this).showToast(desc);
					}
					else
					{
						loginSuccessDeal(mapstr);// 成功后处理
					}
				}
				return "";
			}
		}.execute(Constant.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.LOGIN, parasTemp);

	}

	@SuppressWarnings("unchecked")
	private void loginSuccessDeal(Map<String, Object> map)
	{
		new LoginDealTask().execute(map);
	}

	class LoginDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
	{

		@Override
		protected void onPreExecute()
		{
			showProgress(2 * 60 * 1000);
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{

		}

		@Override
		protected Void doInBackground(Map<String, Object>... map)
		{
			try
			{
				// 2，保存登录用户的基本信息到手机文件存储库中
				if (map != null)
				{
					SharedPreferencesConfig
							.saveConfig(LoginActivity.this, Constant.USER_ID, StringUtil.Object2String(map[0].get("UserID")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TOKEN,
							StringUtil.Object2String(map[0].get("UserToken")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TEL, numEditText.getText().toString());
				}
				else
					ToastManager.getInstance(LoginActivity.this).showToast("登录失败!");

				dismissProgress();
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				ToastManager.getInstance(LoginActivity.this).showToast("登录失败!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			dismissProgress();
		}

	}

	class TimeCount extends CountDownTimer
	{
		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish()
		{// 计时完毕时触发
			checkButton.setText(getResources().getString(R.string.register_get_messcheck));
			checkButton.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished)
		{// 计时过程显示
			checkButton.setClickable(false);
			checkButton.setText(millisUntilFinished / 1000 + "秒");
		}

	}
}
