package com.demo.jxdemo.ui.activity.menu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseSlidingActivity;
import com.demo.jxdemo.ui.customviews.CustomDialog;
import com.demo.jxdemo.utils.ToastManager;

public class UserInfoActivity extends BaseSlidingActivity
{
	private RelativeLayout cleanLayout;

	private ImageView iconImg;

	/** 名字 */
	private EditText userNameEditText;

	/** 手机号 */
	private EditText userTelEditText;

	/** 性别 */
	private TextView userGenderTextView;

	/** 地区 */
	private EditText userLocationEditText;

	/** 职业 */
	private EditText userJobEditText;

	/** 个人介绍 */
	private EditText userIntroduceEditText;

	private CustomDialog cDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_userinfo);
		loadMenu();

		findViews();
		initViews();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.left_self));
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.top_left);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.text_right)).setText(getResources().getString(R.string.left_self_save));
		((TextView) findViewById(R.id.text_right)).setVisibility(View.VISIBLE);

		iconImg = (ImageView) findViewById(R.id.img_self_icon);
		userGenderTextView = (TextView) findViewById(R.id.text_gender);
		userNameEditText = (EditText) findViewById(R.id.edit_username);
		userTelEditText = (EditText) findViewById(R.id.edit_usertel);
		userLocationEditText = (EditText) findViewById(R.id.edit_userlocation);
		userJobEditText = (EditText) findViewById(R.id.edit_userjob);
		userIntroduceEditText = (EditText) findViewById(R.id.edit_userintroduce);

		cleanLayout = (RelativeLayout) findViewById(R.id.rlayout_cleancache);
	}

	private void initViews()
	{
		if (!StringUtil.isBlank(SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_GENDER)))
		{
			String gender = SharedPreferencesConfig.config(UserInfoActivity.this).get(Constant.USER_GENDER);
			if ("0".equals(gender))
				userGenderTextView.setText("未设置");
			else if ("1".equals(gender))
				userGenderTextView.setText("男");
			else if ("2".equals(gender))
				userGenderTextView.setText("女");
		}

		((ScrollView) findViewById(R.id.scrollView1)).setVisibility(View.VISIBLE);
	}

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
					closeKeyboard();
					getSlidingMenu().toggle();
					break;
				case R.id.rlayout_cleancache:
					ToastManager.getInstance(UserInfoActivity.this).showToast("清除........");
					break;
				case R.id.layout_remark:
					ToastManager.getInstance(UserInfoActivity.this).showToast("保存.......");
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
			}
		});
		map.put("女", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
				userGenderTextView.setText("女");
			}
		});
		cDialog = new CustomDialog(this, "选择性别", map, "不设置", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
				userGenderTextView.setText("未设置");
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
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
			 * 传到服务器，QQ头像上传采用的方法跟这个类似
			 */

			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			 * byte[] b = stream.toByteArray();
			 * // 将图片流以字符串形式存储下来
			 * tp = new String(Base64Coder.encodeLines(b));
			 * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，
			 * 服务器处理的方法是服务器那边的事了，吼吼
			 * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
			 * 为我们可以用的图片类型就OK啦...吼吼
			 * Bitmap dBitmap = BitmapFactory.decodeFile(tp);
			 * Drawable drawable = new BitmapDrawable(dBitmap);
			 */
			Bitmap photo = extras.getParcelable("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件
			iconImg.setImageBitmap(photo);
		}
	}
}
