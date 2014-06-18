package com.demo.jxdemo.ui.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.base.util.ScrollListViewUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.adapter.AttachListAdapter;

/**
 * 训练
 * 
 * @author Chan
 */
public class TrainingActivity extends BaseActivity
{

	private ListView attachListView;

	private AttachListAdapter attachAdapter;

	// private LearningMaterialCommentListAdapter commentAdapter;

	private List<Map<String, Object>> lists;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_traning);

		findViews();
		initData();
		initView();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText("详细信息");
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.img_back);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);

		attachListView = (ListView) findViewById(R.id.list_traning_attach);
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

		attachAdapter = new AttachListAdapter(TrainingActivity.this);
		attachAdapter.setDataList(lists);
		attachListView.setAdapter(attachAdapter);
		ScrollListViewUtil.setListViewHeightBasedOnChildren(attachListView);

		attachListView.setOnItemClickListener(onItemClickAvoidForceListener);
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		((RelativeLayout) findViewById(R.id.rlayout_chat)).setOnClickListener(onClickAvoidForceListener);
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
				case R.id.rlayout_chat:
					Intent intent = new Intent();
					intent.setClass(TrainingActivity.this, ChatActivity.class);
					startActivity(intent);
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
		}
	};

}
