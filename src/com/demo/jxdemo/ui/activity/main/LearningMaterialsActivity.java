package com.demo.jxdemo.ui.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.support.CacheSupport;
import com.demo.base.util.DateUtil;
import com.demo.base.util.FileUtils;
import com.demo.base.util.HttpUtils;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.ScrollListViewUtil;
import com.demo.base.util.StringUtil;
import com.demo.base.util.Utils;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.activity.menu.ViewWebViewActivity;
import com.demo.jxdemo.ui.adapter.AttachListAdapter;
import com.demo.jxdemo.utils.ToastManager;

/**
 * 学习资料详细页面
 * 
 * @author Chan
 */
public class LearningMaterialsActivity extends BaseActivity
{
	private TextView colTextView;

	private ListView attachListView;

	private LinearLayout commentLayout;

	private ImageView topImageView;

	private TextView titleTextView;

	private TextView contentTextView;

	private TextView attCountTextView;

	private TextView commentCountTextView;

	private EditText commentEditText;

	private RelativeLayout submitCommentLayout;

	private AttachListAdapter attachAdapter;

	private List<Map<String, Object>> attachLists;

	private Map<String, Object> returnMap;

	private List<Map<String, Object>> commentshLists;

	private String fragmentID;

	private int fav = 0;

	private int commentCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_learningmaterials);

		fragmentID = getIntent().getStringExtra("FragmentID");

		findViews();
		initData();
		// initView();
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

		topImageView = (ImageView) findViewById(R.id.detail_top_image);
		titleTextView = (TextView) findViewById(R.id.text_title);
		contentTextView = (TextView) findViewById(R.id.text_content);
		attCountTextView = (TextView) findViewById(R.id.text_att_count);
		commentCountTextView = (TextView) findViewById(R.id.text_comment_count);
		commentEditText = (EditText) findViewById(R.id.edit_comment);
		submitCommentLayout = (RelativeLayout) findViewById(R.id.rlayout_submit);

		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.four);
		DisplayMetrics dm = Utils.getDisplayMetrics(this);
		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		float currentHight = ((float) (bitmapOrg.getHeight() * screenWidth)) / ((float) bitmapOrg.getWidth());

		topImageView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, (int) currentHight));
	}

	private void initData()
	{
		// Map<String, Object> m1 = new HashMap<String, Object>();
		// m1.put("11", "111");
		// Map<String, Object> m2 = new HashMap<String, Object>();
		// m2.put("11", "111");
		// Map<String, Object> m3 = new HashMap<String, Object>();
		// m3.put("11", "111");
		//
		// lists = new ArrayList<Map<String, Object>>();
		// lists.add(m1);
		// lists.add(m2);
		// lists.add(m3);

		// 请求主信息
		request(CommandConstants.LEARNINGFRAGMENT);
		// 请求评论列表
		request(CommandConstants.LEARNINGFRAGMENTCOMMENTS);
		// 收藏(连接失败?)

	}

	private void request(final String cmd)
	{
		showProgress(4 * 1000);
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(LearningMaterialsActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("FragmentID", fragmentID);

		new HttpPostAsync(LearningMaterialsActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(LearningMaterialsActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(LearningMaterialsActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(LearningMaterialsActivity.this).showToast(desc);
					}
					else
					{
						if (cmd.equals(CommandConstants.LEARNINGFRAGMENT))
							loadSuccessDeal(mapstr, 1);// 成功后处理
						else if (cmd.equals(CommandConstants.LEARNINGFRAGMENTCOMMENTS))
							loadSuccessDeal(mapstr, 2);// 成功后处理
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + cmd, parasTemp);

	}

	/**
	 * 返回数据后处理
	 * 
	 * @param map
	 * @param flag
	 *            1:主信息 2:评论列表
	 */
	@SuppressWarnings("unchecked")
	private void loadSuccessDeal(Map<String, Object> map, int flag)
	{
		new LoadDealTask(flag).execute(map);
	}

	class LoadDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
	{
		int flag = 0;

		public LoadDealTask(int flag)
		{
			this.flag = flag;
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
					if (flag == 1)
					{
						returnMap = map[0];
						if (map[0].get("AttachementArray") != null)
							attachLists = JsonUtil.getList(map[0].get("AttachementArray").toString());
						mHandler.sendEmptyMessage(1);
						if(map[0].get("Banner") != null){
							String serverUrl = CommandConstants.URL_ROOT + map[0].get("Banner").toString();
							String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
							if (HttpUtils.downloadFile(serverUrl, localUrl))
							{
								Message msg = new Message();
								msg.what = 1001;
								msg.obj = localUrl;
								mHandler.sendMessage(msg);	
							}
						}
					}
					else if (flag == 2)
					{
						commentshLists = JsonUtil.getList(map[0].get("Comments").toString());
						mHandler.sendEmptyMessage(3);
					}
					dismissProgress();
				}
				else
					ToastManager.getInstance(LearningMaterialsActivity.this).showToast("获取失败!");

			}
			catch (Exception e)
			{
				e.printStackTrace();
				ToastManager.getInstance(LearningMaterialsActivity.this).showToast("获取失败!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			dismissProgress();
		}

	}

	private void sentComment()
	{
		showProgress(4 * 1000);
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(LearningMaterialsActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("FragmentID", fragmentID);
		parasTemp.put("Content", commentEditText.getText().toString());

		new HttpPostAsync(LearningMaterialsActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(LearningMaterialsActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(LearningMaterialsActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(LearningMaterialsActivity.this).showToast(desc);
					}
					else
					{
						dismissProgress();
						ToastManager.getInstance(LearningMaterialsActivity.this).showToast("评论成功");
						mHandler.sendEmptyMessage(4);
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.COMMENTFRAGMENT, parasTemp);

	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					initView();
					break;
				case 2:
					controlRightImg();
					break;
				case 3:
					initCommentsView();
					break;
				case 4:
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("SenderTitle", SharedPreferencesConfig.config(LearningMaterialsActivity.this).get(Constant.USER_NAME));
					map.put("SentAt", DateUtil.getChinaDateByDateString(DateUtil.getCurrentDateTime()));
					map.put("Content", commentEditText.getText().toString());

					List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
					temp.add(map);
					temp.addAll(commentshLists);
					commentshLists = temp;

					initCommentsView();
					commentEditText.setText("");
					commentCountTextView.setText(commentCount + 1 + "条评论");
					break;
				case 1001:
					// 刷新top图片
					refreshTopImage((String)msg.obj);
					break;
				default:
					break;
			}
		}
	};

	private void controlRightImg()
	{
		ToastManager.getInstance(LearningMaterialsActivity.this).showToast(fav + "");
	}

	private void initView()
	{
		attachAdapter = new AttachListAdapter(LearningMaterialsActivity.this);
		attachAdapter.setDataList(attachLists);
		attachListView.setAdapter(attachAdapter);
		ScrollListViewUtil.setListViewHeightBasedOnChildren(attachListView);

		commentCount = returnMap.get("CommentCount") == null ? 0 : Integer.parseInt(returnMap.get("CommentCount").toString());
		titleTextView.setText(StringUtil.Object2String(returnMap.get("Title")));
		contentTextView.setText(StringUtil.Object2String(returnMap.get("Content")));
		attCountTextView.setText(StringUtil.Object2String(attachLists == null ? "0" : attachLists.size()) + "个附件");
		commentCountTextView.setText(commentCount + "条评论");

		attachListView.setOnItemClickListener(onItemClickAvoidForceListener);

		((ScrollView) findViewById(R.id.scrollView1)).setVisibility(View.VISIBLE);
		
	}
	
	private void refreshTopImage(String localImagePath){
		Bitmap bitmapOrg = FileUtils.getBitmapByimgPath(localImagePath);
		DisplayMetrics dm = Utils.getDisplayMetrics(this);
		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		float currentHight = ((float) (bitmapOrg.getHeight() * screenWidth)) / ((float) bitmapOrg.getWidth());

		topImageView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, (int) currentHight));
		topImageView.setImageBitmap(bitmapOrg);
	}

	private void initCommentsView()
	{
		if (commentshLists != null)
		{
			commentLayout.removeAllViews();
			for (int i = 0; i < commentshLists.size(); i++)
			{
				String name = StringUtil.Object2String(commentshLists.get(i).get("SenderTitle"));

				LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_learningmaterials_item_comment,
						null);
				((TextView) layout.findViewById(R.id.text_title)).setText(name);
				((TextView) layout.findViewById(R.id.text_time)).setText(StringUtil.Object2String(commentshLists.get(i).get("SentAt")));
				((TextView) layout.findViewById(R.id.text_content)).setText(StringUtil.Object2String(commentshLists.get(i).get("Content")));

				if (SharedPreferencesConfig.config(LearningMaterialsActivity.this).get(Constant.USER_NAME).equals(name))
					((Button) layout.findViewById(R.id.btn_right)).setVisibility(View.VISIBLE);
				commentLayout.addView(layout);
			}
		}

	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		topImageView.setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClickAvoidForceListener);
		submitCommentLayout.setOnClickListener(onClickAvoidForceListener);
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
				case R.id.detail_top_image:
					if (returnMap != null && returnMap.get("Link") != null)
					{
						Intent intent = new Intent();
						intent.putExtra("url", returnMap.get("Link").toString());
						intent.setClass(LearningMaterialsActivity.this, ViewWebViewActivity.class);
						startActivity(intent);
					}
					break;
				case R.id.layout_remark:
					toggleFavourite();
					break;
				case R.id.rlayout_submit:
					sentComment();
					break;
				default:
					break;
			}
		}
	};

	private void toggleFavourite()
	{
		String learningFragmentID = "";
		if (returnMap != null && !StringUtil.isBlank(returnMap.get("ID").toString()))
			learningFragmentID = returnMap.get("ID").toString();

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(LearningMaterialsActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("LearningFragmentID", learningFragmentID);

		new HttpPostAsync(LearningMaterialsActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("ToggleFavourite=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(LearningMaterialsActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(LearningMaterialsActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(LearningMaterialsActivity.this).showToast(desc);
					}
					else
					{
						fav = Integer.parseInt(mapstr.get("Fav") + "");
						mHandler.sendEmptyMessage(2);
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.TOGGLEFAVOURITE, parasTemp);

	}

	// protected void onActivityResult(int requestCode, int resultCode, Intent data)
	// {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (RESULT_OK == resultCode)
	// {
	// switch (requestCode)
	// {
	// case 1:
	// showProgress(4 * 1000);
	// request();
	// break;
	// default:
	// break;
	// }
	// }
	// };

	private OnItemClickAvoidForceListener onItemClickAvoidForceListener = new OnItemClickAvoidForceListener()
	{

		@Override
		public void onItemClickAvoidForce(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
		}
	};

	public TextWatcher textWatcher = new TextWatcher()
	{

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
			try
			{
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};

}
