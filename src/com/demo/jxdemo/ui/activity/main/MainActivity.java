package com.demo.jxdemo.ui.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.base.global.ActivityTaskManager;
import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.base.util.Utils;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;
import com.demo.jxdemo.ui.adapter.MainListAdapter;
import com.demo.jxdemo.ui.adapter.MainTabPagerAdapter;
import com.demo.jxdemo.ui.adapter.SwitherImageAdapter;
import com.demo.jxdemo.ui.customviews.GuideGallery;
import com.demo.jxdemo.ui.customviews.MyViewPager;
import com.demo.jxdemo.utils.ToastManager;
import com.demo.jxdemo.utils.UIUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

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

	private PullToRefreshScrollView csrcoll;

	private TextView tvRefresh;

	private ScrollView mScrollView;

	private int pageSize = 10;

	private MainListAdapter mainListAdapter = null;

	private List<Map<String, Object>> myList;

	private List<Map<String, Object>> mOrderResult = new ArrayList<Map<String, Object>>();

	public static int offset; // 间隔

	public static int cursorWidth; // 游标的长度

	public static ImageView cursor = null;

	public static int cruurentItem = 0;

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
		csrcoll = (PullToRefreshScrollView) findViewById(R.id.customscrollview);
		csrcoll.setMode(Mode.BOTH); // 设置模式，只允许顶部下拉PULL_FROM_START
		csrcoll.setOnRefreshListener(new OnRefreshListener<ScrollView>()
		{

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				String currentMode = refreshView.getCurrentMode().toString();
				System.out.println("onRefresh====" + currentMode);
				if (Mode.PULL_FROM_START.equals(currentMode))
				{
					// 顶部下拉
					new GetDataTask().execute();
				}
				else
				// if (Mode.PULL_FROM_END.equals(currentMode))
				{
					// 底部上啦
					new GetOldDataTask().execute();
				}
			}
		});

		mScrollView = csrcoll.getRefreshableView();
		// csrcoll.setOnTouchListener((OnTouchListener) new TouchListenerImpl());
		// tvRefresh = (TextView) findViewById(R.id.tv_refresh);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]>
	{

		@Override
		protected String[] doInBackground(Void... params)
		{
			// Simulates a background job.
			try
			{
				// Thread.sleep(4000);
				mOrderResult = getMyListData(tabList, 0, true);
				mainListAdapter.setDataList(mOrderResult);
			}
			catch (Exception e)
			{
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result)
		{
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			csrcoll.onRefreshComplete();
			mainListAdapter.notifyDataSetChanged();
			// 动态设置ViewPager的高度（根据listView的数据量计算）
			autoChangeViewPagerHeight(viewPager, listView);
			super.onPostExecute(result);
		}
	}

	private class GetOldDataTask extends AsyncTask<Void, Void, String[]>
	{
		boolean isNoMore = false;

		@Override
		protected String[] doInBackground(Void... params)
		{
			// Simulates a background job.
			try
			{
				List<Map<String, Object>> my = getMyListData(tabList, mainListAdapter.getCount(), false);
				if (my != null && my.size() > 0)
					mOrderResult.addAll(my);
				else
					isNoMore = true;

			}
			catch (Exception e)
			{
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result)
		{
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			csrcoll.onRefreshComplete();
			if (isNoMore)
				((LinearLayout) findViewById(R.id.layout_nomore)).setVisibility(View.VISIBLE);
			mainListAdapter.notifyDataSetChanged();
			// 动态设置ViewPager的高度（根据listView的数据量计算）
			autoChangeViewPagerHeight(viewPager, listView);
			super.onPostExecute(result);
		}
	}

	// private class TouchListenerImpl implements OnTouchListener{
	// @Override
	// public boolean onTouch(View view, MotionEvent motionEvent) {
	// switch (motionEvent.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	//
	// break;
	// case MotionEvent.ACTION_CANCEL:
	// System.out.println("ACTION_CANCEL。。");
	// break;
	// case MotionEvent.ACTION_MOVE:
	// int scrollY=view.getScrollY();
	// int height=view.getHeight();
	// int scrollViewMeasuredHeight=csrcoll.getChildAt(0).getMeasuredHeight();
	// if(scrollY>=0){
	// System.out.println("滑动到了顶端 view.getScrollY()="+scrollY);
	// // if(View.VISIBLE == tvRefresh.getVisibility()){
	// // tvRefresh.setVisibility(View.GONE);
	// // }
	//
	// }else if(scrollY < 0 && scrollY > -120){
	// System.out.println("下拉刷新 view.getScrollY()="+scrollY);
	// if(View.GONE == tvRefresh.getVisibility()){
	// tvRefresh.setVisibility(View.VISIBLE);
	// }
	// if(View.VISIBLE == tvRefresh.getVisibility()){
	// tvRefresh.setText("下拉刷新");
	// }
	// } else if(scrollY < -120){
	// if(View.VISIBLE == tvRefresh.getVisibility()){
	// tvRefresh.setText("松开刷新");
	// }
	// }
	// if((scrollY+height)==scrollViewMeasuredHeight){
	// System.out.println("滑动到了底部 scrollY="+scrollY);
	// System.out.println("滑动到了底部 height="+height);
	// System.out.println("滑动到了底部 scrollViewMeasuredHeight="+scrollViewMeasuredHeight);
	// }
	// break;
	//
	// default:
	// break;
	// }
	// return false;
	// }
	//
	// };

	public void initCursor(int tagNum)
	{
		cursorWidth = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		offset = ((dm.widthPixels / tagNum) - cursorWidth) / 2;

		cursor = (ImageView) findViewById(R.id.ivCursor);
		Matrix matrix = new Matrix();
		matrix.setTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	private void initData()
	{
		String test = SharedPreferencesConfig.config(this).get(Constant.USER_TEST);
		tabList = JsonUtil.getList(test);
		mHandler.sendEmptyMessage(1);
		// request();
	}

	private void request()
	{

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(MainActivity.this).get(Constant.USER_TOKEN));

		new HttpPostAsync(MainActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("CourseList=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(MainActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(MainActivity.this).showToast("连接不上服务器");
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					boolean isSuccess = false;
					if (!mapstr.containsKey(CommandConstants.ERRCODE))
						isSuccess = true;
					String desc = (String) mapstr.get(CommandConstants.ERRCODE);
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						dismissProgress();
						ToastManager.getInstance(MainActivity.this).showToast(desc);
					}
					else
					{
						// 成功后处理
						String data = mapstr.get("Fragments").toString().replace("\"", "\\\"");
						tabList = JsonUtil.getList(data);
						mHandler.sendEmptyMessage(1);
						dismissProgress();
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.LEARNINGFRAGMENTS, parasTemp);

	}

	@SuppressWarnings("unused")
	private List<Map<String, Object>> getMyListData(List<Map<String, Object>> lists, int currentPage, boolean isFirst)
	{
		myList = new ArrayList<Map<String, Object>>();
		if (isFirst)
		{
			for (int i = currentPage; i < lists.size(); i++)
			{
				Map<String, Object> map = lists.get(i);
				myList.add(map);
			}
		}
		else
		{
			if (currentPage + pageSize <= lists.size())
			{
				for (int i = currentPage + 1; i < currentPage + 10; i++)
				{
					Map<String, Object> map = lists.get(i);
					myList.add(map);
				}
			}
			else
			{
				for (int i = currentPage; i <= lists.size(); i++)
				{
					Map<String, Object> map = lists.get(i);
					myList.add(map);
				}
			}
		}
		return myList;
	}

	private void initView()
	{
		initCursor(4);
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
				if (i == 0)
				{
					mainListAdapter = new MainListAdapter(this);
					mOrderResult = getMyListData(tabList, 0, true);
					mainListAdapter.setDataList(mOrderResult);
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
		// 动态设置ViewPager的高度（根据listView的数据量计算）
		autoChangeViewPagerHeight(viewPager, listView);
		pagerAdapter = new MainTabPagerAdapter(this, pagerContents, 4, viewPager);
		viewPager.setOnPageChangeListener(pagerAdapter);

		viewPager.setAdapter(pagerAdapter);
		pagerAdapter.notifyDataSetChanged();

	}

	/**
	 * 动态设置ViewPager的高度（根据listView的数据量计算）
	 * 
	 * @param _viewPager
	 * @param _listView
	 */
	private void autoChangeViewPagerHeight(ViewPager _viewPager, ListView _listView)
	{
		int totalHeight = UIUtils.getTotalHeightofListView(_listView);
		if (totalHeight > 0)
		{
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) _viewPager.getLayoutParams();
			params.height = totalHeight + listView.getAdapter().getCount() * UIUtils.px2dip(this, 40);
			_viewPager.setLayoutParams(params);
		}
	}

	private void setViewClick()
	{
		// btn3.setOnClickListener(onClickAvoidForceListener);
		// btn2.setOnClickListener(onClickAvoidForceListener);
		// btn1.setOnClickListener(onClickAvoidForceListener);
		// btn4.setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClickAvoidForceListener);
		((Button) findViewById(R.id.btn_cancel)).setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{

			int currentPage = cruurentItem;
			// Intent intent = new Intent();
			switch (v.getId())
			{
				case R.id.btn_4:
					pagerAdapter.onPageSelected(3);
					// pagerAdapter.setBtnColor(btn4);
					break;
				case R.id.btn_3:
					pagerAdapter.onPageSelected(2);
					// pagerAdapter.setBtnColor(btn3);
					// intent.setClass(MainActivity.this, LearningMaterialsActivity.class);
					// startActivity(intent);
					break;
				case R.id.btn_2:
					pagerAdapter.onPageSelected(1);
					// pagerAdapter.setBtnColor(btn2);
					// intent.setClass(MainActivity.this, TrainingActivity.class);
					// startActivity(intent);
					break;
				case R.id.btn_1:
					pagerAdapter.onPageSelected(0);
					// pagerAdapter.setBtnColor(btn1);
					break;
				case R.id.layout_return:
					getSlidingMenu().toggle();
					break;
				case R.id.layout_remark:
					windowTitleLayout.setVisibility(View.GONE);
					images_ga.setVisibility(View.GONE);
					((LinearLayout) findViewById(R.id.gallery_point_linear)).setVisibility(View.GONE);
					// //创建一个LayoutParams对象
					// RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					// ViewGroup.LayoutParams.WRAP_CONTENT);
					// //设置android:layout_below属性的值
					// layoutParams.addRule(RelativeLayout.BELOW, R.id.layout_search);
					// ((LinearLayout) findViewById(R.id.layout_tab_bottom)).setLayoutParams(layoutParams);
					searchLayout.setVisibility(View.VISIBLE);
					searchLayout.setFocusable(true);
					searchLayout.setFocusableInTouchMode(true);
					searchLayout.requestFocus();
					// getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					break;
				case R.id.btn_cancel:
					closeKeyboard();
					// 创建一个LayoutParams对象
					// RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					// ViewGroup.LayoutParams.WRAP_CONTENT);
					// //设置android:layout_below属性的值
					// layoutParams2.addRule(RelativeLayout.BELOW, R.id.gallery_point_linear);
					// ((LinearLayout) findViewById(R.id.layout_tab_bottom)).setLayoutParams(layoutParams2);
					windowTitleLayout.setVisibility(View.VISIBLE);
					images_ga.setVisibility(View.VISIBLE);
					((LinearLayout) findViewById(R.id.gallery_point_linear)).setVisibility(View.VISIBLE);
					searchLayout.setVisibility(View.GONE);
					// getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					images_ga.setFocusable(true);
					images_ga.setFocusableInTouchMode(true);
					images_ga.requestFocus();
					break;
				default:
					break;
			}
			cruurentItem = currentPage;

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
		float currentHight = ((float) (bitmapOrg.getHeight() * screenWidth)) / ((float) bitmapOrg.getWidth());

		images_ga = (GuideGallery) findViewById(R.id.image_wall_gallery);
		images_ga.setImageActivity(this);
		images_ga.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, (int) currentHight));

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
				 * switch (arg2) { case 0: uri = Uri.parse("http://www.36939.net/"); intent = new Intent(Intent.ACTION_VIEW, uri); startActivity(intent); break;
				 * case 1: uri = Uri.parse("http://www.jiqunejia.com/default.aspx"); intent = new Intent(Intent.ACTION_VIEW, uri); startActivity(intent); break;
				 * case 2: uri = Uri.parse("http://www.jiqunejia.tv/"); intent = new Intent(Intent.ACTION_VIEW, uri); startActivity(intent); break; case 3: uri
				 * = Uri.parse("http://city.4000100006.com/"); intent = new Intent(Intent.ACTION_VIEW, uri); startActivity(intent); break; default: break; }
				 */

			}
		});

		images_ga.setFocusable(true);
		images_ga.setFocusableInTouchMode(true);
		images_ga.requestFocus();

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
		// if(StringUtil.isBlank(getIntent().getStringExtra("courseTitle"))){ // 为了保证进入了此页面设置当前侧边栏选中
		// SharedPreferencesConfig.saveConfig(getApplicationContext(), Constant.CURRENT_LEFTMENU, 0 + "");
		// }
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

}
