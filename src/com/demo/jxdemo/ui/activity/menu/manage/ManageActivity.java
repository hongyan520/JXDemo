package com.demo.jxdemo.ui.activity.menu.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;
import com.demo.jxdemo.ui.adapter.ManageListAdapter;
import com.demo.jxdemo.utils.ToastManager;

public class ManageActivity extends BaseSlidingActivity
{
	private LinearLayout backLayout;

	private Button successButton;

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
		backLayout = (LinearLayout) findViewById(R.id.returnBtn);
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
		backLayout.setVisibility(View.VISIBLE);

		successButton = (Button) findViewById(R.id.remarkBtn);
		successButton.setText(getResources().getString(R.string.left_manage_success));
		successButton.setVisibility(View.VISIBLE);

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
	}

	private void initView()
	{
		adapter = new ManageListAdapter(ManageActivity.this);
		adapter.setDataList(lists);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickAvoidForceListener);
	}

	private void setViewClick()
	{
		backLayout.setOnClickListener(onClickAvoidForceListener);
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
