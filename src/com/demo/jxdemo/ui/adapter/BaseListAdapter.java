package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.demo.jxdemo.ui.activity.BaseListActivity;

public class BaseListAdapter extends BaseAdapter
{

	private List<Map<String, Object>> list = null;

	private BaseListActivity activity = null;

	public BaseListAdapter(BaseListActivity context)
	{
		this.activity = context;
	}

	public void setDataList(List<Map<String, Object>> list)
	{
		this.list = list;
	}

	public List<Map<String, Object>> getList()
	{
		return list;
	}

	@Override
	public int getCount()
	{
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Map<String, Object> record = list.get(position);
		if (activity != null)
		{
			return this.activity.getView(record, position, convertView, parent);
		}
		return null;
	}

}
