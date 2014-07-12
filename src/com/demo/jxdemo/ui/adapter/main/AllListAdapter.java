package com.demo.jxdemo.ui.adapter.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
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

public class AllListAdapter extends BaseAdapter
{

	static class ViewHolder
	{
		// /////学习资料///////
		TextView text_title;

		TextView text_content;

		TextView text_xxly;

		TextView text_pl;

		// ////训练//////

		TextView text_state;

	}

	private Context context;

	private List<Map<String, Object>> news;

	public AllListAdapter(Context context, List<Map<String, Object>> news)
	{
		this.context = context;
		this.news = news;
	}

	public List<Map<String, Object>> getList()
	{
		return news;
	}

	@Override
	public int getCount()
	{
		return news.size();
	}

	@Override
	public Map<String, Object> getItem(int position)
	{
		return news.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final Map<String, Object> map = news.get(position);
		// 获得holder
		ViewHolder holder = null;
		// 先确保view存在
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_main_listview_item, null);

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
						intent.putExtra("FragmentID", StringUtil.Object2String(map.get("ID")));
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
						intent.putExtra("FragmentID", StringUtil.Object2String(map.get("ID")));
						context.startActivity(intent);
					}
				});
			}
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

}
