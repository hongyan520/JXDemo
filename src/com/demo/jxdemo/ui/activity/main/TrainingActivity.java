package com.demo.jxdemo.ui.activity.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.support.CacheSupport;
import com.demo.base.util.CalendarUtil;
import com.demo.base.util.FileUtils;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.ScrollListViewUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.adapter.AttachListAdapter;
import com.demo.jxdemo.ui.customviews.CustomDialog;
import com.demo.jxdemo.utils.ToastManager;

/**
 * 训练
 * 
 * @author Chan
 */
public class TrainingActivity extends BaseActivity
{

	private String fragmentID;

	private TextView colTextView;

	private ListView attachListView;

	private AttachListAdapter attachAdapter;

	private List<Map<String, Object>> attachLists;

	private Map<String, Object> returnMap;

	private ImageView recordBeginBtn;

	private TextView recordTimeTextView;

	private static final String LOG_TAG = "AudioRecordTest";

	private static String mFileName = null;

	private MediaRecorder mRecorder = null;

	private MediaPlayer mPlayer = null;

	boolean mStartRecording = true;

	boolean mStartPlaying = true;

	private TextView tvHint = null;

	private long recLen = 0;

	private Timer timer;

	private CustomDialog cDialog = null;

	private int fav = 0;

	private List<Map<String, Object>> uploadedContentArray;

	private String serverUrl = "";

	private String localUrl = "";

	// 录音
	// 录音中
	// 上传录音
	// 已上传 点击播放
	// 未下载 点击下载 下载完 点击播放

	// ///////上传下载块//////////
	private TextView recordInfoTextView;

	private TextView recordSizeTextView;

	private Button recordDownloadButton;

	private ProgressBar progressBar;

	private TextView recordContentTextView;

	private ImageView replayImageView;

	// //////////稿件块///////////
	private EditText tjgjEditText;

	private RelativeLayout tjgjLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_traning);

		fragmentID = getIntent().getStringExtra("FragmentID");

		findViews();
		initData();
		setViewClick();
	}

	private void findViews()
	{
		((TextView) findViewById(R.id.formTilte)).setText("详细信息");
		((ImageView) findViewById(R.id.imgview_return)).setBackgroundResource(R.drawable.img_back);
		((ImageView) findViewById(R.id.imgview_return)).setVisibility(View.VISIBLE);
		colTextView = (TextView) findViewById(R.id.text_right);

		attachListView = (ListView) findViewById(R.id.list_traning_attach);
		recordBeginBtn = (ImageView) findViewById(R.id.img_record_start);
		recordTimeTextView = (TextView) findViewById(R.id.tv_record_time);
		tvHint = (TextView) findViewById(R.id.tv_hint);

		recordInfoTextView = (TextView) findViewById(R.id.text_record_info);
		recordSizeTextView = (TextView) findViewById(R.id.text_record_size);
		recordDownloadButton = (Button) findViewById(R.id.btn_record_download);
		progressBar = (ProgressBar) findViewById(R.id.progress_record);
		recordContentTextView = (TextView) findViewById(R.id.text_record_content);
		replayImageView = (ImageView) findViewById(R.id.img_record_replay);

		tjgjEditText = (EditText) findViewById(R.id.edit_tjgj);
		tjgjLayout = (RelativeLayout) findViewById(R.id.rlayout_submit);

		tvHint.setText("点击麦克风按钮开始录音");

	}

	private void initDialog()
	{
		Map<String, OnClickListener> map = new HashMap<String, View.OnClickListener>();
		map.put("是", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
				((LinearLayout) findViewById(R.id.layout_record)).setVisibility(View.GONE);
				uploadRadio();
			}
		});
		cDialog = new CustomDialog(this, "是否上传?", map, "否", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cDialog.cancel();
			}
		});
		cDialog.show();
	}

	private void uploadRadio()
	{
		recordInfoTextView.setText("后台上传录音");
		recordContentTextView.setText("您可以做点别的..................");
		((LinearLayout) findViewById(R.id.layout_record_updown)).setVisibility(View.VISIBLE);

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(TrainingActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("FragmentID", fragmentID);
		Map<String, File> paramsFile = new HashMap<String, File>();
		paramsFile.put("Content", new File(mFileName));

		new HttpPostAsync(TrainingActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(TrainingActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(TrainingActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(TrainingActivity.this).showToast(desc);
					}
					else
					{
						// 成功后处理
						ToastManager.getInstance(TrainingActivity.this).showToast("上传成功!");
						mHandler.sendEmptyMessage(1);
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_FILES_DATA, CommandConstants.URL + CommandConstants.UPLOADAUDIO, parasTemp, paramsFile);
	}

	private void uploadFile()
	{
		showProgress(4 * 1000);
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(TrainingActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("FragmentID", fragmentID);
		parasTemp.put("Content", tjgjEditText.getText().toString());

		new HttpPostAsync(TrainingActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(TrainingActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(TrainingActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(TrainingActivity.this).showToast(desc);
					}
					else
					{
						dismissProgress();
						ToastManager.getInstance(TrainingActivity.this).showToast("提交成功");
						mHandler.sendEmptyMessage(5);
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.UPLOADTEXT, parasTemp);

	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					recordInfoTextView.setText("已上传录音");
					progressBar.setVisibility(View.INVISIBLE);
					recordContentTextView.setText("点击红色按钮重听您的录音");
					replayImageView.setClickable(true);
					replayImageView.setOnClickListener(onClickAvoidForceListener);
					break;
				case 3:
					// File newFile = FileUtils.createFile(Constant.BASE_CACHE_PATH + Constant.STATIC_PATH, "/audiorecordtest.mp3");
					// if (newFile != null)
					// {
					recordInfoTextView.setText("已下载录音");
					progressBar.setVisibility(View.INVISIBLE);
					recordDownloadButton.setVisibility(View.INVISIBLE);
					recordContentTextView.setText("点击红色按钮听您的录音");
					replayImageView.setClickable(true);
					replayImageView.setOnClickListener(onClickAvoidForceListener);
					// }
					break;
				case 4:
					ToastManager.getInstance(TrainingActivity.this).showToast("下载失败，请重新下载！");
					recordContentTextView.setText("录音已经上传但本地没有找到缓存文件，请重新从积雪的服务器上进行下载。");
					progressBar.setVisibility(View.INVISIBLE);
					break;
				case 5:
					tjgjEditText.setText("");
					break;
				case 2014:
					// 下载中更新UI进度条
					int currentDownSize = msg.arg1;
					if (currentDownSize == 100)
					{ // 下载完成
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						progressBar.setVisibility(View.GONE);
						progressBar.setProgress(0); // 复位进度条
					}
					else
					{
						progressBar.setProgress(currentDownSize);
					}

					break;
			}
		}
	};

	private void initData()
	{
		request();

		mFileName = Constant.BASE_CACHE_PATH + Constant.STATIC_PATH;
		mFileName += "/audiorecordtest.mp3";
	}

	private void request()
	{
		showProgress(4 * 1000);
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(TrainingActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("FragmentID", fragmentID);

		new HttpPostAsync(TrainingActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(TrainingActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(TrainingActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(TrainingActivity.this).showToast(desc);
					}
					else
					{
						loadSuccessDeal(mapstr);// 成功后处理
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.LEARNINGFRAGMENT, parasTemp);

	}

	/**
	 * 返回数据后处理
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void loadSuccessDeal(Map<String, Object> map)
	{
		new LoadDealTask().execute(map);
	}

	class LoadDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
	{

		@Override
		protected void onPreExecute()
		{
			showProgress(2 * 60 * 1000);
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Map<String, Object>... map)
		{
			try
			{
				if (map != null)
				{
					returnMap = map[0];
					if (map[0].get("RequirementAttachementArray") != null)
						attachLists = JsonUtil.getList(map[0].get("RequirementAttachementArray").toString());

					uploadedContentArray = JsonUtil.getList(StringUtil.Object2String(returnMap.get("UploadedContentArray")));

					handler.sendEmptyMessage(1);
					dismissProgress();
				}
				else
					ToastManager.getInstance(TrainingActivity.this).showToast("获取失败!");

			}
			catch (Exception e)
			{
				e.printStackTrace();
				ToastManager.getInstance(TrainingActivity.this).showToast("获取失败!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			dismissProgress();
		}

	}

	private void initView()
	{
		((TextView) findViewById(R.id.text_xlxq)).setText(StringUtil.Object2String(returnMap.get("RequirementDescription")));
		((TextView) findViewById(R.id.text_lszd)).setText(StringUtil.Object2String(returnMap.get("FeedbackContent")));

		attachAdapter = new AttachListAdapter(TrainingActivity.this);
		attachAdapter.setDataList(attachLists);
		attachListView.setAdapter(attachAdapter);
		ScrollListViewUtil.setListViewHeightBasedOnChildren(attachListView);

		fav = returnMap.get("Fav") == null ? 0 : Integer.parseInt(returnMap.get("Fav").toString());
		controlRightImg();

		// String[] arr = serverUrl.split("/");

		int uploadType = returnMap.get("UploadType") == null ? 0 : Integer.parseInt(returnMap.get("UploadType").toString());
		// 1是录音
		if (uploadType == 1)
		{
			if (uploadedContentArray != null && uploadedContentArray.size() > 0)
			{// 如果服务器上录音文件不为空
				serverUrl = CommandConstants.URL_ROOT + uploadedContentArray.get(0).get("Content");
				localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
				// 如果本地存在该录音文件
				if (FileUtils.isExisitFile(localUrl))// (FileUtils.isExisitFile(arr[arr.length - 1]))
				{
					// 存在，已经下载
					recordInfoTextView.setText("已上传录音");
					recordContentTextView.setText("点击红色按钮重听您的录音");// ("录音已经上传但本地没有找到缓存文件，请重新从积雪的服务器上进行下载。");
					progressBar.setVisibility(View.INVISIBLE);
					replayImageView.setClickable(true);
					replayImageView.setOnClickListener(onClickAvoidForceListener);
				}
				else
				{// 如果本地不存在该录音文件
					recordDownloadButton.setVisibility(View.VISIBLE);
					recordInfoTextView.setText("已上传录音");
					recordContentTextView.setText("录音已经上传但本地没有找到缓存文件，请重新从积雪的服务器上进行下载。");
					progressBar.setVisibility(View.INVISIBLE);
					recordDownloadButton.setOnClickListener(onClickAvoidForceListener);
				}
				((LinearLayout) findViewById(R.id.layout_record_updown)).setVisibility(View.VISIBLE);
			}
			else
				// 如果服务器上录音文件为空
				((LinearLayout) findViewById(R.id.layout_record)).setVisibility(View.VISIBLE);
		}
		// 其他
		else
			((LinearLayout) findViewById(R.id.layout_tjgj)).setVisibility(View.VISIBLE);

		((ScrollView) findViewById(R.id.scrollView1)).setVisibility(View.VISIBLE);
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		((RelativeLayout) findViewById(R.id.rlayout_chat)).setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClickAvoidForceListener);

		recordBeginBtn.setOnClickListener(onClickAvoidForceListener);
		tjgjLayout.setOnClickListener(onClickAvoidForceListener);
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
				case R.id.layout_remark:
					toggleFavourite();
					break;
				case R.id.rlayout_chat:
					Intent intent = new Intent();
					intent.setClass(TrainingActivity.this, ChatActivity.class);
					startActivity(intent);
					break;
				case R.id.img_record_start:
					// 录音按钮
					onRecord(mStartRecording);
					if (mStartRecording)
					{
						recLen = 0;
						((ImageView) v).setBackgroundResource(R.drawable.icon_record_frame);
						tvHint.setText("正在录音，再次点击结束录音");
						if (timer == null)
						{
							timer = new Timer(true);
						}
						timer.schedule(new MyTimeTask(), 0, 1000); // 延时1000ms后执行，1000ms执行一次
					}
					else
					{
						((ImageView) v).setBackgroundResource(R.drawable.icon_record);
						recordTimeTextView.setText("");
						tvHint.setText("点击麦克风按钮开始录音");
						timer.cancel(); // 退出计时器
						timer = null;
						initDialog();
					}
					mStartRecording = !mStartRecording;
					break;
				case R.id.img_record_replay:
					// 播放录音按钮
					onPlay(mStartPlaying);
					mStartPlaying = !mStartPlaying;
					break;
				case R.id.btn_record_download:
					downloadRecord();
					break;
				case R.id.rlayout_submit:
					uploadFile();
					break;
				default:
					break;
			}
		}
	};

	private void downloadRecord()
	{
		progressBar.setVisibility(View.VISIBLE);
		recordInfoTextView.setText("后台下载录音");
		recordContentTextView.setText("您可以做点别的..................");

		// if (!StringUtil.isBlank(mFileName))
		// {
		final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
		new Thread()
		{
			public void run()
			{
				if (downloadFile(serverUrl, localUrl, mHandler))
				{
					Message msg = new Message();
					msg.what = 3;
					Bundle bundle = new Bundle();
					bundle.putString("localUrl", localUrl);
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				}
				else
				{

					Message msg = new Message();
					msg.what = 4;
					Bundle bundle = new Bundle();
					bundle.putString("localUrl", localUrl);
					msg.setData(bundle);
					mHandler.sendMessage(msg);

				}

			};
		}.start();

		// }

	}

	// 当录音按钮被click时调用此方法，开始或停止录音
	private void onRecord(boolean start)
	{
		if (start)
		{
			startRecording();
		}
		else
		{
			stopRecording();
		}
	}

	// 当播放按钮被click时调用此方法，开始或停止播放
	private void onPlay(boolean start)
	{
		if (start)
		{
			startPlaying();
		}
		else
		{
			stopPlaying();
		}
	}

	private void startPlaying()
	{
		mPlayer = new MediaPlayer();
		try
		{
			// 设置要播放的文件
			if (FileUtils.isExisitFile(localUrl))
				mPlayer.setDataSource(localUrl);
			else
				mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			// 播放之
			mPlayer.start();
		}
		catch (IOException e)
		{
			Log.e(LOG_TAG, "prepare() failed");
		}
	}

	// 停止播放
	private void stopPlaying()
	{
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording()
	{
		mRecorder = new MediaRecorder();
		// 设置音源为Micphone
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置封装格式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		// 设置编码格式
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try
		{
			mRecorder.prepare();
		}
		catch (IOException e)
		{
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording()
	{
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		// Activity暂停时释放录音和播放对象
		if (mRecorder != null)
		{
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null)
		{
			mPlayer.release();
			mPlayer = null;
		}
	}

	private class MyTimeTask extends TimerTask
	{
		public void run()
		{
			Message message = new Message();
			message.what = 9;
			handler.sendMessage(message);
		}
	};

	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					initView();
					break;
				case 2:
					dismissProgress();
					controlRightImg();
					break;
				case 9:
					recLen++;
					// tvHint.setText("正在录音，再次点击结束录音" + CalendarUtil.getDateFormatByMs(recLen * 1000, "mm:ss"));
					recordTimeTextView.setText(CalendarUtil.getDateFormatByMs(recLen * 1000, "mm:ss"));
					break;
			}
			super.handleMessage(msg);
		}
	};

	private void controlRightImg()
	{
		if (fav == 1)
		{
			Drawable drawable = getResources().getDrawable(R.drawable.icon_favourite);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			colTextView.setCompoundDrawables(drawable, null, null, null);
		}
		else
		{
			Drawable drawable = getResources().getDrawable(R.drawable.icon_favourite_gray);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			colTextView.setCompoundDrawables(drawable, null, null, null);
		}
		colTextView.setText(" 收藏");
		colTextView.setVisibility(View.VISIBLE);
	}

	private void toggleFavourite()
	{
		showProgress(4000);
		String learningFragmentID = "";
		if (returnMap != null && !StringUtil.isBlank(returnMap.get("ID").toString()))
			learningFragmentID = returnMap.get("ID").toString();

		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(TrainingActivity.this).get(Constant.USER_TOKEN));
		parasTemp.put("FragmentID", learningFragmentID);

		new HttpPostAsync(TrainingActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("ToggleFavourite=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(TrainingActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(TrainingActivity.this).showToast("连接不上服务器");
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
						ToastManager.getInstance(TrainingActivity.this).showToast(desc);
					}
					else
					{
						fav = Integer.parseInt(mapstr.get("Fav") + "");
						handler.sendEmptyMessage(2);
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.TOGGLEFAVOURITE, parasTemp);

	}

	public static boolean downloadFile(String serverUrl, String localUrl, Handler mHandler)
	{
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection conn = null;
		try
		{

			File file = new File(localUrl);
			if (file.exists())
			{
				return true;
			}
			else
			{
				// httpGet连接对象
				HttpGet httpRequest = new HttpGet(serverUrl);
				// 取得HttpClient 对象
				HttpClient httpclient = new DefaultHttpClient();
				// 请求httpClient ，取得HttpRestponse
				HttpResponse httpResponse = httpclient.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					// 取得相关信息 取得HttpEntiy
					HttpEntity httpEntity = httpResponse.getEntity();
					// 获得一个输入流
					input = httpEntity.getContent();
					int fileSize = 0;
					if (input != null)
					{
						fileSize = input.toString().length();
					}

					String dir = localUrl.substring(0, localUrl.lastIndexOf("/") + 1);
					new File(dir).mkdirs();// 新建文件夹
					file.createNewFile();// 新建文件
					output = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;
					int lentemp = 0;
					while ((len = input.read(buffer)) != -1)
					{
						output.write(buffer, 0, len);
						if (mHandler != null)
						{
							Message msg = new Message();
							msg.what = 2014;
							lentemp = lentemp + len;
							msg.arg1 = (lentemp * 100) / fileSize;
							mHandler.sendMessage(msg);

						}
					}
					if (mHandler != null)
					{
						Message msg = new Message();
						msg.what = 2014;
						msg.arg1 = 100;
						mHandler.sendMessage(msg);

					}

					// 读取大文件
					// byte[] buffer = new byte[4 * 1024];
					// while (input.read(buffer) != -1) {
					// output.write(buffer);
					// }
					output.flush();
				}

				// URL url = new URL(serverUrl);
				// conn = (HttpURLConnection) url
				// .openConnection();
				// input = conn.getInputStream();

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try
			{
				if (output != null)
					output.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (conn != null)
			{
				conn.disconnect();
			}
		}
		return true;
	}
}
