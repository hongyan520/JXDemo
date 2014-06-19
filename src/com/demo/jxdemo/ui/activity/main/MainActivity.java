package com.demo.jxdemo.ui.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.base.global.ActivityTaskManager;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.ScrollListViewUtil;
import com.demo.base.util.Utils;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;
import com.demo.jxdemo.ui.adapter.MainListAdapter;
import com.demo.jxdemo.ui.adapter.MainTabPagerAdapter;
import com.demo.jxdemo.ui.adapter.SwitherImageAdapter;
import com.demo.jxdemo.ui.customviews.GuideGallery;
import com.demo.jxdemo.ui.customviews.MyViewPager;
import com.demo.jxdemo.utils.ToastManager;

public class MainActivity extends BaseSlidingActivity
{
	private RelativeLayout windowTitleLayout;

	private LinearLayout searchLayout;

	private EditText searchEditText;

	private boolean isExit;

	/** 传过来的 1:英语口语 2:英语写作 */
	private String courseTitle;

	/************* 图片轮播块 **************/
	private List<String> urls;

	private GuideGallery images_ga;

	private int positon = 0;

	private Thread timeThread = null;

	public boolean timeFlag = true;

	public ImageTimerTask timeTaks = null;

	private Timer autoGallery = new Timer();

	private Uri uri;

	private Intent intent;

	private int gallerypisition = 0;

	/************* tab块 **************/
	private LinearLayout bottomLayout;

	private MyViewPager viewPager;

	public static Button btn1;

	public static Button btn2;

	public static Button btn3;

	public static Button btn4;

	private MainTabPagerAdapter pagerAdapter;

	private ArrayList<View> pagerContents = new ArrayList<View>();

	private List<Map<String, Object>> tabList;

	private ListView listView;

	/***************************/

	public static MainActivity newInstance()
	{
		MainActivity mainActivity = new MainActivity();
		return mainActivity;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		loadMenu();
		courseTitle = getIntent().getStringExtra("courseTitle") == null ? getResources().getString(R.string.app_name) : getIntent()
				.getStringExtra("courseTitle");
		findViews();
		initData();
		initView();
		setViewClick();

		timeTaks = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTaks, 5000, 5000);
		timeThread = new Thread()
		{
			public void run()
			{
				while (!isExit)
				{
					try
					{
						Thread.sleep(1500);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					synchronized (timeTaks)
					{
						if (!timeFlag)
						{
							timeTaks.timeCondition = true;
							timeTaks.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};
		timeThread.start();
		init();
	}

	private void findViews()
	{
		windowTitleLayout = (RelativeLayout) findViewById(R.id.windowtitle);
		searchLayout = (LinearLayout) findViewById(R.id.layout_search);
		searchEditText = (EditText) findViewById(R.id.edit_search);

		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);
		((ImageView) findViewById(R.id.imgview_right)).setBackgroundResource(R.drawable.top_search);
		((ImageView) findViewById(R.id.imgview_right)).setVisibility(View.VISIBLE);

		bottomLayout = (LinearLayout) findViewById(R.id.layout_tab_bottom);
		bottomLayout.setVisibility(View.INVISIBLE);
		btn1 = (Button) findViewById(R.id.btn_1);
		btn2 = (Button) findViewById(R.id.btn_2);
		btn3 = (Button) findViewById(R.id.btn_3);
		btn4 = (Button) findViewById(R.id.btn_4);

		listView = (ListView) (getLayoutInflater().inflate(R.layout.layout_list_page, null).findViewById(R.id.listview));
		listView.setDivider(null);

		viewPager = (MyViewPager) findViewById(R.id.vPage_introduce);
		viewPager.setHorizontalScrollBarEnabled(false);
		btn1.setTextColor(getResources().getColor(R.color.red));
	}

	private void initData()
	{
		String test = SharedPreferencesConfig.config(this).get(Constant.USER_TEST);
		tabList = JsonUtil.getList(test);
		mHandler.sendEmptyMessage(1);
	}

	private void initView()
	{
		((TextView) findViewById(R.id.formTilte)).setText(courseTitle);
	}

	private void initListView(String courseTitle)
	{
		if (tabList != null)
		{
			pagerContents.clear();
			for (int i = 0; i < 4; i++)
			{
				LinearLayout layout = null;
				MainListAdapter mainListAdapter = null;
				if (i == 0)
				{
					mainListAdapter = new MainListAdapter(this);
					mainListAdapter.setDataList(tabList);
					listView.setAdapter(mainListAdapter);
					pagerContents.add(listView);
				}
				else if (i == 2)// ("0".equals(tabList.get(i).get("Type").toString()))
				{// 学习资料
					layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_studydata, null);
					pagerContents.add(layout);
				}
				else if (i == 1)
				{// 训练
					layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_trainingproject, null);
					pagerContents.add(layout);
				}
				else
				{
					layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_date, null);
					pagerContents.add(layout);
				}
			}
		}

		pagerAdapter = new MainTabPagerAdapter(this, pagerContents, 4, viewPager);
		viewPager.setOnPageChangeListener(pagerAdapter);

		viewPager.setAdapter(pagerAdapter);
		pagerAdapter.notifyDataSetChanged();

	}

	private void setViewClick()
	{
		btn3.setOnClickListener(onClickAvoidForceListener);
		btn2.setOnClickListener(onClickAvoidForceListener);
		btn1.setOnClickListener(onClickAvoidForceListener);
		btn4.setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClickAvoidForceListener);
		((Button) findViewById(R.id.btn_cancel)).setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{

			int currentPage = pagerAdapter.cruurentItem;
			// Intent intent = new Intent();
			switch (v.getId())
			{
				case R.id.btn_4:
					pagerAdapter.onPageSelected(3);
					pagerAdapter.setBtnColor(btn4);
					break;
				case R.id.btn_3:
					pagerAdapter.onPageSelected(2);
					pagerAdapter.setBtnColor(btn3);
					// intent.setClass(MainActivity.this, LearningMaterialsActivity.class);
					// startActivity(intent);
					break;
				case R.id.btn_2:
					pagerAdapter.onPageSelected(1);
					pagerAdapter.setBtnColor(btn2);
					// intent.setClass(MainActivity.this, TrainingActivity.class);
					// startActivity(intent);
					break;
				case R.id.btn_1:
					pagerAdapter.onPageSelected(0);
					pagerAdapter.setBtnColor(btn1);
					break;
				case R.id.layout_return:
					getSlidingMenu().toggle();
					break;
				case R.id.layout_remark:
					windowTitleLayout.setVisibility(View.GONE);
					images_ga.setVisibility(View.GONE);
					((LinearLayout) findViewById(R.id.gallery_point_linear)).setVisibility(View.GONE);
					searchLayout.setVisibility(View.VISIBLE);
					// getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					break;
				case R.id.btn_cancel:
					closeKeyboard();
					windowTitleLayout.setVisibility(View.VISIBLE);
					images_ga.setVisibility(View.VISIBLE);
					((LinearLayout) findViewById(R.id.gallery_point_linear)).setVisibility(View.VISIBLE);
					searchLayout.setVisibility(View.GONE);
					// getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					break;
				default:
					break;
			}
			pagerAdapter.cruurentItem = currentPage;

		}
	};

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{

			switch (msg.what)
			{
				case 1:
					initListView(courseTitle);
					bottomLayout.setVisibility(View.VISIBLE);
					break;
				case 2:
					isExit = false;
					break;
				default:
					break;

			}

		}
	};

	private List<Map<String, Object>> testData()
	{

		List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xx", "A：啊啊啊");

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("xx", "B：不不不");

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("xx", "C：不不不错错错");

		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("xx", "D：hahaha");

		mList.add(map);
		mList.add(map1);
		mList.add(map2);
		mList.add(map3);
		return mList;
	}

	@Override
	protected void onStart()
	{
		super.onStart();

	}

	private void init()
	{
		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.four);

		DisplayMetrics dm = Utils.getDisplayMetrics(this);

		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		float currentHight = ((float)(bitmapOrg.getHeight()*screenWidth))/((float)bitmapOrg.getWidth());
		
		images_ga = (GuideGallery) findViewById(R.id.image_wall_gallery);
		images_ga.setImageActivity(this);
		images_ga.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, (int)currentHight));

		SwitherImageAdapter imageAdapter = new SwitherImageAdapter(this);
		images_ga.setAdapter(imageAdapter);
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		LayoutParams params = pointLinear.getLayoutParams();
		params.height = 50;
		params.width = LayoutParams.FILL_PARENT;
		pointLinear.setLayoutParams(params);
		pointLinear.setBackgroundColor(getResources().getColor(R.color.bg_color));
		for (int i = 0; i < 4; i++)
		{
			ImageView pointView = new ImageView(this);
			if (i == 0)
			{
				pointView.setBackgroundResource(R.drawable.feature_point_cur);
			}
			else
				pointView.setBackgroundResource(R.drawable.feature_point);
			pointLinear.addView(pointView);
		}
		images_ga.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				System.out.println(arg2 + "arg2");
				/*
				 * switch (arg2) {
				 * case 0:
				 * uri = Uri.parse("http://www.36939.net/");
				 * intent = new Intent(Intent.ACTION_VIEW, uri);
				 * startActivity(intent);
				 * break;
				 * case 1:
				 * uri = Uri.parse("http://www.jiqunejia.com/default.aspx");
				 * intent = new Intent(Intent.ACTION_VIEW, uri);
				 * startActivity(intent);
				 * break;
				 * case 2:
				 * uri = Uri.parse("http://www.jiqunejia.tv/");
				 * intent = new Intent(Intent.ACTION_VIEW, uri);
				 * startActivity(intent);
				 * break;
				 * case 3:
				 * uri = Uri.parse("http://city.4000100006.com/");
				 * intent = new Intent(Intent.ACTION_VIEW, uri);
				 * startActivity(intent);
				 * break;
				 * default:
				 * break;
				 * }
				 */

			}
		});

	}

	public void changePointView(int cur)
	{
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null)
		{
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			positon = cur;
		}
	}

	final Handler autoGalleryHandler = new Handler()
	{
		public void handleMessage(Message message)
		{
			super.handleMessage(message);
			switch (message.what)
			{
				case 1:
					images_ga.setSelection(message.getData().getInt("pos"));
					break;
			}
		}
	};

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		timeFlag = false;
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		timeTaks.timeCondition = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			back();
			return false;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	public void back()
	{
		if (!getSlidingMenu().isMenuShowing())
		{
			if (!isExit)
			{
				isExit = true;
				ToastManager.getInstance(MainActivity.this).showToast("再按一次退出程序");
				mHandler.sendEmptyMessageDelayed(2, 2000);
			}
			else
			{
				ActivityTaskManager.getInstance().closeAllActivity();
				Utils.killProcess(getApplicationContext());
			}
		}
	}

	public class ImageTimerTask extends TimerTask
	{
		public volatile boolean timeCondition = true;

		public void run()
		{
			synchronized (this)
			{
				while (!timeCondition)
				{
					try
					{
						Thread.sleep(5000);
						wait();
					}
					catch (InterruptedException e)
					{
						Thread.interrupted();
					}
				}
			}
			try
			{
				gallerypisition = images_ga.getSelectedItemPosition() + 1;
				System.out.println(gallerypisition + "");
				Message msg = new Message();
				Bundle date = new Bundle();// ������
				date.putInt("pos", gallerypisition);
				msg.setData(date);
				msg.what = 1;// ��Ϣ��ʶ
				autoGalleryHandler.sendMessage(msg);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void refreshData(String courseTitle)
	{
		this.courseTitle = courseTitle;
		loadMenu();
		findViews();
		initData();
		initView();
	}

}
