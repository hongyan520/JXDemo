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

import com.demo.jxdemo.R;

public class ChatListAdapter extends BaseAdapter
{
	private Context context = null;

	private List<Map<String, Object>> chatList = null;

	private LayoutInflater inflater = null;

	private int COME_MSG = 0;

	private int TO_MSG = 1;

	public ChatListAdapter(Context context, List<Map<String, Object>> chatList)
	{
		this.context = context;
		this.chatList = chatList;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount()
	{
		return chatList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return chatList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public int getItemViewType(int position)
	{
		// 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
		Map<String, Object> entity = chatList.get(position);
		if ("".equals(entity.get("")))
		{
			return COME_MSG;
		}
		else
		{
			return TO_MSG;
		}
	}

	@Override
	public int getViewTypeCount()
	{
		// 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ChatHolder chatHolder = null;
		if (convertView == null)
		{
			chatHolder = new ChatHolder();
			if ("from".equals(chatList.get(position).get("flag")))
			{
				convertView = inflater.inflate(R.layout.layout_chat_from_item, null);
			}
			else
			{
				convertView = inflater.inflate(R.layout.layout_chat_to_item, null);
			}
			chatHolder.timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
			chatHolder.contentTextView = (TextView) convertView.findViewById(R.id.tv_content);
			chatHolder.userImageView = (ImageView) convertView.findViewById(R.id.iv_user_image);
			convertView.setTag(chatHolder);
		}
		else
		{
			chatHolder = (ChatHolder) convertView.getTag();
		}

//		chatHolder.timeTextView.setText(chatList.get(position).getChatTime());
//		chatHolder.contentTextView.setText(chatList.get(position).getContent());
//		chatHolder.userImageView.setImageResource(chatList.get(position).getUserImage());

		return convertView;
	}

	private class ChatHolder
	{
		private TextView timeTextView;

		private ImageView userImageView;

		private TextView contentTextView;
	}

}
