package com.demo.jxdemo.ui.fragment.main;

import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.ui.fragment.BaseFragment;
import com.demo.jxdemo.R;

public class AFragment extends BaseFragment
{

	private Map<String, String> configMap;

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
		configMap = SharedPreferencesConfig.config(getActivity());
		((ViewGroup) getView()).removeAllViews();
		((ViewGroup) getView()).addView(inflater.inflate(R.layout.fragment_main_mess, null));
		((TextView) getActivity().findViewById(R.id.formTilte)).setText("test");
		((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);

		// if (StringUtil.isBlank(configMap.get(Constant.USER_ID)))
		// {
		// ToastManager.getInstance(getActivity()).showToast("请先登录!");
		// }
		// else
		// {
		showProgress();
		initData();
		setViewClick();
		// }
		return null;
	}

	private void initData()
	{
		mHandler.sendEmptyMessage(1);
	}

	private void setViewClick()
	{
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
		}
	};

	private OnItemClickAvoidForceListener onItemClickAvoidForceListener = new OnItemClickAvoidForceListener()
	{

		@Override
		public void onItemClickAvoidForce(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
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
			((TextView) getActivity().findViewById(R.id.formTilte)).setText("test");
			((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);
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
