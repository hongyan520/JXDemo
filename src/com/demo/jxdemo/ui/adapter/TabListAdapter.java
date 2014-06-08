package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.holder.TabListHolder;

public class TabListAdapter extends BaseAdapter
{

	/**
	 * Layout填充器
	 */
	private LayoutInflater inflater = null;

	/**
	 * 存储列表实体类的list
	 */
	private List<Map<String, Object>> list = null;

	private String type;

	/**
	 * 列表类
	 */
	@SuppressWarnings("unused")
	private Context context;

	private int temp = -1;

	public void setTemp(int temp)
	{
		this.temp = temp;
	}

	public int getTemp()
	{
		return temp;
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            列表类
	 */
	public TabListAdapter(Context context)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public String getType()
	{
		return type;
	}

	/**
	 * 设置数据源
	 * 
	 * @param list
	 *            存储列表实体类的list
	 */
	public void setDataList(List<Map<String, Object>> list, String type)
	{
		this.list = list;
		this.type = type;
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
		final Map<String, Object> map = (Map<String, Object>) list.get(position);

		final TabListHolder holder;
		convertView = inflater.inflate(R.layout.activity_tab_list, null);
		holder = new TabListHolder(convertView, context);
		holder.setValue(map, type);
		Log.d("", "FLAG  -------position=" + position + "   temp=" + temp);
		convertView.setTag(holder);

		return convertView;

	}

}
