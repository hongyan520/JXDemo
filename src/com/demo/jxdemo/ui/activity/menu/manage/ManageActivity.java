package com.demo.jxdemo.ui.activity.menu.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;
import com.demo.jxdemo.ui.adapter.ManageListAdapter;
import com.demo.jxdemo.utils.ToastManager;

public class ManageActivity extends BaseSlidingActivity
{
	private ListView listView;

	private ManageListAdapter adapter;

	private List<Map<String, Object>> lists;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_manage);
		loadMenu();

		findViews();
		initData();
		initView();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_manage_choose));
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.text_right)).setText(getResources().getString(R.string.left_manage_success));
		((TextView) findViewById(R.id.text_right)).setVisibility(View.VISIBLE);

		listView = (ListView) findViewById(R.id.list_manage);
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

		// request();
	}

	private void request()
	{

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(ManageActivity.this).get(Constant.USER_TOKEN));

		new HttpPostAsync(ManageActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("CourseList=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(ManageActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(ManageActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(ManageActivity.this).showToast(desc);
					}
					else
					{
						loadSuccessDeal(mapstr);// 成功后处理
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.COURSELIST, parasTemp);

	}

	@SuppressWarnings("unchecked")
	private void loadSuccessDeal(Map<String, Object> map)
	{
		new LoadDealTask().execute(map);
	}

	class LoadDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
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
				if (map != null)
				{
					mHandler.sendEmptyMessage(1);
					dismissProgress();
				}
				else
					ToastManager.getInstance(ManageActivity.this).showToast("获取失败!");

			}
			catch (Exception e)
			{
				e.printStackTrace();
				ToastManager.getInstance(ManageActivity.this).showToast("获取失败!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			dismissProgress();
		}

	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					adapter = new ManageListAdapter(ManageActivity.this);
					initView();
					break;
				default:
					break;
			}
		}
	};

	private void initView()
	{
		adapter = new ManageListAdapter(ManageActivity.this);
		adapter.setDataList(lists);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickAvoidForceListener);
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
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
				case R.id.layout_remark:
					ToastManager.getInstance(ManageActivity.this).showToast("完成.......");
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
			intent.setClass(ManageActivity.this, ManageDetailActivity.class);
			startActivity(intent);
		}
	};

}
