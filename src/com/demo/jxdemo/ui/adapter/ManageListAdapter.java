package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;

public class ManageListAdapter extends BaseAdapter
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

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            列表类
	 */
	public ManageListAdapter(Context context)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
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
		Map<String, Object> map = list.get(position);
		// 先确保view存在
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.activity_menu_manage_item, null);
		}

		// 获得holder
		ManageListHolder holder = null;
		Object tag = convertView.getTag();
		if (tag == null)
		{
			holder = new ManageListHolder(convertView);
		}
		else
		{
			holder = (ManageListHolder) tag;
		}
		holder.setData(map);
		// 设置tag
		convertView.setTag(holder);

		return convertView;
	}

	private class ManageListHolder
	{

		TextView textTitle;

		TextView textContent;

		ImageView imageView;

		private ManageListHolder(View convertView)
		{
			textTitle = (TextView) convertView.findViewById(R.id.text_title);
			textContent = (TextView) convertView.findViewById(R.id.text_content);
			imageView = (ImageView) convertView.findViewById(R.id.img_icon);
		}

		private void setData(Map<String, Object> map)
		{
			textTitle.setText(StringUtil.Object2String(map.get("Title")));
			textContent.setText(StringUtil.Object2String(map.get("Abstract")));
		}
	}

}
