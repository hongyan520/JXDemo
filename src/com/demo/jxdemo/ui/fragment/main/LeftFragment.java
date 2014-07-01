package com.demo.jxdemo.ui.fragment.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.base.global.ActivityTaskManager;
import com.demo.base.support.BaseConstants;
import com.demo.base.support.CacheSupport;
import com.demo.base.util.FileUtils;
import com.demo.base.util.HttpUtils;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
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
import com.demo.jxdemo.ui.customviews.CustomDialog;
import com.demo.jxdemo.ui.fragment.BaseFragment;
import com.demo.jxdemo.utils.UIUtils;

public class LeftFragment extends BaseFragment
{
	private List<Map<String, Object>> courseList;

	private LinearLayout courseLayout;

	private CustomDialog cDialog;

	private Map<String, OnClickListener> map = new HashMap<String, View.OnClickListener>();

	private Map<String, String> configMap;

	private int current = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO 可以重写该方法对数据加载时dialog的一些属性进行设置
		configMap = SharedPreferencesConfig.config(getActivity());
		current = Integer.parseInt(configMap.get(Constant.CURRENT_LEFTMENU));
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Object onCreateTaskLoadData(Object... params)
	{
		// TODO 在这里做相关的数据耗时操作
		return null;
	}

	@Override
	protected Object onEndTaskAddView(Object result)
	{
		// TODO 界面和数据的适配操作

		if (getView() == null)
		{
			return null;
		}
		((ViewGroup) getView()).removeAllViews();
		((ViewGroup) getView()).addView(inflater.inflate(R.layout.fragment_left, null));
		courseLayout = (LinearLayout) getActivity().findViewById(R.id.layout_left);
		// Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.sidepanebackground);
		// BitmapUtils.scaleBitmap(bitmapOrg, getActivity());
		// ((LinearLayout)getActivity().findViewById(R.id.layout_leftfragment)).setBackgroundDrawable(BitmapUtils.scaleBitmap(bitmapOrg, getActivity()));
		//showProgress();

		map.put("确定", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				// ToastManager.getInstance(getActivity()).showToast("第一个.....");
				SharedPreferencesConfig.saveConfig(getActivity(), Constant.CURRENT_LEFTMENU, 0 + "");
				ActivityTaskManager.getInstance().closeAllActivity();
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});
		cDialog = new CustomDialog(getActivity(), "确定登出？", map, "取消", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
			}
		});

		initData();
		setViewClick();

		return null;
	}

	private void initData()
	{
		String course = SharedPreferencesConfig.config(getActivity()).get(Constant.USER_COURSEARRAY);
		courseList = JsonUtil.getList(course);

		mHandler.sendEmptyMessage(1);
	}

	private void setViewClick()
	{
		((ImageView) getActivity().findViewById(R.id.img_user)).setOnClickListener(new OnClickAvoidForceListener()
		{

			@Override
			public void onClickAvoidForce(View v)
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), UserDetailActivity.class);
				startActivity(intent);
			}
		});
		((TextView) getActivity().findViewById(R.id.text_index)).setOnClickListener(onClickAvoidForceListener);
		// ((TextView) getActivity().findViewById(R.id.text_speak)).setOnClickListener(onClickAvoidForceListener);
		// ((TextView) getActivity().findViewById(R.id.text_write)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_manage)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_self)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_option)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_about)).setOnClickListener(onClickAvoidForceListener);
		((TextView) getActivity().findViewById(R.id.text_logout)).setOnClickListener(onClickAvoidForceListener);

		// ((TextView) getActivity().findViewById(R.id.text_index)).setOnTouchListener(onTouchListener);
		// // ((TextView) getActivity().findViewById(R.id.text_speak)).setOnTouchListener(onTouchListener);
		// // ((TextView) getActivity().findViewById(R.id.text_write)).setOnTouchListener(onTouchListener);
		// ((TextView) getActivity().findViewById(R.id.text_manage)).setOnTouchListener(onTouchListener);
		// ((TextView) getActivity().findViewById(R.id.text_self)).setOnTouchListener(onTouchListener);
		// ((TextView) getActivity().findViewById(R.id.text_option)).setOnTouchListener(onTouchListener);
		// ((TextView) getActivity().findViewById(R.id.text_about)).setOnTouchListener(onTouchListener);
		// ((TextView) getActivity().findViewById(R.id.text_logout)).setOnTouchListener(onTouchListener);

	}

	private void initView()
	{
		for (int i = 0; i < courseList.size(); i++)
		{
			LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_left_course, null);
			final TextView textView = (TextView) layout.findViewById(R.id.text_course);
			textView.setText(StringUtil.Object2String(courseList.get(i).get("Title")));
			textView.setId(i + 1); // 设置id
			Drawable db = null;
			if(textView.getText().toString().contains("口语")){
				db = getActivity().getResources().getDrawable(R.drawable.iconspokenmin);
				
			}else if(textView.getText().toString().contains("写作")){
				db = getActivity().getResources().getDrawable(R.drawable.iconwritingmin);
			}
			if(db != null){
				db.setBounds(0, 0, db.getMinimumWidth(), db.getMinimumHeight());
				textView.setCompoundDrawables(db,null,null,null);
			}
			textView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// onClickBgChange(v.getId());
					// v.setBackgroundResource(R.color.transparent_white_30);
					SharedPreferencesConfig.saveConfig(getActivity(), Constant.CURRENT_LEFTMENU, v.getId() + "");
					Intent intent = new Intent();
					intent.setClass(getActivity(), MainActivity.class);
					intent.putExtra("courseTitle", textView.getText().toString());
					ActivityTaskManager.getInstance().closeAllActivity();
					startActivity(intent);
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
//			String iconUrlStr = StringUtil.Object2String(courseList.get(i).get("Icon"));
//			final String serverUrl = CommandConstants.URL_ROOT+iconUrlStr;
//			final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
//			new Thread(){
//				public void run() {
//					if(HttpUtils.downloadFile(serverUrl,localUrl)){
//						Message msg = new Message();
//						msg.what = 2;
//						msg.obj = localUrl;
//						msg.arg1 = textView.getId();
//						mHandler.sendMessage(msg);
//					}
//				};
//			}.start();
		}

		if (current == 0)
		{
			((TextView) getActivity().findViewById(R.id.text_index)).setBackgroundResource(R.color.transparent_white_30);
		}
		else
		{
			((TextView) getActivity().findViewById(current)).setBackgroundResource(R.color.transparent_white_30);
		}
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			Intent intent = new Intent();
			// onClickBgChange(v.getId());
			SharedPreferencesConfig.saveConfig(getActivity(), Constant.CURRENT_LEFTMENU, v.getId() + "");
			switch (v.getId())
			{
				case R.id.text_index:
					intent.setClass(getActivity(), MainActivity.class);
					intent.putExtra("courseTitle", getResources().getString(R.string.app_name));
					break;
				// case R.id.text_speak:
				// intent.setClass(getActivity(), MainActivity.class);
				// intent.putExtra("type", 1);
				// break;
				// case R.id.text_write:
				// intent.setClass(getActivity(), MainActivity.class);
				// intent.putExtra("type", 2);
				// break;
				case R.id.text_manage:
					intent.setClass(getActivity(), ManageActivity.class);
					break;
				case R.id.text_self:
					intent.setClass(getActivity(), UserInfoActivity.class);
					break;
				case R.id.text_option:
					intent.setClass(getActivity(), OptionActivity.class);
					break;
				case R.id.text_about:
					intent.setClass(getActivity(), AboutActivity.class);
					break;
				case R.id.text_logout:
					cDialog.show();
					return;
					// ActivityTaskManager.getInstance().closeAllActivity();
					// intent.setClass(getActivity(), LoginActivity.class);
				default:
					break;

			}
			ActivityTaskManager.getInstance().closeAllActivity();
			startActivity(intent);
		}
	};

	// private void onClickBgChange(int id)
	// {
	// ((TextView) getActivity().findViewById(id)).setBackgroundResource(R.color.transparent_white_30);
	// }
	
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					closeProgress();
					if (courseList != null)
						initView();
					break;
				case 2:
					// 下载成功
					//（file转bitmap转Drawable）
					Drawable db = FileUtils.imgPathToDrawable(msg.obj.toString(), getActivity(),UIUtils.dip2px(getActivity(), 34),UIUtils.dip2px(getActivity(), 34));
					if(db != null){
						db.setBounds(0, 0, db.getMinimumWidth(), db.getMinimumHeight());
						((TextView)(courseLayout.findViewById(msg.arg1))).setCompoundDrawables(db,null,null,null);
					}
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		if (!hidden)
		{
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	protected void onBaseResume()
	{
		// TODO Auto-generated method stub
		super.onBaseResume();
	}

}
