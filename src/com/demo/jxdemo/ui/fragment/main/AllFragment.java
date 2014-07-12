package com.demo.jxdemo.ui.fragment.main;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.adapter.main.AllListAdapter;
import com.demo.jxdemo.utils.UIUtils;

public class AllFragment extends Fragment
{

	private final static String TAG = "AllFragment";

	Activity activity;

	String text;

	public final static int SET_NEWSLIST = 0;

	private AllListAdapter adapter = null;

	private ListView listView;

	private List<Map<String, Object>> list;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		initData();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.test_main_list, null);
		listView = (ListView) view.findViewById(R.id.listView1);

		initView();
		return view;
	}

	private void initView()
	{
		adapter = new AllListAdapter(getActivity(), list);
		listView.setAdapter(adapter);
	}

	@SuppressWarnings("unchecked")
	private void initData()
	{
		Bundle bundle = getArguments();
		list = (List<Map<String, Object>>) bundle.getSerializable("lists");
	}

	/* 摧毁视图 */
	@Override
	public void onDestroyView()
	{
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	/* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**
	 * 动态设置ViewPager的高度（根据listView的数据量计算）
	 * 
	 * @param _viewPager
	 * @param _listView
	 */
	private void autoChangeViewPagerHeight(ViewPager _viewPager, ListView _listView)
	{
		int totalHeight = UIUtils.getTotalHeightofListView(_listView);
		if (totalHeight > 0)
		{
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) _viewPager.getLayoutParams();
			params.height = totalHeight + listView.getAdapter().getCount() * UIUtils.px2dip(getActivity(), 40);
			_viewPager.setLayoutParams(params);
		}
	}


}
