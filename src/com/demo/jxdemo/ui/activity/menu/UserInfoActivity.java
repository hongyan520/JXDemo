package com.demo.jxdemo.ui.activity.menu;

import ui.listener.OnClickAvoidForceListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;
import com.demo.jxdemo.ui.activity.login.LoginActivity;
import com.demo.jxdemo.utils.ToastManager;

public class UserInfoActivity extends BaseSlidingActivity
{
	private RelativeLayout cleanLayout;

	/** 名字 */
	private EditText userNameEditText;

	/** 手机号 */
	/** 性别 */
	private TextView userGenderTextView;

	/** 地区 */
	/** 职业 */
	/** 个人介绍 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_userinfo);
		loadMenu();

		findViews();
		initViews();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_self));
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.text_right)).setText(getResources().getString(R.string.left_self_save));
		((TextView) findViewById(R.id.text_right)).setVisibility(View.VISIBLE);

		userGenderTextView = (TextView) findViewById(R.id.text_gender);
		cleanLayout = (RelativeLayout) findViewById(R.id.rlayout_cleancache);
	}

	private void initViews()
	{
		if (!StringUtil.isBlank(SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_GENDER)))
		{
			String gender = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_GENDER);
			if ("0".equals(gender))
				userGenderTextView.setText("未设置");
			else if ("1".equals(gender))
				userGenderTextView.setText("男");
			else if ("2".equals(gender))
				userGenderTextView.setText("女");
		}

		((ScrollView) findViewById(R.id.scrollView1)).setVisibility(View.VISIBLE);
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
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
				case R.id.layout_return:
					closeKeyboard();
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
