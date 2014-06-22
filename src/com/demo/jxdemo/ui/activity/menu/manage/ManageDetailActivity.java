package com.demo.jxdemo.ui.activity.menu.manage;

import java.util.HashMap;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.utils.ToastManager;

public class ManageDetailActivity extends BaseActivity
{
	private int id;

	private String yaoQiu;

	private String zhouQi;

	private String muBiao;

	private int acceptMaterial = 0;

	private int acceptTraining = 0;

	private ImageView materialImageView;

	private ImageView trainingImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_manage_detail);

		id = getIntent().getIntExtra("ID", id);
		yaoQiu = getIntent().getStringExtra("yaoQiu");
		zhouQi = getIntent().getStringExtra("zhouQi");
		muBiao = getIntent().getStringExtra("muBiao");
		acceptMaterial = getIntent().getIntExtra("AcceptMaterial", 0);
		acceptTraining = getIntent().getIntExtra("AcceptTraining", 0);

		findViews();
		initView();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText("英语口语");
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.img_back);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);

		materialImageView = (ImageView) findViewById(R.id.img_xl);
		trainingImageView = (ImageView) findViewById(R.id.img_dy);
	}

	private void initView()
	{
		((TextView) findViewById(R.id.text_manage_yq)).setText(yaoQiu);
		((TextView) findViewById(R.id.text_manage_zq)).setText(zhouQi);
		((TextView) findViewById(R.id.text_manage_mb)).setText(muBiao);

		controlImg();

		((ScrollView) findViewById(R.id.scroll_manage)).setVisibility(View.VISIBLE);
	}

	private void controlImg()
	{
		if (acceptMaterial == 0)
			materialImageView.setBackgroundResource(R.drawable.icon_ok_gray);
		else
			materialImageView.setBackgroundResource(R.drawable.icon_ok);
		if (acceptTraining == 0)
			trainingImageView.setBackgroundResource(R.drawable.icon_ok_gray);
		else
			trainingImageView.setBackgroundResource(R.drawable.icon_ok);
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		((RelativeLayout) findViewById(R.id.rlayout_kcxl)).setOnClickListener(onClickAvoidForceListener);
		((RelativeLayout) findViewById(R.id.rlayout_dyzl)).setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.layout_return:
					finishMyActivity();
					break;
				case R.id.rlayout_kcxl:
					// request(CommandConstants.LEARNCOURSE);
					break;
				case R.id.rlayout_dyzl:
					// request(CommandConstants.ENGAGECOURSE);
					break;
				default:
					break;
			}
		}
	};

	private void request(final String cmd)
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(ManageDetailActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("CourseID", id);

		new HttpPostAsync(ManageDetailActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println(cmd + "=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(ManageDetailActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(ManageDetailActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(ManageDetailActivity.this).showToast(desc);
					}
					else
					{
						// 成功后处理
						if (CommandConstants.LEARNCOURSE.equals(cmd))
						{
							acceptMaterial = (Integer) mapstr.get("Status");
							mHandler.sendEmptyMessage(1);
						}
						else if (CommandConstants.ENGAGECOURSE.equals(cmd))
						{
							acceptTraining = (Integer) mapstr.get("Status");
							mHandler.sendEmptyMessage(2);
						}
						dismissProgress();
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + cmd, parasTemp);
	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
				case 2:
					controlImg();
					break;
				default:
					break;
			}
		}
	};
}
