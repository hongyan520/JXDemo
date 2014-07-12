package com.demo.jxdemo.ui.fragment.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.adapter.main.AllListAdapter;
import com.demo.jxdemo.ui.adapter.main.TrainingListAdapter;

public class TraningFragment extends Fragment
{

	Activity activity;

	String text;

	public final static int SET_NEWSLIST = 0;

	private TrainingListAdapter adapter = null;

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
		adapter = new TrainingListAdapter(getActivity(), list);
		listView.setAdapter(adapter);
	}

	@SuppressWarnings("unchecked")
	private void initData()
	{
		Bundle bundle = getArguments();
		list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : (List<Map<String, Object>>) bundle.getSerializable("lists"))
		{
			if ((Integer) map.get("Type") == 1)
			{
				list.add(map);
			}
		}
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

}
