package com.demo.jxdemo.ui.customviews;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.base.global.ActivityTaskManager;
import com.demo.base.support.CacheSupport;
import com.demo.base.util.BitmapUtils;
import com.demo.base.util.FileUtils;
import com.demo.base.util.HttpUtils;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.base.util.Utils;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.login.LoginActivity;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.activity.menu.AboutActivity;
import com.demo.jxdemo.ui.activity.menu.OptionActivity;
import com.demo.jxdemo.ui.activity.menu.UserDetailActivity;
import com.demo.jxdemo.ui.activity.menu.UserInfoActivity;
import com.demo.jxdemo.ui.activity.menu.manage.ManageActivity;
import com.demo.jxdemo.ui.customviews.slide.SlidingMenu;
import com.demo.jxdemo.ui.customviews.slide.SlidingMenu.OnClosedListener;
import com.demo.jxdemo.ui.customviews.slide.SlidingMenu.OnOpenListener;
import com.demo.jxdemo.utils.UIUtils;

public class SlidingView
{
	private final Activity activity;

	private SlidingMenu localSlidingMenu;

	private List<Map<String, Object>> courseList;

	private LinearLayout courseLayout;

	private CustomDialog cDialog;

	private Map<String, OnClickListener> map = new HashMap<String, View.OnClickListener>();

	private Map<String, String> configMap;

	private int current = 0;
	
	private ImageView imgUser;

	public SlidingView(Activity activity)
	{
		this.activity = activity;
		configMap = SharedPreferencesConfig.config(activity);
		current = Integer.parseInt(configMap.get(Constant.CURRENT_LEFTMENU));
	}

	public SlidingMenu initSlidingMenu()
	{
		localSlidingMenu = new SlidingMenu(activity);

		Bitmap bitmapOrg = BitmapFactory.decodeResource(activity.getResources(), R.drawable.sidepanebackground);
		DisplayMetrics dm = Utils.getDisplayMetrics(activity);

		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		int screenHight = dm.heightPixels;

		int offset = screenWidth - bitmapOrg.getWidth();
		float currentWidth = 0;
		// if (screenHight < bitmapOrg.getHeight())
		// {
		currentWidth = ((float) bitmapOrg.getWidth()) / (((float) bitmapOrg.getHeight()) / ((float) screenHight));
		offset = (int) (screenWidth - currentWidth);
		// }

		localSlidingMenu.setShadowWidth(50);
		localSlidingMenu.setBehindOffset(offset);
		localSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		localSlidingMenu.setMode(SlidingMenu.LEFT);// 设置左右滑菜单
		// localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// 设置要使菜单滑动，触碰屏幕的范围
		// localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//设置了这个会获取不到菜单里面的焦点，所以先注释掉
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片
//		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
		localSlidingMenu.setFadeDegree(0.35F);// SlidingMenu滑动时的渐变程度
		localSlidingMenu.attachToActivity((Activity) activity, SlidingMenu.RIGHT);// 使SlidingMenu附加在Activity右边
		// localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
		localSlidingMenu.setMenu(R.layout.fragment_left);// 设置menu的布局文件
		// localSlidingMenu.toggle();//动态判断自动关闭或开启SlidingMenu
		
		localSlidingMenu.setOnOpenListener(new OnOpenListener() {
				
				@Override
				public void onOpen() {
					// TODO Auto-generated method stub
					System.out.println("open...");
					closeKeyboard();
				}
			});
		localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener()
		{
			public void onOpened()
			{

			}
		});
		localSlidingMenu.setOnClosedListener(new OnClosedListener()
		{

			@Override
			public void onClosed()
			{
				// TODO Auto-generated method stub

			}
		});

		taskAddView();
		return localSlidingMenu;
	}
	
	public void closeKeyboard()
	{
		if (activity.getCurrentFocus() != null)
		{
			((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private void taskAddView()
	{

		courseLayout = (LinearLayout) localSlidingMenu.findViewById(R.id.layout_left);

		map.put("确定", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				// ToastManager.getInstance(getActivity()).showToast("第一个.....");
				SharedPreferencesConfig.saveConfig(activity, Constant.CURRENT_LEFTMENU, 0 + "");
				ActivityTaskManager.getInstance().closeAllActivity();
				Intent intent = new Intent();
				intent.setClass(activity, LoginActivity.class);
				activity.startActivity(intent);
			}
		});
		cDialog = new CustomDialog(activity, "确定登出？", map, "取消", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
			}
		});
		
		imgUser = ((ImageView) localSlidingMenu.findViewById(R.id.img_user));
		String userAvatar = SharedPreferencesConfig.config(activity).get(Constant.USER_AVATAR);
		if(!StringUtil.isBlank(userAvatar)){
			 final String serverUrl = CommandConstants.URL_ROOT+userAvatar;
			 final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
			 new Thread(){
			 public void run() {
				 if(HttpUtils.downloadFile(serverUrl,localUrl)){
					 Message msg = new Message();
					 msg.what = 3;
					 msg.obj = localUrl;
					 mHandler.sendMessage(msg);
				 }
			 };
			 }.start();
		}

		initData();
		setViewClick();
	}

	private void initData()
	{
		String course = SharedPreferencesConfig.config(activity).get(Constant.USER_COURSEARRAY);
		courseList = JsonUtil.getList(course);

		mHandler.sendEmptyMessage(1);
	}

	private void setViewClick()
	{
		((ImageView) localSlidingMenu.findViewById(R.id.img_user)).setOnClickListener(new OnClickAvoidForceListener()
		{

			@Override
			public void onClickAvoidForce(View v)
			{
				Intent intent = new Intent();
				intent.setClass(activity, UserDetailActivity.class);
				activity.startActivity(intent);
			}
		});
		((TextView) localSlidingMenu.findViewById(R.id.text_index)).setOnClickListener(onClickAvoidForceListener);
		((TextView) localSlidingMenu.findViewById(R.id.text_manage)).setOnClickListener(onClickAvoidForceListener);
		((TextView) localSlidingMenu.findViewById(R.id.text_self)).setOnClickListener(onClickAvoidForceListener);
		((TextView) localSlidingMenu.findViewById(R.id.text_option)).setOnClickListener(onClickAvoidForceListener);
		((TextView) localSlidingMenu.findViewById(R.id.text_about)).setOnClickListener(onClickAvoidForceListener);
		((TextView) localSlidingMenu.findViewById(R.id.text_logout)).setOnClickListener(onClickAvoidForceListener);

	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			Intent intent = new Intent();
			// onClickBgChange(v.getId());
			SharedPreferencesConfig.saveConfig(activity, Constant.CURRENT_LEFTMENU, v.getId() + "");
			switch (v.getId())
			{
				case R.id.text_index:
					intent.setClass(activity, MainActivity.class);
					intent.putExtra("courseTitle", activity.getResources().getString(R.string.app_name));
					break;
				case R.id.text_manage:
					intent.setClass(activity, ManageActivity.class);
					break;
				case R.id.text_self:
					intent.setClass(activity, UserInfoActivity.class);
					break;
				case R.id.text_option:
					intent.setClass(activity, OptionActivity.class);
					break;
				case R.id.text_about:
					intent.setClass(activity, AboutActivity.class);
					break;
				case R.id.text_logout:
					cDialog.show();
					return;
					// ActivityTaskManager.getInstance().closeAllActivity();
					// intent.setClass(activity, LoginActivity.class);
				default:
					break;

			}
			ActivityTaskManager.getInstance().closeAllActivity();
			activity.startActivity(intent);
		}
	};

	private void initView()
	{
		for (int i = 0; i < courseList.size(); i++)
		{
			LinearLayout layout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.layout_left_course, null);
			final TextView textView = (TextView) layout.findViewById(R.id.text_course);
			textView.setText(StringUtil.Object2String(courseList.get(i).get("Title")));
			textView.setId(i + 1); // 设置id
			Drawable db = null;
			if (textView.getText().toString().contains("口语"))
			{
				db = activity.getResources().getDrawable(R.drawable.iconspokenmin);

			}
			else if (textView.getText().toString().contains("写作"))
			{
				db = activity.getResources().getDrawable(R.drawable.iconwritingmin);
			}
			if (db != null)
			{
				db.setBounds(0, 0, db.getMinimumWidth(), db.getMinimumHeight());
				textView.setCompoundDrawables(db, null, null, null);
			}
			textView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// onClickBgChange(v.getId());
					// v.setBackgroundResource(R.color.transparent_white_30);
					SharedPreferencesConfig.saveConfig(activity, Constant.CURRENT_LEFTMENU, v.getId() + "");
					Intent intent = new Intent();
					intent.setClass(activity, MainActivity.class);
					intent.putExtra("courseTitle", textView.getText().toString());
					ActivityTaskManager.getInstance().closeAllActivity();
					activity.startActivity(intent);
				}
			});
			if (i == courseList.size() - 1)
			{
				ImageView imageView = (ImageView) layout.findViewById(R.id.img_course);
				imageView.setVisibility(View.GONE);
			}
			courseLayout.addView(layout);

			// 动态设置drawableLeft图标，（先下载到本地缓存，再读取本地缓存）
			// TODO
			 String iconUrlStr = StringUtil.Object2String(courseList.get(i).get("Icon"));
			 final String serverUrl = CommandConstants.URL_ROOT+iconUrlStr;
			 final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
			 new Thread(){
				 public void run() {
				if(HttpUtils.downloadFile(serverUrl,localUrl)){
						 Message msg = new Message();
						 msg.what = 2;
						 msg.obj = localUrl;
						 msg.arg1 = textView.getId();
						 mHandler.sendMessage(msg);
				 }
			 };
			 }.start();
		}

		if (current == 0)
		{
			((TextView) localSlidingMenu.findViewById(R.id.text_index)).setBackgroundResource(R.color.transparent_white_30);
		}
		else
		{
			((TextView) localSlidingMenu.findViewById(current)).setBackgroundResource(R.color.transparent_white_30);
		}
	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					((TextView) localSlidingMenu.findViewById(R.id.text_user)).setText(configMap.get(Constant.USER_NAME));
					if (courseList != null)
						initView();
					break;
				case 2:
					// 下载成功
					// （file转bitmap转Drawable）
					Drawable db = FileUtils.imgPathToDrawable(msg.obj.toString(), activity, UIUtils.dip2px(activity, 34),
							UIUtils.dip2px(activity, 34));
					if (db != null)
					{
						db.setBounds(0, 0, db.getMinimumWidth(), db.getMinimumHeight());
						((TextView) (courseLayout.findViewById(msg.arg1))).setCompoundDrawables(db, null, null, null);
					}
					break;
				case 3:
					Bitmap photo = FileUtils.getBitmapByimgPath(msg.obj.toString());
					if(photo != null){
						photo = BitmapUtils.toRoundBitmap(photo);
						imgUser.setImageBitmap(photo);
					}
					
					break;
				default:
					break;
			}
		}
	};
}
