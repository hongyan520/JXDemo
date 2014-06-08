package com.demo.jxdemo.ui.holder;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.demo.jxdemo.R;

public class TabListHolder
{

	@SuppressWarnings("unused")
	private Map<String, Object> map = null;

	protected TextView contentTextView;

	private Context context;

	/**
	 * 构造方法
	 * 
	 * @param convertView
	 */
	public TabListHolder(final View convertView, final Context context)
	{
		this.context = context;

		contentTextView = (TextView) convertView.findViewById(R.id.text_vote_content);
	}

	public void setValue(Map<String, Object> map, String voteType)
	{
		this.map = map;

		String xx = map.get("xx") == null ? "" : map.get("xx").toString();
		contentTextView.setText(xx);

	}
}
