package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.base.support.CacheSupport;
import com.demo.base.util.FileUtils;
import com.demo.base.util.HttpUtils;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.constant.CommandConstants;

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

		ImageView dyImageView;

		ImageView xlImageView;

		private ManageListHolder(View convertView)
		{
			textTitle = (TextView) convertView.findViewById(R.id.text_title);
			textContent = (TextView) convertView.findViewById(R.id.text_content);
			imageView = (ImageView) convertView.findViewById(R.id.img_icon);
			dyImageView = (ImageView) convertView.findViewById(R.id.img_dy);
			xlImageView = (ImageView) convertView.findViewById(R.id.img_xl);
		}

		private void setData(Map<String, Object> map)
		{
			if ((Integer) map.get("AcceptMaterial") == 1){
				dyImageView.setBackgroundResource(R.drawable.icon_ok);
			} else{
				dyImageView.setBackgroundResource(R.drawable.icon_ok_gray);
			}
			if ((Integer) map.get("AcceptTraining") == 1){
				xlImageView.setBackgroundResource(R.drawable.icon_ok);
			} else{
				xlImageView.setBackgroundResource(R.drawable.icon_ok_gray);
			}

			textTitle.setText(StringUtil.Object2String(map.get("Title")));
			textContent.setText(StringUtil.Object2String(map.get("Abstract")));
			
			if(textTitle.getText().toString().contains("口语")){
				imageView.setBackgroundResource(R.drawable.iconspoken);
			}else if(textTitle.getText().toString().contains("写作")){
				imageView.setBackgroundResource(R.drawable.iconwriting);
			}
			
			// 动态设置drawableLeft图标，（先下载到本地缓存，再读取本地缓存）
			// TODO
//			String iconUrlStr = StringUtil.Object2String(map.get("Icon"));
//			final String serverUrl = CommandConstants.URL_ROOT+iconUrlStr;
//			final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
//			// 下载成功
//			//（file转bitmap转Drawable）
//			Drawable db = FileUtils.imgPathToDrawable(localUrl, context,60,60);
//			if(db != null){
//				imageView.setBackgroundDrawable(db);
//			}
		}
	}

}
