package com.demo.jxdemo.ui.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.base.util.ScrollListViewUtil;
import com.demo.base.util.Utils;
import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.adapter.AttachListAdapter;
import com.demo.jxdemo.ui.adapter.LearningMaterialCommentListAdapter;

/**
 * 学习资料详细页面
 * 
 * @author Chan
 */
public class LearningMaterialsActivity extends BaseActivity
{
	private TextView colTextView;

	private ListView attachListView;

	// private ListView commentListView;
	private LinearLayout commentLayout;

	private AttachListAdapter attachAdapter;

	// private LearningMaterialCommentListAdapter commentAdapter;

	private List<Map<String, Object>> lists;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_learningmaterials);

		findViews();
		initData();
		initView();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText("详细信息");
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.img_back);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);
		colTextView = (TextView) findViewById(R.id.text_right);
		colTextView.setText("收藏");
		colTextView.setVisibility(View.VISIBLE);

		attachListView = (ListView) findViewById(R.id.list_learn_attach);
		commentLayout = (LinearLayout) findViewById(R.id.list_learn_comment);
		// commentListView = (ListView) findViewById(R.id.list_learn_comment);

		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.four);

		DisplayMetrics dm = Utils.getDisplayMetrics(this);

		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		float currentHight = ((float) (bitmapOrg.getHeight() * screenWidth)) / ((float) bitmapOrg.getWidth());

		((ImageView) findViewById(R.id.detail_top_image)).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) currentHight));
	}

	private void initData()
	{
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("11", "111");
		Map<String, Object> m2 = new HashMap<String, Object>();
		m2.put("11", "111");
		Map<String, Object> m3 = new HashMap<String, Object>();
		m3.put("11", "111");

		lists = new ArrayList<Map<String, Object>>();
		lists.add(m1);
		lists.add(m2);
		lists.add(m3);
	}

	private void initView()
	{

		attachAdapter = new AttachListAdapter(LearningMaterialsActivity.this);
		attachAdapter.setDataList(lists);
		attachListView.setAdapter(attachAdapter);
		ScrollListViewUtil.setListViewHeightBasedOnChildren(attachListView);

		// commentAdapter = new LearningMaterialCommentListAdapter(LearningMaterialsActivity.this);
		// commentAdapter.setDataList(lists);
		// commentListView.setAdapter(commentAdapter);
		// ScrollListViewUtil.setListViewHeightBasedOnChildren(commentListView);

		if (lists != null)
		{
			for (int i = 0; i < lists.size(); i++)
			{
				LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_learningmaterials_item_comment,
						null);
				commentLayout.addView(layout);
			}
		}

		attachListView.setOnItemClickListener(onItemClickAvoidForceListener);
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.layout_return:
					finishMyActivity();
					break;
				default:
					break;
			}
		}
	};

	private OnItemClickAvoidForceListener onItemClickAvoidForceListener = new OnItemClickAvoidForceListener()
	{

		@Override
		public void onItemClickAvoidForce(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
		}
	};

}
