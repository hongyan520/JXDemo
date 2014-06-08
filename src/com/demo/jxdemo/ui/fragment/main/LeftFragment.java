package com.demo.jxdemo.ui.fragment.main;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.base.global.ActivityTaskManager;
import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.login.LoginActivity;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.activity.menu.AboutActivity;
import com.demo.jxdemo.ui.activity.menu.OptionActivity;
import com.demo.jxdemo.ui.activity.menu.UserInfoActivity;
import com.demo.jxdemo.ui.activity.menu.manage.ManageActivity;
import com.demo.jxdemo.ui.fragment.BaseFragment;

public class LeftFragment extends BaseFragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO 可以重写该方法对数据加载时dialog的一些属性进行设置
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Object onCreateTaskLoadData(Object... params)
	{
		// TODO 在这里做相关的数据耗时操作
		return null;
	}

	@Override
	protected Object onEndTaskAddView(Object result)
	{
		// TODO 界面和数据的适配操作

		if (getView() == null)
		{
			return null;
		}
		((ViewGroup) getView()).removeAllViews();
		((ViewGroup) getView()).addView(inflater.inflate(R.layout.fragment_left, null));

		showProgress();
		initData();
		setViewClick();
		return null;
	}

	private void initData()
	{
		mHandler.sendEmptyMessage(1);
	}

	private void setViewClick()
	{
		((TextView) getActivity().findViewById(R.id.text_index)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_speak)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_write)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_manage)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_self)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_option)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_about)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_logout)).setOnClickListener(onClickAvoidForceListener);
		
//		((TextView) getActivity().findViewById(R.id.text_speak)).setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				MainActivity.newInstance().refreshData(1);
//				
//			}
//		});
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			Intent intent = new Intent();
			switch (v.getId())
			{
				case R.id.text_index:
					intent.setClass(getActivity(), MainActivity.class);
					intent.putExtra("type", 0);
					break;
				case R.id.text_speak:
					intent.setClass(getActivity(), MainActivity.class);
					intent.putExtra("type", 1);
					break;
				case R.id.text_write:
					intent.setClass(getActivity(), MainActivity.class);
					intent.putExtra("type", 2);
					break;
				case R.id.text_manage:
					intent.setClass(getActivity(), ManageActivity.class);
					break;
				case R.id.text_self:
					intent.setClass(getActivity(), UserInfoActivity.class);
					break;
				case R.id.text_option:
					intent.setClass(getActivity(), OptionActivity.class);
					break;
				case R.id.text_about:
					intent.setClass(getActivity(), AboutActivity.class);
					break;
				case R.id.text_logout:
					ActivityTaskManager.getInstance().closeAllActivity();
					intent.setClass(getActivity(), LoginActivity.class);
					break;
				default:
					break;
			}
			ActivityTaskManager.getInstance().closeAllActivity();
			startActivity(intent);
		}
	};

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					closeProgress();
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		if (!hidden)
		{
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	protected void onBaseResume()
	{
		// TODO Auto-generated method stub
		super.onBaseResume();
	}

}
