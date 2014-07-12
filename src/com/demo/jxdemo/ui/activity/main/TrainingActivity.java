package com.demo.jxdemo.ui.activity.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ui.listener.OnClickAvoidForceListener;
import ui.listener.OnItemClickAvoidForceListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.base.util.CalendarUtil;
import com.demo.base.util.ScrollListViewUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.adapter.AttachListAdapter;

/**
 * 训练
 * 
 * @author Chan
 */
public class TrainingActivity extends BaseActivity
{

	private ListView attachListView;

	private AttachListAdapter attachAdapter;

	private List<Map<String, Object>> lists;

	private Button recordBtn;

	private Button playBtn;

	private static final String LOG_TAG = "AudioRecordTest";

	private static String mFileName = null;

	private MediaRecorder mRecorder = null;

	private MediaPlayer mPlayer = null;

	boolean mStartRecording = true;

	boolean mStartPlaying = true;

	private TextView tvHint = null;

	private long recLen = 0;

	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_traning);

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

		attachListView = (ListView) findViewById(R.id.list_traning_attach);

		recordBtn = (Button) findViewById(R.id.record_btn);
		playBtn = (Button) findViewById(R.id.play_btn);

		tvHint = (TextView) findViewById(R.id.tv_hint);
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

		mFileName = Constant.BASE_CACHE_PATH + Constant.STATIC_PATH;
		mFileName += "/audiorecordtest.mp3";

		tvHint.setText("点击麦克风按钮开始录音");
	}

	private void initView()
	{

		attachAdapter = new AttachListAdapter(TrainingActivity.this);
		attachAdapter.setDataList(lists);
		attachListView.setAdapter(attachAdapter);
		ScrollListViewUtil.setListViewHeightBasedOnChildren(attachListView);

		attachListView.setOnItemClickListener(onItemClickAvoidForceListener);
	}

	private void setViewClick()
	{
		((LinearLayout) findViewById(R.id.layout_return)).setOnClickListener(onClickAvoidForceListener);
		((RelativeLayout) findViewById(R.id.rlayout_chat)).setOnClickListener(onClickAvoidForceListener);

		recordBtn.setOnClickListener(onClickAvoidForceListener);
		playBtn.setOnClickListener(onClickAvoidForceListener);
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
				case R.id.rlayout_chat:
					Intent intent = new Intent();
					intent.setClass(TrainingActivity.this, ChatActivity.class);
					startActivity(intent);
					break;
				case R.id.record_btn:
					// 录音按钮
					onRecord(mStartRecording);
					if (mStartRecording)
					{
						recLen = 0;
						((Button) v).setText("停止录音");
						tvHint.setText("正在录音，再次点击结束录音");
						if (timer == null)
						{
							timer = new Timer(true);
						}
						timer.schedule(new MyTimeTask(), 0, 1000); // 延时1000ms后执行，1000ms执行一次
					}
					else
					{
						((Button) v).setText("开始录音");
						timer.cancel(); // 退出计时器
						timer = null;
					}
					mStartRecording = !mStartRecording;
					break;
				case R.id.play_btn:
					// 播放录音按钮
					onPlay(mStartPlaying);
					if (mStartPlaying)
					{
						((Button) v).setText("停止播放");
					}
					else
					{
						((Button) v).setText("开始播放");
					}
					mStartPlaying = !mStartPlaying;
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
			message.what = 1;
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
					recLen++;
					tvHint.setText("正在录音，再次点击结束录音" + CalendarUtil.getDateFormatByMs(recLen * 1000, "mm:ss"));
					break;
			}
			super.handleMessage(msg);
		}
	};
}
