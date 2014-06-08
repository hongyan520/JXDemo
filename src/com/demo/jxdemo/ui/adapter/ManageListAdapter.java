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

		// TextView textQi;
		//
		// TextView textMoney;
		//
		// TextView textDate;
		//
		// TextView textStatus;
		//
		// TextView textTitle;
		//
		// ImageView imageView;

		private ManageListHolder(View convertView)
		{
			// textQi = (TextView) convertView.findViewById(R.id.text_qi);
			// textMoney = (TextView) convertView.findViewById(R.id.text_money);
			// textDate = (TextView) convertView.findViewById(R.id.text_date);
			// textStatus = (TextView) convertView.findViewById(R.id.text_result);
			// textTitle = (TextView) convertView.findViewById(R.id.text_title);
			// imageView = (ImageView) convertView.findViewById(R.id.img_icon);
		}

		private void setData(Map<String, Object> map)
		{
			// String money = StringUtil.Object2String(map.get("betting_amount"));
			// String cpType = StringUtil.Object2String(map.get("lottery_type_name"));
			// textQi.setText(StringUtil.Object2String("第" + map.get("order_nper")));// xx+1.0e-6 betting_amount
			// textMoney.setText(money.substring(0, money.indexOf(".")) + "元");
			// textDate.setText(StringUtil.Object2String(map.get("time")));// 订单日期
			// textStatus.setText(StringUtil.Object2String(map.get("order_status_name")));// 状态
			// textTitle.setText(cpType);
			// if (context.getResources().getString(R.string.gc_dlt).equals(cpType))
			// imageView.setBackgroundResource(R.drawable.logo_dlt_72);
			// else if (context.getResources().getString(R.string.gc_qxc).equals(cpType))
			// imageView.setBackgroundResource(R.drawable.logo_qxc_72);
			// else if (context.getResources().getString(R.string.gc_pl3).equals(cpType))
			// imageView.setBackgroundResource(R.drawable.logo_pl3_72);
			// else if (context.getResources().getString(R.string.gc_pl5).equals(cpType))
			// imageView.setBackgroundResource(R.drawable.logo_pl5_72);
		}
	}

}
