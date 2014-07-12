package com.demo.jxdemo.ui.activity.menu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.demo.base.util.BitmapUtils;
import com.demo.base.util.FileUtils;
import com.demo.base.util.HttpUtils;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseFragmentActivity;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.customviews.CustomDialog;
import com.demo.jxdemo.ui.customviews.SlidingView;
import com.demo.jxdemo.utils.ToastManager;

public class UserInfoActivity extends BaseFragmentActivity
{
	private RelativeLayout cleanLayout;

	private ImageView iconImg;

	/** 名字 */
	private EditText userNameEditText;

	/** 手机号 */
	private EditText userTelEditText;

	/** 性别 */
	private TextView userGenderTextView;

	private TextView userGenderIdTextView;

	/** 地区 */
	private EditText userLocationEditText;

	/** 职业 */
	private EditText userJobEditText;

	/** 个人介绍 */
	private EditText userIntroduceEditText;

	private CustomDialog cDialog = null;

	private String fromLoginString;

	private String userImageStr;

	private TextView tvCacheSize;

	private String cacheRoot = BaseConstants.BASE_CACHE_PATH;

	private String iconImgPath = BaseConstants.BASE_CACHE_PATH + BaseConstants.STATIC_PATH;

	private String iconImgName = "iconimg.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_userinfo);

		fromLoginString = StringUtil.Object2String(getIntent().getStringExtra("fromLogin"));
		findViews();
		initViews(false);
		setViewClick();
	}

	protected void initSlidingMenu()
	{
		side_drawer = new SlidingView(this).initSlidingMenu();
		
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_self));
		if (StringUtil.isBlank(fromLoginString))
		{
			((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
			initSlidingMenu();
		}
		else
		{
			((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.img_back);
			// setBehindContentView(R.layout.slidemenu_back_content); // 后面的布局(机构)
		}

		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.text_right)).setText(getResources().getString(R.string.left_self_save));
		((TextView) findViewById(R.id.text_right)).setVisibility(View.VISIBLE);

		iconImg = (ImageView) findViewById(R.id.img_self_icon);
		userGenderTextView = (TextView) findViewById(R.id.text_gender);
		userGenderIdTextView = (TextView) findViewById(R.id.text_gender_id);
		userNameEditText = (EditText) findViewById(R.id.edit_username);
		userTelEditText = (EditText) findViewById(R.id.edit_usertel);
		userLocationEditText = (EditText) findViewById(R.id.edit_userlocation);
		userJobEditText = (EditText) findViewById(R.id.edit_userjob);
		userIntroduceEditText = (EditText) findViewById(R.id.edit_userintroduce);

		tvCacheSize = (TextView) findViewById(R.id.tv_cache_size);

		cleanLayout = (RelativeLayout) findViewById(R.id.rlayout_cleancache);
	}

	private void initViews(boolean isRequest)
	{
		String gender = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_GENDER);
		String name = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_NAME);
		String tel = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_TEL);
		String location = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_LOCATION);
		String job = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_JOB);
		String introduce = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_INTRODUCTION);

		String userAvatar = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_AVATAR);

		// Bitmap bm = BitmapFactory.decodeFile(iconImgPath+iconImgName);
		// bm = toRoundBitmap(bm);
		// iconImg.setImageBitmap(bm);
		if (!StringUtil.isBlank(userAvatar))
		{
			final String serverUrl = CommandConstants.URL_ROOT + userAvatar;
			final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
			new Thread()
			{
				public void run()
				{
					if (HttpUtils.downloadFile(serverUrl, localUrl))
					{
						Message msg = new Message();
						msg.what = 3;
						msg.obj = localUrl;
						mHandler.sendMessage(msg);
					}
				};
			}.start();

		}

		refreshCacheSize();// 刷新缓存大小

		userGenderIdTextView.setText(gender);
		if ("1".equals(gender))
			userGenderTextView.setText("男");
		else if ("2".equals(gender))
			userGenderTextView.setText("女");
		else
			userGenderTextView.setText("请选择");

		userNameEditText.setText(name);
		userTelEditText.setText(tel.replace(" ", ""));
		userLocationEditText.setText(location);
		userJobEditText.setText(job);
		userIntroduceEditText.setText(introduce);

		userNameEditText.setSelection(name.length());
		userTelEditText.setSelection(tel.length() - 3);
		userLocationEditText.setSelection(location.length());
		userJobEditText.setSelection(job.length());
		userIntroduceEditText.setSelection(introduce.length());

		((ScrollView) findViewById(R.id.scrollView1)).setVisibility(View.VISIBLE);

		if (StringUtil.isBlank(fromLoginString) && isRequest)
		{
			initSlidingMenu();
		}

		// TODO
		if (!isRequest)
			request(isRequest);
	}

	private void refreshCacheSize()
	{
		String cachesize = FileUtils.calculatePathSize(cacheRoot);
		tvCacheSize.setText(cachesize);
	}

	private void request(final boolean isRefreshLeft)
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("UserID", SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_ID));

		new HttpPostAsync(UserInfoActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("UserProfile=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(UserInfoActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(UserInfoActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(UserInfoActivity.this).showToast(desc);
					}
					else
					{
						loadSuccessDeal(mapstr, isRefreshLeft);// 成功后处理
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.USERPROFILE, parasTemp);
	}

	@SuppressWarnings("unchecked")
	private void loadSuccessDeal(Map<String, Object> map, boolean isRefreshLeft)

	{
		new LoadDealTask(isRefreshLeft).execute(map);
	}

	class LoadDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
	{
		boolean isRefreshLeft = false;

		public LoadDealTask(boolean _isRefreshLeft)
		{

			isRefreshLeft = _isRefreshLeft;
		}

		@Override
		protected void onPreExecute()
		{
			showProgress(2 * 60 * 1000);
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{

		}

		@Override
		protected Void doInBackground(Map<String, Object>... map)
		{
			try
			{
				if (map != null)
				{

					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_AVATAR,
							StringUtil.Object2String(map[0].get("Avatar")));
					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_INTRODUCTION,
							StringUtil.Object2String(map[0].get("Introduction")));
					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_JOB,
							StringUtil.Object2String(map[0].get("Job")));
					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_LOCATION,
							StringUtil.Object2String(map[0].get("Location")));
					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_GENDER,
							StringUtil.Object2String(map[0].get("Sex")));
					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_NAME,
							StringUtil.Object2String(map[0].get("Title")));
					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_TYPE,
							StringUtil.Object2String(map[0].get("UserType")));
					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_DIGEST,
							StringUtil.Object2String(map[0].get("Digest")));
					// TODO 课程待测
					SharedPreferencesConfig.saveConfig(UserInfoActivity.this, Constant.USER_COURSEARRAY,
							StringUtil.Object2String(map[0].get("CourseArray").toString()));

					mHandler.sendEmptyMessage(1);
					dismissProgress();
				}
				else
					ToastManager.getInstance(UserInfoActivity.this).showToast("获取失败!");

			}
			catch (Exception e)
			{
				e.printStackTrace();
				ToastManager.getInstance(UserInfoActivity.this).showToast("获取失败!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			dismissProgress();
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
					initViews(true);
					break;
				case 2:
					// 保存成功
					if (!StringUtil.isBlank(fromLoginString))
					{
						// 跳转到首页
						Intent intent = new Intent();
						intent.setClass(UserInfoActivity.this, MainActivity.class);
						ActivityTaskManager.getInstance().closeAllActivity();
						startActivity(intent);
					}
					break;
				case 3:
					// 下载成功
					// （file转bitmap转Drawable）
					// Drawable db = FileUtils.imgPathToDrawable(msg.obj.toString(), UserInfoActivity.this,0,0);
					// if(db != null){
					// iconImg.setBackgroundDrawable(db);
					// }
					Bitmap photo = FileUtils.getBitmapByimgPath(msg.obj.toString());
					if (photo != null)
					{
						photo = BitmapUtils.toRoundBitmap(photo);
						iconImg.setImageBitmap(photo);
					}

					break;
				default:
					break;
			}
		}
	};

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		cleanLayout.setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClickAvoidForceListener);
		iconImg.setOnClickListener(onClickAvoidForceListener);
		userGenderTextView.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.layout_return:
					if (StringUtil.isBlank(fromLoginString))
					{
						closeKeyboard();
						if (side_drawer.isMenuShowing())
						{
							side_drawer.showContent();
						}
						else
						{
							side_drawer.showMenu();
						}
					}
					else
						finish();
					break;
				case R.id.rlayout_cleancache:
					ToastManager.getInstance(UserInfoActivity.this).showToast("清除成功");
					FileUtils.deleteFilesByPath(cacheRoot, false);
					refreshCacheSize();// 刷新缓存大小
					break;
				case R.id.layout_remark:
					saveInfo();
					// ToastManager.getInstance(UserInfoActivity.this).showToast("保存.......");
					// if (!StringUtil.isBlank(userTelEditText.getText().toString().trim()))
					// saveInfo();
					// else
					// ToastManager.getInstance(UserInfoActivity.this).showToast("请填写手机号码!");
					break;
				case R.id.img_self_icon:
					ShowPickDialog();
					break;
				case R.id.text_gender:
					initGender();
					break;
				default:
					break;
			}
		}
	};

	private void saveInfo()
	{
		if (StringUtil.isBlank(userNameEditText.getText().toString()))
		{
			ToastManager.getInstance(UserInfoActivity.this).showToast("请输入名字");
			return;
		}
		if (StringUtil.isBlank(userTelEditText.getText().toString()))
		{
			ToastManager.getInstance(UserInfoActivity.this).showToast("请输入手机号");
			return;
		}
		showProgress(5 * 60 * 1000);
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_TOKEN));
		// parasTemp.put("Avatar", userImageStr);
		parasTemp.put("Title", userNameEditText.getText().toString());
		parasTemp.put("PhoneNumber", userTelEditText.getText().toString());
		parasTemp.put("AuthCode", "");// 验证码 当手机号码没有修改的时候,服务器端忽略此项。
		parasTemp.put("Location", userLocationEditText.getText().toString());
		parasTemp.put("Job", userJobEditText.getText().toString());
		parasTemp.put("Introduction", userIntroduceEditText.getText().toString());
		// 整数，代表性别。 0 －未设置 1 － 男 2 － 女
		parasTemp.put("Sex", userGenderIdTextView.getText().toString());

		Map<String, File> paramsFile = new HashMap<String, File>();

		paramsFile.put("Avatar", new File(iconImgPath + iconImgName));

		new HttpPostAsync(UserInfoActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("UpdateUserProfile=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(UserInfoActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(UserInfoActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(UserInfoActivity.this).showToast(desc);
					}
					else
					{
						// 成功后处理
						request(true);
						ToastManager.getInstance(UserInfoActivity.this).showToast("保存成功!");
						mHandler.sendEmptyMessage(2);
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_FILES_DATA, CommandConstants.URL + CommandConstants.UPDATEUSERPROFILE, parasTemp, paramsFile);

	}

	private void initGender()
	{
		Map<String, OnClickListener> map = new HashMap<String, View.OnClickListener>();
		map.put("男", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
				userGenderTextView.setText("男");
				userGenderIdTextView.setText("1");
			}
		});
		map.put("女", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
				userGenderTextView.setText("女");
				userGenderIdTextView.setText("2");
			}
		});
		cDialog = new CustomDialog(this, "选择性别", map, "取消", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
				userGenderTextView.setText("请选择");
				userGenderIdTextView.setText("0");
			}
		});
		cDialog.show();
	}

	private void ShowPickDialog()
	{
		Map<String, OnClickListener> map = new HashMap<String, View.OnClickListener>();
		map.put("拍照", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "gixueIcon.jpg")));
				startActivityForResult(intent, 2);

			}
		});
		map.put("从手机相册选择", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 1);
			}
		});
		cDialog = new CustomDialog(this, "设置头像", map, "取消", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
			}
		});
		cDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		// 如果是直接从相册获取
			case 1:
				if (data != null)
					startPhotoZoom(data.getData());
				break;
			// 如果是调用相机拍照时
			case 2:
				File temp = new File(Environment.getExternalStorageDirectory() + "/gixueIcon.jpg");
				startPhotoZoom(Uri.fromFile(temp));
				break;
			// 取得裁剪后的图片
			case 3:
				if (data != null)
				{
					setPicToView(data);
				}
				break;
			default:
				break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata)
	{
		Bundle extras = picdata.getExtras();
		if (extras != null)
		{

			/**
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
			 */

			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream(); photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); byte[] b =
			 * stream.toByteArray(); // 将图片流以字符串形式存储下来 tp = new String(Base64Coder.encodeLines(b)); 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼
			 * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼 Bitmap dBitmap = BitmapFactory.decodeFile(tp); Drawable drawable = new
			 * BitmapDrawable(dBitmap);
			 */
			Bitmap photo = extras.getParcelable("data");

			if (photo == null)
			{
				return;
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件
			photo = BitmapUtils.toRoundBitmap(photo);
			iconImg.setImageBitmap(photo);
			BitmapUtils.saveBitmap2file(photo, iconImgPath, iconImgName);
			userImageStr = stream.toString();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{// 屏幕返回键处理
			if (!StringUtil.isBlank(fromLoginString))
			{
				finish();
			}
			else
				return super.onKeyDown(keyCode, event);
		}
		return false;
	}

}
