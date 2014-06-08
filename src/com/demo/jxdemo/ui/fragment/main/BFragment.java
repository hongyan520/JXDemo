package com.demo.jxdemo.ui.fragment.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.adapter.ItemMenuApapter;
import com.demo.jxdemo.ui.customviews.DragGridView;
import com.demo.jxdemo.ui.customviews.MainPagerAdapter;
import com.demo.jxdemo.ui.customviews.MyViewPager;
import com.demo.jxdemo.ui.fragment.BaseFragment;
import com.demo.jxdemo.utils.ToastManager;

public class BFragment extends BaseFragment
{

	private int pageNum = 0;// 功能菜单所占的屏幕数量

	private int pageSize = 9; // 手机屏幕一屏显示的功能菜单数量

	private int numEachRow = 3;// 每行的模块数目

	private int padding = 30;//

	private MyViewPager viewPager;

	private MainPagerAdapter pagerAdapter;

	private ArrayList<View> pagerContents = null;

	private int pagerIndex = 1;

	private List<Map<String, Object>> menuList;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO 可以重写该方法对数据加载时dialog的一些属性进行设置
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Object onCreateTaskLoadData(Object... params)
	{
		menuList = new ArrayList<Map<String, Object>>();

		if (menuList == null || menuList.size() == 0)
		{
			menuList = loadMenuData();
		}
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
		((ViewGroup) getView()).addView(inflater.inflate(R.layout.fragment_main_work, null));
		((TextView) getActivity().findViewById(R.id.formTilte)).setText("testB");
		((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);

		// 菜单显示出来
		if (menuList != null && menuList.size() > 0)
		{
			// 菜单加载
			loadPermissionMenuItems();
		}

		return null;
	}

	/**
	 * 菜单数据
	 * 
	 * @throws Exception
	 */
	private List<Map<String, Object>> loadMenuData()
	{
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> tempHashMap1 = new HashMap<String, Object>();
		Map<String, Object> tempHashMap2 = new HashMap<String, Object>();
		Map<String, Object> tempHashMap3 = new HashMap<String, Object>();
		Map<String, Object> tempHashMap4 = new HashMap<String, Object>();
		Map<String, Object> tempHashMap5 = new HashMap<String, Object>();

		tempHashMap1.put("MODULE_NAME", "TabScroll");
		tempHashMap1.put("MODULE_ID", "1");// 用于标记意向视图所要指向的类标识

		tempHashMap2.put("MODULE_NAME", "Panel");
		tempHashMap2.put("MODULE_ID", "2");// 用于标记意向视图所要指向的类标识

		tempHashMap3.put("MODULE_NAME", "SwitcherImg");
		tempHashMap3.put("MODULE_ID", "3");// 用于标记意向视图所要指向的类标识

		tempHashMap4.put("MODULE_NAME", "Panel2");
		tempHashMap4.put("MODULE_ID", "4");// 用于标记意向视图所要指向的类标识

		tempHashMap5.put("MODULE_NAME", "合起来");
		tempHashMap5.put("MODULE_ID", "5");// 用于标记意向视图所要指向的类标识
		lists.add(tempHashMap1);
		lists.add(tempHashMap2);
		lists.add(tempHashMap3);
		lists.add(tempHashMap4);
		lists.add(tempHashMap5);
		return lists;
	}

	public void loadPermissionMenuItems()
	{
		viewPager = (MyViewPager) getActivity().findViewById(R.id.vPager);
		viewPager.setHorizontalScrollBarEnabled(false);
		viewPager.removeAllViews();
		pagerContents = new ArrayList<View>();
		pagerContents.clear();
		pageNum = menuList.size() / pageSize;
		pageNum = (pageNum * pageSize < menuList.size() ? pageNum + 1 : pageNum);

		for (int i = 0; i < pageNum; i++)
		{
			LinearLayout linearLayout = new LinearLayout(getActivity());
			linearLayout.setTag(i);
			DragGridView mGrid = new DragGridView(getActivity());
			int mcount = 0;
			if (pageNum * pageSize > menuList.size() && i == pageNum - 1)
			{
				mcount = menuList.size() - i * pageSize;
			}
			else
			{
				mcount = pageSize;
			}
			mGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
			mGrid.setAdapter(new ItemMenuApapter(getActivity(), menuList, null, i * pageSize, mcount, 0, 0));
			mGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			mGrid.setGravity(Gravity.CENTER); // 位置居中
			mGrid.setColumnWidth(100);// 该设置无实际意义，不设置将无法显示
			mGrid.setPadding(0, padding, 0, 0);
			mGrid.setNumColumns(numEachRow); // 设置每行列数
			mGrid.setVerticalSpacing(padding); // 垂直间隔
			mGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
			mGrid.setOnItemClickListener(new OnItemClickListener()
			{// 点击屏幕菜单的监听事件
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{

					String name = menuList.get(arg2).get("MODULE_NAME").toString();
					String intentdir = menuList.get(arg2).get("MODULE_ID").toString();
					Intent intent = new Intent();
					if (!StringUtil.isBlank(name))
						intent.putExtra("title", name);
					// if ("1".equals(intentdir))
					// {
					// intent.setClass(getActivity(), TabScrollActivity.class);
					// }
					// else if ("2".equals(intentdir))
					// {
					// intent.setClass(getActivity(), PanelActivity.class);
					// }
					// else if ("3".equals(intentdir))
					// {
					// ToastManager.getInstance(getActivity()).showToast("对不起，您未被授权");
					// return;
					// }
					// else if ("4".equals(intentdir))
					// {
					// intent.setClass(getActivity(), PanelActivity2.class);
					// }
					// else if ("5".equals(intentdir))
					// {
					// intent.setClass(getActivity(), MainActivity.class);
					// }
					// else
					// {
					ToastManager.getInstance(getActivity()).showToast("对不起，您未被授权");
					return;
					// }
					// startActivity(intent);
				}
			});

			mGrid.setHorizontalScrollBarEnabled(false);
			linearLayout.setHorizontalScrollBarEnabled(false);
			linearLayout.addView(mGrid, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			pagerContents.add(linearLayout);

		}
		Constant.PAGESIZE = pageSize;
		pagerAdapter = new MainPagerAdapter(getActivity(), pagerContents, pageNum, viewPager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(pagerAdapter);
		viewPager.setCurrentItem(pagerIndex - 1, false);
		pagerAdapter.notifyDataSetChanged();

	}

	/*
	 * 当有菜单改变的时候加在数据并更新
	 */
	public void reloadWorkWhenChanger()
	{
		// menuList = DE.getModuleList().getList();
		if (menuList == null)
		{
			Log.d("", "FALG  -reload--menlist=null");
			menuList = loadMenuData();
		}

		loadPermissionMenuItems();

	}

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		if (!hidden)
		{
			((TextView) getActivity().findViewById(R.id.formTilte)).setText("testB");
			((LinearLayout) getActivity().findViewById(R.id.windowtitle)).setVisibility(View.VISIBLE);
		}
		super.onHiddenChanged(hidden);
	}
}
