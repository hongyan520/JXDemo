package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.main.LearningMaterialsActivity;
import com.demo.jxdemo.ui.activity.main.TrainingActivity;

public class MainListAdapter extends BaseAdapter
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
	public MainListAdapter(Context context)
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
	public void unregisterDataSetObserver(DataSetObserver observer)
	{
		if (observer != null)
		{
			super.unregisterDataSetObserver(observer);
		}
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
		// 获得holder
		MainGridHolder holder = null;
		// 先确保view存在
		if (convertView == null)
		{
			holder = new MainGridHolder();
			convertView = inflater.inflate(R.layout.layout_main_listview_item, null);

			LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_main_list);
			if ((Integer) map.get("Type") == 0)
			{
				LinearLayout study = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_studydata, null);
				layout.addView(study);

				((TextView) study.findViewById(R.id.text_title)).setText(StringUtil.Object2String(map.get("Title")));
				((TextView) study.findViewById(R.id.text_content)).setText(StringUtil.Object2String(map.get("Abstract")));
				((TextView) study.findViewById(R.id.text_xxly)).setText(StringUtil.Object2String(map.get("Link")));
				((TextView) study.findViewById(R.id.text_pl)).setText(StringUtil.Object2String(map.get("CommentCount")) + "个评论");
				convertView.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent();
						intent.setClass(context, LearningMaterialsActivity.class);
						context.startActivity(intent);
					}
				});
			}
			else
			{
				LinearLayout training = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_trainingproject, null);
				layout.addView(training);

				((TextView) training.findViewById(R.id.text_title)).setText(StringUtil.Object2String(map.get("Title")));
				((TextView) training.findViewById(R.id.text_content)).setText(StringUtil.Object2String(map.get("RequirementDescription")));
				((TextView) training.findViewById(R.id.text_state)).setText(StringUtil.Object2String(map.get("New")));
				((TextView) training.findViewById(R.id.text_pl)).setText(StringUtil.Object2String(map.get("FeedbackContent")));
				convertView.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent();
						intent.setClass(context, TrainingActivity.class);
						context.startActivity(intent);
					}
				});
			}
			// holder.timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
			// holder.contentTextView = (TextView) convertView.findViewById(R.id.tv_content);
			// holder.userImageView = (ImageView) convertView.findViewById(R.id.iv_user_image);
			convertView.setTag(holder);
		}
		else
		{
			holder = (MainGridHolder) convertView.getTag();
		}

		// chatHolder.timeTextView.setText(chatList.get(position).getChatTime());
		// chatHolder.contentTextView.setText(chatList.get(position).getContent());
		// chatHolder.userImageView.setImageResource(chatList.get(position).getUserImage());

		return convertView;
	}

	private class MainGridHolder
	{
		// /////学习资料///////
		TextView text_title;

		TextView text_content;

		TextView text_xxly;

		TextView text_pl;

		// ////训练//////
		// TextView text_title;
		//
		// TextView text_content;

		TextView text_state;

		// TextView text_pl;
	}
}
