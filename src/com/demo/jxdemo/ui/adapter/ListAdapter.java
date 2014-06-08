package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListAdapter extends BaseAdapter
{
	/**
	 * Layout填充器
	 */
	private LayoutInflater inflater = null;

	/**
	 * 存储列表实体类的list
	 */
	private List<Map<String, Object>> list = null;

	/**
	 * 列表类
	 */
	@SuppressWarnings("unused")
	private Context context;

	private int flag = 0;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            列表类
	 */
	public ListAdapter(Context context)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public void setDataList(List<Map<String, Object>> list, int flag)
	{
		this.list = list;
		this.flag = flag;
	}

	public List<Map<String, Object>> getList()
	{
		return list;
	}

	@Override
	public int getCount()
	{
		return list == null ? 0 : list.size();
		// return 5;
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final Map<String, Object> map = list.get(position);
		// 先确保view存在
		if (convertView == null)
		{
			// TODO
			// convertView = inflater.inflate(R.layout.fragment_main_grid_item, null);
		}

		// 获得holder
		MainGridHolder holder = null;
		Object tag = convertView.getTag();
		if (tag == null)
		{
			holder = new MainGridHolder(convertView);
		}
		else
		{
			holder = (MainGridHolder) tag;
		}
		holder.setData(map, flag);
		// 设置tag
		convertView.setTag(holder);

		return convertView;
	}

	private class MainGridHolder
	{

		private MainGridHolder(View convertView)
		{
		}

		private void setData(Map<String, Object> map, int flag)
		{
		}
	}
}
