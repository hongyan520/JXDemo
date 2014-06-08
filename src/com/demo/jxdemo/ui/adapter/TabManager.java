package com.demo.jxdemo.ui.adapter;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

public class TabManager implements TabHost.OnTabChangeListener
{

	/**
	 * fragment所在的父activity
	 */
	private FragmentActivity mActivity;

	/**
	 * fragment对应的tabhost
	 */
	private TabHost mTabHost;

	/**
	 * fragment标识符
	 */
	private int mContainerId;

	/**
	 * tab页签的内容数据
	 */
	private HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();

	/**
	 * tab页签的内容对象
	 */
	private TabInfo mLastTab;

	/**
	 * fragment的工厂，用于fragment的初始化
	 */
	private DummyTabFactory factory;

	class TabInfo
	{
		private String tag;

		private Class<?> clss;

		private Bundle args;

		private Fragment fragment;

		TabInfo(String _tag, Class<?> _class, Bundle _args)
		{
			tag = _tag;
			clss = _class;
			args = _args;
		}
	}

	class DummyTabFactory implements TabHost.TabContentFactory
	{
		private Context mContext;

		public DummyTabFactory(Context context)
		{
			mContext = context;
		}

		@Override
		public View createTabContent(String tag)
		{
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	public TabManager(FragmentActivity activity, TabHost tabHost, int containerId)
	{
		mActivity = activity;
		mTabHost = tabHost;
		mContainerId = containerId;
		factory = new DummyTabFactory(mActivity);
		mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * 为了保证fragment的通信，在add的时候就初始化fragment，一般情况下fragment
	 * 只有在使用的时候才回去实例化
	 * 
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
	{
		tabSpec.setContent(factory);
		String tag = tabSpec.getTag();
		TabInfo info = new TabInfo(tag, clss, args);
		info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
		if (info.fragment != null && !info.fragment.isDetached())
		{
			FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
			ft.detach(info.fragment);
			ft.commit();
		}
		mTabs.put(tag, info);
		mTabHost.addTab(tabSpec);
		// 添加的时候实例化，或者fragmtent通信时去手动初始化
		// manalInitFragment(tag);
	}

	/*
	 * // 备份，勿删，改方法实现fragment的切换时的销毁视图工作。
	 * public void onTabChanged11(String tabId)
	 * {
	 * TabInfo newTab = mTabs.get(tabId);
	 * if (mLastTab != newTab)
	 * {
	 * FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
	 * if (mLastTab != null)
	 * {
	 * if (mLastTab.fragment != null)
	 * {
	 * if(!"contract".equals(mLastTab.fragment.getTag())){
	 * ft.detach(mLastTab.fragment);
	 * }
	 * mLastTab.fragment.getView().setVisibility(View.INVISIBLE);
	 * }
	 * }
	 * if (newTab != null)
	 * {
	 * if (newTab.fragment == null)
	 * {
	 * newTab.fragment = Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
	 * ft.add(mContainerId, newTab.fragment, newTab.tag);
	 * }
	 * else
	 * {
	 * // 显示fragment视图
	 * ft.attach(newTab.fragment);
	 * if("contract".equals(newTab.fragment.getTag())){
	 * ((ContactFragment)newTab.fragment).resetTitle();
	 * newTab.fragment.getView().setVisibility(View.VISIBLE);
	 * }
	 * }
	 * }
	 * mLastTab = newTab;
	 * ft.commit();
	 * mActivity.getSupportFragmentManager().executePendingTransactions();
	 * }
	 * }
	 */

	public void clear()
	{
		factory = null;
		if (mLastTab != null)
		{
			mLastTab.fragment = null;
			mLastTab.clss = null;
			mLastTab = null;
		}
		if (mTabHost != null)
		{
			mTabHost.clearAllTabs();
		}
		mTabs = null;
		if (mActivity != null)
		{
			mActivity.finish();
			mActivity = null;
		}
	}

	/**
	 * 实例化fragment，将该fragment加入到activity的fragment的manager类中达到初始化的目的，
	 * 然后将该framgent的view移除掉。
	 * 
	 * @param tabId
	 */
	public void manalInitFragment(String tabId)
	{
		TabInfo newTab = mTabs.get(tabId);
		FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
		if (newTab != null)
		{
			if (newTab.fragment == null)
			{
				newTab.fragment = Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
				// 加入管理类中
				ft.add(mContainerId, newTab.fragment, newTab.tag);
				// 销毁视图（与activity解除绑定）
				// ft.detach(newTab.fragment);

				// 不销毁视图，只是隐藏
				ft.hide(newTab.fragment);
			}
		}
		ft.commit();
		// commit函数不会立即执行，须等待UI线程执行完之后才会，所以需要调用executePendingTransactions
		// 方法进行执行，在addTab()方法里面不需要执行该方法
		mActivity.getSupportFragmentManager().executePendingTransactions();
	}

	public String getTab()
	{

		return mLastTab.tag;
	}

	public Fragment getCurrentFragment()
	{
		return mLastTab.fragment;
	}

	@Override
	public void onTabChanged(String tabId)
	{
		TabInfo newTab = mTabs.get(tabId);
		FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
		FragmentManager fm = mActivity.getSupportFragmentManager();

		if (newTab == mLastTab)
		{
			return;
		}

		if (newTab != null)
		{
			if (newTab.fragment == null)
			{
				newTab.fragment = Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
				ft.add(mContainerId, newTab.fragment, newTab.tag);

			}

			else
			{
				if (newTab.fragment.getView() == null)
				{
					ft.remove(newTab.fragment);
					Log.d("", "be clean cretor now=" + newTab.tag);
					newTab.fragment = Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
					ft.add(mContainerId, newTab.fragment, newTab.tag);
				}
				else
				{
					ft.show(newTab.fragment);
				}

			}

		}
		if (mLastTab != null && mLastTab.fragment != null)
		{
			ft.hide(mLastTab.fragment);
		}

		// ft.commit();
		// 该方法允许在fragment数据保存的时候提交事务
		ft.commitAllowingStateLoss();
		mActivity.getSupportFragmentManager().executePendingTransactions();
		mLastTab = newTab;

	}

}
