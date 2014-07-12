package com.demo.jxdemo.ui.activity.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.ScrollListViewUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.activity.menu.manage.ManageDetailActivity;
import com.demo.jxdemo.ui.adapter.UserDetailListAdapter;
import com.demo.jxdemo.utils.ToastManager;

public class UserDetailActivity extends BaseActivity
{
	private ListView listView;

	private UserDetailListAdapter adapter;

	private List<Map<String, Object>> lists;

	private TextView nameTextView;

	private TextView locationTextView;

	private TextView introduceTextView;

	private ImageView genderImageView;

	private ImageView userImageView;

	private Map<String, Object> maps;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_userdetail);

		adapter = new UserDetailListAdapter(UserDetailActivity.this);
		findViews();
		// initData();// 记得去掉
		initView(false);
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_index_userdetail));
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.img_back);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);

		nameTextView = (TextView) findViewById(R.id.text_name);
		locationTextView = (TextView) findViewById(R.id.text_location);
		introduceTextView = (TextView) findViewById(R.id.text_introduce);
		genderImageView = (ImageView) findViewById(R.id.img_gender);
		userImageView = (ImageView) findViewById(R.id.img_user);

		listView = (ListView) findViewById(R.id.list_userdetail);
	}

	private void initData()
	{
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("11", "111");
		Map<String, Object> m2 = new HashMap<String, Object>();
		m2.put("11", "111");
		Map<String, Object> m3 = new HashMap<String, Object>();
		m3.put("11", "111");

		lists = new ArrayList<Map<String, Object>>();
		lists.add(m1);
		lists.add(m2);
		lists.add(m3);
	}

	private void initView(boolean isRequest)
	{
		String name = SharedPreferencesConfig.config(UserDetailActivity.this).get(Constant.USER_NAME);
		String location = SharedPreferencesConfig.config(UserDetailActivity.this).get(Constant.USER_LOCATION);
		String introduce = SharedPreferencesConfig.config(UserDetailActivity.this).get(Constant.USER_INTRODUCTION);
		String gender = SharedPreferencesConfig.config(UserDetailActivity.this).get(Constant.USER_GENDER);
		String userIcon = SharedPreferencesConfig.config(UserDetailActivity.this).get(Constant.USER_AVATAR);

		// ////打开下面两块
		if ("1".equals(gender))
			genderImageView.setBackgroundResource(R.drawable.icon_male);
		else if ("2".equals(gender))
			genderImageView.setBackgroundResource(R.drawable.icon_female);
		else
			genderImageView.setVisibility(View.INVISIBLE);

		nameTextView.setText(name);
		locationTextView.setText(location);
		introduceTextView.setText(introduce);
		if (!isRequest)
			request(CommandConstants.USERPROFILE);
		initListView(false);
	}

	private void initListView(boolean isRequest)
	{
		// lists = JsonUtil.getList(SharedPreferencesConfig.config(UserDetailActivity.this).get(Constant.USER_COURSEARRAY).toString());
		adapter.setDataList(lists);
		listView.setAdapter(adapter);
		ScrollListViewUtil.setListViewHeightBasedOnChildren(listView);
		listView.setOnItemClickListener(onItemClickAvoidForceListener);
		// 打开下面一块
		if (!isRequest)
			request(CommandConstants.COURSELIST);
	}

	private void request(final String cmd)
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(UserDetailActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("UserID", SharedPreferencesConfig.config(UserDetailActivity.this).get(Constant.USER_ID));

		new HttpPostAsync(UserDetailActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("UserProfile=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(UserDetailActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(UserDetailActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(UserDetailActivity.this).showToast(desc);
					}
					else
					{
						// 成功后处理
						if (CommandConstants.USERPROFILE.equals(cmd))
						{
							// mapstr
							maps = mapstr;
							mHandler.sendEmptyMessage(1);
						}
						else if (CommandConstants.COURSELIST.equals(cmd))
						{
							lists = JsonUtil.getList(mapstr.get("Courses").toString());
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
					SharedPreferencesConfig.saveConfig(UserDetailActivity.this, Constant.USER_AVATAR,
							StringUtil.Object2String(maps.get("Avatar")));
					SharedPreferencesConfig.saveConfig(UserDetailActivity.this, Constant.USER_NAME,
							StringUtil.Object2String(maps.get("Title")));
					SharedPreferencesConfig.saveConfig(UserDetailActivity.this, Constant.USER_GENDER,
							StringUtil.Object2String(maps.get("Sex")));
					SharedPreferencesConfig.saveConfig(UserDetailActivity.this, Constant.USER_LOCATION,
							StringUtil.Object2String(maps.get("Location")));
					SharedPreferencesConfig.saveConfig(UserDetailActivity.this, Constant.USER_INTRODUCTION,
							StringUtil.Object2String(maps.get("Introduction")));
					initView(true);
					break;
				case 2:
					// SharedPreferencesConfig.saveConfig(UserDetailActivity.this, Constant.USER_COURSEARRAY,
					// StringUtil.Object2String(lists.toString()));
					initListView(true);
					break;
				default:
					break;
			}
		}
	};

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
					finishMyActivity();
					break;
				default:
					break;
			}
		}
	};

	private OnItemClickAvoidForceListener onItemClickAvoidForceListener = new OnItemClickAvoidForceListener()
	{

		@Override
		public void onItemClickAvoidForce(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			Intent intent = new Intent();
			intent.setClass(UserDetailActivity.this, ManageDetailActivity.class);
			intent.putExtra("ID", (Integer) lists.get(arg2).get("ID"));// 学习资料
			intent.putExtra("Title", StringUtil.Object2String(lists.get(arg2).get("Title")));
			intent.putExtra("yaoQiu", StringUtil.Object2String(lists.get(arg2).get("Requirements")));
			intent.putExtra("zhouQi", StringUtil.Object2String(lists.get(arg2).get("Period")));
			intent.putExtra("muBiao", StringUtil.Object2String(lists.get(arg2).get("Aim")));
			intent.putExtra("AcceptMaterial", (Integer) lists.get(arg2).get("AcceptMaterial"));// 学习资料
			intent.putExtra("AcceptTraining", (Integer) lists.get(arg2).get("AcceptTraining"));// 训练项目
			startActivity(intent);
		}
	};

}
