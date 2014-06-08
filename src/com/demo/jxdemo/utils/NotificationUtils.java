package com.demo.jxdemo.utils;

import java.util.Date;
import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.demo.base.util.DateUtil;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.R;

public class NotificationUtils
{

	private NotificationManager mqNotificationManager;

	private Context context = null;

	private static HashMap<String, NoticeInfo> sessionIds = new HashMap<String, NoticeInfo>();

	private static NotificationUtils instance;

	public NotificationUtils(Context context)
	{
		this.context = context;
		mqNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
	}

	public static NotificationUtils getInstance(Context context)
	{

		if (instance == null)
		{
			instance = new NotificationUtils(context);
		}
		return instance;
	}

	/**
	 * 发送通知
	 * 
	 * @param intentDate
	 */
	public synchronized void sendNotification(Intent intentDate)
	{

		Notification mqNotification = new Notification();
		PendingIntent mqPendingIntent = null;
		// 点击通知栏，回到主界面
		Intent broadIntent = intentDate;
		String sessionId = broadIntent.getStringExtra("sessionId");
		Log.d("", "FLAG---will send seeionId=" + sessionId);
		// 如果已存在广播而且上次发送的时间到现在还没超过3秒，就不再发送
		if (sessionIds.containsKey(sessionId))
		{
			if (DateUtil.getDiffByDate(sessionIds.get(sessionId).lastShowTime) < 3)
			{
				Log.d("", "FLAG --time is less than 3 seconds so not send notice!!");
				return;
			}
			else
			{
				// 超过了3秒，则重置一下时间
				sessionIds.get(sessionId).lastShowTime = new Date(System.currentTimeMillis());
			}
		}
		else
		{
			java.util.Random r = new java.util.Random();
			NoticeInfo noticeInfo = new NoticeInfo(r.nextInt(), new Date(System.currentTimeMillis()));
			sessionIds.put(sessionId, noticeInfo);
		}

		Log.d("", "FLAG---!!send seeionId " + sessionId);

		Intent mqIntent = new Intent();// context,MessageDetailViewActivity.class);
		mqIntent.putExtra("flag", "Notice");
		mqIntent.putExtra("sessionId", sessionId);

		// 设置通知栏显示内容
		mqNotification.icon = R.drawable.ic_launcher;
		mqNotification.defaults = 0;

		if ("ON".equals(SharedPreferencesConfig.config(context).get(Constant.ISSOUND)))
		{
			mqNotification.defaults |= Notification.DEFAULT_SOUND;
		}
		else
		{
			mqNotification.sound = null;
		}
		if ("ON".equals(SharedPreferencesConfig.config(context).get(Constant.ISVIBRATE)))
		{
			mqNotification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		else
		{
			mqNotification.vibrate = null;
		}

		mqNotification.tickerText = "新消息";
		mqNotification.flags = Notification.FLAG_AUTO_CANCEL;

		if ("new_version".equals(sessionId))
		{
			// mqIntent.setClass(context, NewVersionActivity.class);
			// mqPendingIntent = PendingIntent.getActivity(context, sessionIds.get(sessionId).showId, mqIntent,
			// PendingIntent.FLAG_UPDATE_CURRENT);
			// mqNotification.setLatestEventInfo(context, "新版本", "发现新版本", mqPendingIntent);
			ToastManager.getInstance(context).showToast("发现新版本！");
		}
		else
		{
			// mqIntent.setClass(context, MessageDetailViewActivity.class);
			// MessageSession session = MessageSession.getSessionFromId(sessionId);
			// String sessionType = session == null ? "移动OA" : session.getTitle();
			// mqPendingIntent = PendingIntent.getActivity(context, sessionIds.get(sessionId).showId, mqIntent,
			// PendingIntent.FLAG_UPDATE_CURRENT);
			// mqNotification.setLatestEventInfo(context, sessionType, "发来一条消息！", mqPendingIntent);
			ToastManager.getInstance(context).showToast("发来一条消息！");
		}

		// 发出通知
		mqNotificationManager.notify(sessionIds.get(sessionId).showId, mqNotification);

	}

	/**
	 * 清除通知栏的指定sessionId的通知
	 * 
	 * @param context
	 * @param sessionId
	 */
	public static void cancelNotification(Context context, String sessionId)
	{
		if (sessionId != null && sessionIds.containsKey(sessionId))
		{
			((NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE)).cancel(sessionIds.get(sessionId).showId);
			sessionIds.remove(sessionId);
		}

	}

	/**
	 * 清除通知栏的所有通知，如果能清除
	 * 
	 * @param context
	 */
	public static void cancelAllNotification(Context context)
	{
		((NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE)).cancelAll();
	}

	/**
	 * 文件下载成功发送
	 * 
	 * @param fileId
	 * @param fileName
	 */
	public synchronized void sendFileNotification(String fileId, String fileName)
	{

		// Notification mqNotification = new Notification();
		// PendingIntent mqPendingIntent = null;
		// // 点击通知栏，回到主界面
		// Intent mqIntent = new Intent(context,MessageDetailViewActivity.class);
		// if(sessionIds.containsKey(fileId)){
		//
		// }else{
		// java.util.Random r=new java.util.Random();
		// sessionIds.put(fileId,r.nextInt());
		// }
		// mqPendingIntent = PendingIntent.getActivity(context, sessionIds.get(fileId), mqIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// // 设置通知栏显示内容
		// mqNotification.icon = R.drawable.ic_launcher;
		// mqNotification.defaults= 0;
		//
		// if("ON".equals(SharedPreferencesConfig.config(context).get(Constant.ISSOUND))){
		// mqNotification.defaults |= Notification.DEFAULT_SOUND;
		// }
		// else{
		// mqNotification.sound=null;
		// }
		// if("ON".equals(SharedPreferencesConfig.config(context).get(Constant.ISVIBRATE))){
		// mqNotification.defaults |= Notification.DEFAULT_VIBRATE;
		// }
		// else{
		// mqNotification.vibrate=null;
		// }
		//
		// mqNotification.tickerText = "新消息";
		// mqNotification.flags = Notification.FLAG_AUTO_CANCEL ;
		//
		// mqNotification.setLatestEventInfo(context,fileName,"文件下载完成" ,
		// mqPendingIntent);
		// // 发出通知
		// mqNotificationManager.notify(sessionIds.get(fileId), mqNotification);
		// Log.d("","random ="+sessionIds);

	}

	/**
	 * 通知的额外数据
	 */
	class NoticeInfo
	{

		/**
		 * 通知栏的通知ID，唯一区分
		 */
		public int showId;

		/**
		 * 该类通知上次显示的时间
		 */
		public Date lastShowTime;

		NoticeInfo(int id, Date time)
		{
			this.showId = id;
			this.lastShowTime = time;
		}

	}

}
