package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.R;

public class ItemMenuApapter extends BaseAdapter
{

	private int count = 0;

	private int startIndex = 0;

	private int columnWidth = 0;

	private int columnHeight = 0;

	private Context context;

	private List<Map<String, Object>> menuList;

	private Map<String, Integer> countMap;

	public ItemMenuApapter(Context context, List<Map<String, Object>> menuBeans, Map<String, Integer> countMap, int startIndex, int count,
			int columnWidth, int columnHeight)
	{
		this.count = count;
		this.startIndex = startIndex;
		this.columnWidth = columnWidth;
		this.columnHeight = columnHeight;
		this.context = context;
		this.menuList = menuBeans;
		this.countMap = countMap;
	}

	public int getStartIndex()
	{
		return startIndex;
	}

	@Override
	public int getCount()
	{
		return count;
	}

	public Map<String, Object> getItem(int position)
	{
		return (Map<String, Object>) this.menuList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout layout;
		if (startIndex + position > menuList.size())
		{
			return null;
		}

		Map<String, Object> bean = menuList.get(startIndex + position);

		convertView = LayoutInflater.from(context).inflate(R.layout.fragment_main_work_grid_item, null);
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesConfig.CONFIG_NAME, Context.MODE_PRIVATE);
		int imageId = context.getResources().getIdentifier(
				context.getPackageName() + ":drawable/icon_" + bean.get("MODULE_ID").toString() + "_" + sharedPreferences.getString(
						Constant.THEMEKEY, Constant.THEMEVALUE).toString(), null, null);
		convertView.findViewById(R.id.image_pic).setBackgroundResource(imageId);
		((TextView) convertView.findViewById(R.id.image_text)).setText(bean.get("MODULE_NAME").toString());
		return convertView;

	}

	public void exchange(int startPosition, int endPosition)
	{

		if (startPosition > endPosition)
		{
			int temp = endPosition;
			endPosition = startPosition;
			startPosition = temp;
		}
		Object endObject = getItem(endPosition);
		Object startObject = getItem(startPosition);

		menuList.set(startPosition, (Map<String, Object>) endObject);
		menuList.set(endPosition, (Map<String, Object>) startObject);
		notifyDataSetChanged();
	}
}
