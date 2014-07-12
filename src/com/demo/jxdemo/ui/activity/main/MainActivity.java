package com.demo.jxdemo.ui.activity.main;

import java.io.Serializable;
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
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.base.global.ActivityTaskManager;
import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.support.CacheSupport;
import com.demo.base.util.FileUtils;
import com.demo.base.util.HttpUtils;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.base.util.Utils;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseFragmentActivity;
import com.demo.jxdemo.ui.activity.menu.ViewWebViewActivity;
import com.demo.jxdemo.ui.adapter.SwitherImageAdapter;
import com.demo.jxdemo.ui.adapter.main.FragmentPagerListAdapter;
import com.demo.jxdemo.ui.customviews.GuideGallery;
import com.demo.jxdemo.ui.customviews.SlidingView;
import com.demo.jxdemo.ui.fragment.main.AllFragment;
import com.demo.jxdemo.ui.fragment.main.CollectionFragment;
import com.demo.jxdemo.ui.fragment.main.LearningFragment;
import com.demo.jxdemo.ui.fragment.main.TraningFragment;
import com.demo.jxdemo.utils.ToastManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class MainActivity extends BaseFragmentActivity
{
	private RelativeLayout windowTitleLayout;

	private LinearLayout searchLayout;

	private EditText searchEditText;

	private LinearLayout bottomLayout;

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

	public static ImageView cursor = null;

	public static int cursorWidth; // 游标的长度

	public static float offset; // 间隔

	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	public static ViewPager mViewPager;

	private List<Map<String, Object>> tabList;

	private PullToRefreshScrollView csrcoll;

	private TextView tvRefresh;

	private ScrollView mScrollView;

	public Button btn1;

	public Button btn2;

	public Button btn3;

	public Button btn4;
	
	private int bmpX_target = 0;
	private int bmpX_Old = 0;

	private List<Map<String, Object>> bannersLists;

	private List<Bitmap> bitmapLists = new ArrayList<Bitmap>();

	private List<String> linkLists = new ArrayList<String>();

	private LinearLayout pointLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);

		courseTitle = getIntent().getStringExtra("courseTitle") == null ? getResources().getString(R.string.app_name) : getIntent()
				.getStringExtra("courseTitle");

		findViews();
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
		request(CommandConstants.LEARNINGFRAGMENTS);
		request(CommandConstants.BANNERS);
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
		//
		// SwitherImageAdapter imageAdapter = new SwitherImageAdapter(this);
		// imageAdapter.setImg(bitmapLists);
		// images_ga.setAdapter(imageAdapter);
		pointLayout = (LinearLayout) findViewById(R.id.gallery_point_linear);
		LayoutParams params = pointLayout.getLayoutParams();
		params.height = 50;
		params.width = LayoutParams.FILL_PARENT;
		pointLayout.setLayoutParams(params);
		pointLayout.setBackgroundColor(getResources().getColor(R.color.bg_color));
		pointLayout.removeAllViews();
		// for (int i = 0; i < 4; i++)
		// {
		// ImageView pointView = new ImageView(this);
		// if (i == 0)
		// {
		// pointView.setBackgroundResource(R.drawable.feature_point_cur);
		// }
		// else
		// pointView.setBackgroundResource(R.drawable.feature_point);
		// pointLayout.addView(pointView);
		// }
		// images_ga.setOnItemClickListener(new OnItemClickListener()
		// {
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		// {
		// System.out.println(arg2 + "arg2");
		// }
		// });

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

	protected void initSlidingMenu()
	{
		side_drawer = new SlidingView(this).initSlidingMenu();
	}

	private void findViews()
	{
		mViewPager = (ViewPager) findViewById(R.id.vpViewPager1);
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
				// mOrderResult = getMyListData(tabList, 0, true);
				// mainAllListAdapter.setDataList(mOrderResult);
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
			// mainAllListAdapter.notifyDataSetChanged();
			// // 动态设置ViewPager的高度（根据listView的数据量计算）
			// autoChangeViewPagerHeight(viewPager, listView);
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
				// List<Map<String, Object>> my = getMyListData(tabList, mainAllListAdapter.getCount(), false);
				// if (my != null && my.size() > 0)
				// mOrderResult.addAll(my);
				// else
				// isNoMore = true;

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
			// if (isNoMore)
			// ((LinearLayout) findViewById(R.id.layout_nomore)).setVisibility(View.VISIBLE);
			// mainAllListAdapter.notifyDataSetChanged();
			// // 动态设置ViewPager的高度（根据listView的数据量计算）
			// autoChangeViewPagerHeight(viewPager, listView);
			super.onPostExecute(result);
		}
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClicks);
		((Button) findViewById(R.id.btn_cancel)).setOnClickListener(onClicks);
		btn1.setOnClickListener(onClicks);
		btn2.setOnClickListener(onClicks);
		btn3.setOnClickListener(onClicks);
		btn4.setOnClickListener(onClicks);
	}

	private OnClickAvoidForceListener onClicks = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{

			// int currentPage = cruurentItem;
			// // Intent intent = new Intent();
			switch (v.getId())
			{
				case R.id.btn_4:
					// pagerAdapter.onPageSelected(3);
					// pagerAdapter.setBtnColor(btn4);
//					ToastManager.getInstance(MainActivity.this).showToast("收藏");
					mViewPager.setCurrentItem(3);
					break;
				case R.id.btn_3:
					// pagerAdapter.onPageSelected(2);
					// pagerAdapter.setBtnColor(btn3);
					// intent.setClass(MainActivity.this, LearningMaterialsActivity.class);
					// startActivity(intent);
//					ToastManager.getInstance(MainActivity.this).showToast("资料");
					mViewPager.setCurrentItem(2);
					break;
				case R.id.btn_2:
					// pagerAdapter.onPageSelected(1);
					// pagerAdapter.setBtnColor(btn2);
					// intent.setClass(MainActivity.this, TrainingActivity.class);
					// startActivity(intent);
//					ToastManager.getInstance(MainActivity.this).showToast("训练");
					mViewPager.setCurrentItem(1);
					break;
				case R.id.btn_1:
					// pagerAdapter.onPageSelected(0);
					// pagerAdapter.setBtnColor(btn1);
//					ToastManager.getInstance(MainActivity.this).showToast("全部");
					mViewPager.setCurrentItem(0);
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
			// cruurentItem = currentPage;

		}
	};

	private int bmpWidth;

	/** 初始化layout控件 */
	private void initView()
	{
		// setChangelView();
		((TextView) findViewById(R.id.formTilte)).setText(courseTitle);
		initCursor(4);
		initSlidingMenu();
	}

	// /**
	// * 当栏目项发生变化时候调用
	// */
	// private void setChangelView()
	// {
	// initFragment();
	// }

	public void initCursor(int tagNum)
	{
		cursor = (ImageView) findViewById(R.id.ivCursor);
		cursorWidth = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		bmpWidth = screenW / tagNum;
		offset = (float) (screenW / tagNum / cursorWidth);// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postScale(offset, 1);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
		
//		offset = ((dm.widthPixels / tagNum) - cursorWidth) / 2;
//		cursor = (ImageView) findViewById(R.id.ivCursor);
//		Matrix matrix = new Matrix();
//		matrix.setTranslate(offset, 0);
//		cursor.setImageMatrix(matrix);
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment()
	{
		fragments.clear();// 清空

		Bundle data = new Bundle();
		for (int i = 1; i <= 4; i++)
		{
			if (i == 1)
			{
				AllFragment fragment = new AllFragment();
				data.putSerializable("lists", (Serializable) tabList);
				fragment.setArguments(data);
				fragments.add(fragment);
			}
			else if (i == 2)
			{
				TraningFragment fragment = new TraningFragment();
				data.putSerializable("lists", (Serializable) tabList);
				fragment.setArguments(data);
				fragments.add(fragment);
			}
			else if (i == 3)
			{
				LearningFragment fragment = new LearningFragment();
				data.putSerializable("lists", (Serializable) tabList);
				fragment.setArguments(data);
				fragments.add(fragment);
			}
			else
			{
				CollectionFragment fragment = new CollectionFragment();
				data.putSerializable("lists", (Serializable) tabList);
				fragment.setArguments(data);
				fragments.add(fragment);
			}
		}

		FragmentPagerListAdapter mAdapetr = new FragmentPagerListAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);

	}

	public OnPageChangeListener pageListener = new OnPageChangeListener()
	{

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			System.out.println("onPageScrollStateChanged="+arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageSelected(int position)
		{
			// TODO Auto-generated method stub
			mViewPager.setCurrentItem(position);
			System.out.println("onPageSelected="+position);
			Animation animation = null;
			switch (position) {
			case 0:// 全部
				bmpX_target = 0;
				btn1.setTextColor(getResources().getColor(R.color.red));
				btn2.setTextColor(R.style.textview_gray16_717171);
				btn3.setTextColor(R.style.textview_gray16_717171);
				btn4.setTextColor(R.style.textview_gray16_717171);
				break;
			case 1:// 训练
				bmpX_target = bmpWidth;
				btn1.setTextColor(R.style.textview_gray16_717171);
				btn2.setTextColor(getResources().getColor(R.color.red));
				btn3.setTextColor(R.style.textview_gray16_717171);
				btn4.setTextColor(R.style.textview_gray16_717171);
				break;
			case 2:// 资料
				bmpX_target = 2 * bmpWidth;
				btn1.setTextColor(R.style.textview_gray16_717171);
				btn2.setTextColor(R.style.textview_gray16_717171);
				btn3.setTextColor(getResources().getColor(R.color.red));
				btn4.setTextColor(R.style.textview_gray16_717171);
				break;
			case 3:// 收藏
				bmpX_target = 3 * bmpWidth;
				btn1.setTextColor(R.style.textview_gray16_717171);
				btn2.setTextColor(R.style.textview_gray16_717171);
				btn3.setTextColor(R.style.textview_gray16_717171);
				btn4.setTextColor(getResources().getColor(R.color.red));
				break;
			}
			animation = new TranslateAnimation(bmpX_Old, bmpX_target, 0, 0);
			bmpX_Old = bmpX_target;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
	};

	private void request(final String cmd)
	{
		showProgress(6 * 1000);
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(MainActivity.this).get(Constant.USER_TOKEN));

		new HttpPostAsync(MainActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				// System.out.println("CourseList=" + result);
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
						ToastManager.getInstance(MainActivity.this).showToast(desc);
					}
					else
					{
						// 成功后处理
						if (cmd.equals(CommandConstants.LEARNINGFRAGMENTS))
						{
							String data = mapstr.get("Fragments").toString();// mapstr.get("Fragments").toString().replace("\"", "\\\"");
							SharedPreferencesConfig.saveConfig(MainActivity.this, Constant.USER_TEST, data);
							tabList = JsonUtil.getList(data);
							mHandler.sendEmptyMessage(1);
						}
						else
						{
							bannersLists = JsonUtil.getList(mapstr.get("Banners").toString());
							bitmapLists.clear();
							for (int i = 0; i < bannersLists.size(); i++)
							{
								final String serverUrl = CommandConstants.URL_ROOT + bannersLists.get(i).get("Image");
								final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
								linkLists.add(bannersLists.get(i).get("Link") == null ? "" : bannersLists.get(i).get("Link").toString());
								new Thread()
								{
									public void run()
									{
										if (HttpUtils.downloadFile(serverUrl, localUrl))
										{
											Bitmap photo = FileUtils.getBitmapByimgPath(localUrl);
											if (photo != null)
											{
												bitmapLists.add(photo);
											}
										}
									};
								}.start();
							}
							mHandler.sendEmptyMessage(2);
						}
					}
					dismissProgress();
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + cmd, parasTemp);

	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{

			switch (msg.what)
			{
				case 1:
					initFragment();
					bottomLayout.setVisibility(View.VISIBLE);
					break;
				case 2:
					for (int i = 0; i < bitmapLists.size(); i++)
					{
						ImageView pointView = new ImageView(MainActivity.this);
						if (i == 0)
						{
							pointView.setBackgroundResource(R.drawable.feature_point_cur);
						}
						else
							pointView.setBackgroundResource(R.drawable.feature_point);
						pointLayout.addView(pointView);
					}
					// images_ga.setOnItemClickListener(new OnItemClickListener()
					// {
					// @Override
					// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					// {
					// if (!StringUtil.isBlank(linkLists.get(arg2)))
					// {
					// Intent intent = new Intent();
					// intent.putExtra("url", linkLists.get(arg2));
					// intent.setClass(MainActivity.this, ViewWebViewActivity.class);
					// startActivity(intent);
					// }
					//
					// }
					// });

					// LayoutParams params = pointLayout.getLayoutParams();
					// params.height = 50;
					// params.width = LayoutParams.FILL_PARENT;
					// pointLayout.setLayoutParams(params);
					// pointLayout.setBackgroundColor(getResources().getColor(R.color.bg_color));
					// for (int i = 0; i < bitmapLists.size(); i++)
					// {
					// ImageView pointView = new ImageView(MainActivity.this);
					// if (i == 0)
					// {
					// pointView.setBackgroundResource(R.drawable.feature_point_cur);
					// }
					// else
					// pointView.setBackgroundResource(R.drawable.feature_point);
					// pointLayout.addView(pointView);
					// }
					// images_ga.setOnItemClickListener(new OnItemClickListener()
					// {
					// @Override
					// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					// {
					// if (!StringUtil.isBlank(linkLists.get(arg2)))
					// {
					// Intent intent = new Intent();
					// intent.putExtra("url", linkLists.get(arg2));
					// intent.setClass(MainActivity.this, ViewWebViewActivity.class);
					// startActivity(intent);
					// }
					// }
					// });
					// images_ga.setFocusable(true);
					// images_ga.setFocusableInTouchMode(true);
					// images_ga.requestFocus();

					SwitherImageAdapter imageAdapter = new SwitherImageAdapter(MainActivity.this);
					imageAdapter.setImg(bitmapLists, linkLists);
					images_ga.setAdapter(imageAdapter);
					break;
				default:
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
		if (!side_drawer.isMenuShowing())
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
