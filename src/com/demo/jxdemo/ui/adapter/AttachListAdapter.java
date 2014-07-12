package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.demo.base.global.ActivityTaskManager;
import com.demo.base.support.CacheSupport;
import com.demo.base.util.BitmapUtils;
import com.demo.base.util.FileUtils;
import com.demo.base.util.HttpUtils;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.activity.menu.UserInfoActivity;

public class AttachListAdapter extends BaseAdapter
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
	public AttachListAdapter(Context context)
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
		final Map<String, Object> map = list.get(position);
		// 先确保view存在
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.activity_learningmaterials_item_attach, null);
		}

		// 获得holder

		Object tag = convertView.getTag();
		final AttachListHolder holder;
		if (tag == null)
		{
			holder = new AttachListHolder(convertView);
		}
		else
		{
			holder = (AttachListHolder) tag;
		}
		holder.setData(map);
		// 设置tag
		convertView.setTag(holder);
		
		holder.rightBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(final View v)
			{
				final String serverUrl = CommandConstants.URL_ROOT + StringUtil.Object2String(map.get("URL"));
				final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
				new Thread()
				{
					public void run()
					{
						if (HttpUtils.downloadFile(serverUrl, localUrl))
						{
							Message msg = new Message();
							msg.what = 1;
							msg.obj = holder;
							Bundle bundle = new Bundle();
							bundle.putString("localUrl", localUrl);
							msg.setData(bundle);
							mHandler.sendMessage(msg);
						}
					};
				}.start();
			}
		});

		return convertView;
	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			
			switch (msg.what)
			{
				case 1:
					AttachListHolder holder = (AttachListHolder) msg.obj;
					if ("下载".equals(holder.rightBtn.getText().toString()))
					{
						holder.rightBtn.setText("暂停");
					}
					else if ("暂停".equals(holder.rightBtn.getText().toString()))
					{
						holder.rightBtn.setText("下载");
					}
					break;
				default:
					break;
			}
		}
	};

	private class AttachListHolder
	{

		TextView textTitle;

		TextView textSize;

		Button leftIcon;

		Button rightBtn;

		private AttachListHolder(View convertView)
		{
			textTitle = (TextView) convertView.findViewById(R.id.text_title);
			textSize = (TextView) convertView.findViewById(R.id.text_size);
			leftIcon = (Button) convertView.findViewById(R.id.btn_left);
			rightBtn = (Button) convertView.findViewById(R.id.btn_right);
		}

		private void setData(Map<String, Object> map)
		{
			String[] a;// 分隔整个url
			String[] b;// 分隔标题和文件类型
			String url = StringUtil.Object2String(map.get("URL"));
			if (!StringUtil.isBlank(url))
			{
				a = url.split("/");
				url = a[a.length - 1];
			}
			b = url.replace(".", "!").split("!");
			textTitle.setText(StringUtil.Object2String(url));
			textSize.setText(StringUtil.Object2String(map.get("Size")));
			leftIcon.setText(StringUtil.Object2String(b[b.length - 1]));
		}
	}

}
