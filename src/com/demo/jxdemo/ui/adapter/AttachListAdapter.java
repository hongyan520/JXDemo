package com.demo.jxdemo.ui.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.base.global.ActivityTaskManager;
import com.demo.base.support.CacheSupport;
import com.demo.base.util.BitmapUtils;
import com.demo.base.util.FileUtils;
import com.demo.base.util.HttpUtils;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.activity.menu.UserInfoActivity;

public class AttachListAdapter extends BaseAdapter
{

	private static String tag = "AttachListAdapter";;

	/**
	 * Layout填充器
	 */
	private LayoutInflater inflater = null;

	/**
	 * 存储列表实体类的list
	 */
	private List<Map<String, Object>> list = null;

	/**
	 * 列表类
	 */
	@SuppressWarnings("unused")
	private Context context;
	
	private final static String DOWN_ON = "下载";
	private final static String DOWN_ING = "下载中";
	private final static String DOWN_WAIT = "暂停";
	private final static String DOWN_OK_SEE = "查看";

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            列表类
	 */
	public AttachListAdapter(Context context)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public void setDataList(List<Map<String, Object>> list)
	{
		this.list = list;
	}

	public List<Map<String, Object>> getList()
	{
		return list;
	}

	@Override
	public int getCount()
	{
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final Map<String, Object> map = list.get(position);
		// 先确保view存在
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.activity_learningmaterials_item_attach, null);
		}

		// 获得holder

		Object tag = convertView.getTag();
		final AttachListHolder holder;
		if (tag == null)
		{
			holder = new AttachListHolder(convertView);
		}
		else
		{
			holder = (AttachListHolder) tag;
		}
		holder.setData(map);
		// 设置tag
		convertView.setTag(holder);
		
		final String serverUrl = CommandConstants.URL_ROOT + StringUtil.Object2String(map.get("URL"));
		final String localUrl = CacheSupport.staticServerUrlConvertToCachePath(serverUrl);
		if(FileUtils.isExisitFile(localUrl)){
			// 存在，已经下载
			holder.rightBtn.setText(DOWN_OK_SEE);
		}
		
		holder.rightBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(final View v)
			{
				if(((Button)v).getText().equals(DOWN_ON)){// 点击下载开始下载
					holder.progressBar.setVisibility(View.VISIBLE);
					holder.rightBtn.setText(DOWN_ING);
					new Thread()
					{
						public void run()
						{
							if (downloadFile(serverUrl, localUrl,mHandler,holder))
							{
								Message msg = new Message();
								msg.what = 1;
								msg.obj = holder;
								Bundle bundle = new Bundle();
								bundle.putString("localUrl", localUrl);
								msg.setData(bundle);
								mHandler.sendMessage(msg);
							}
						};
					}.start();
				}else if(((Button)v).getText().equals(DOWN_OK_SEE)){// 点击查看附件
					// TODO 
				}
			}
		});

		return convertView;
	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			
			switch (msg.what)
			{
				case 1:
					AttachListHolder holder = (AttachListHolder) msg.obj;
					if (DOWN_ON.equals(holder.rightBtn.getText().toString()))
					{
						holder.rightBtn.setText(DOWN_ING);
					}
					else if (DOWN_WAIT.equals(holder.rightBtn.getText().toString()))
					{
						holder.rightBtn.setText(DOWN_ON);
					}
					break;
				case 2014:
					// 下载中更新UI进度条
					AttachListHolder aholder = (AttachListHolder) msg.obj;
					int currentDownSize = msg.arg1;
					if(currentDownSize == 100){ // 下载完成
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						aholder.rightBtn.setText(DOWN_OK_SEE);
						aholder.progressBar.setVisibility(View.GONE);
						aholder.progressBar.setProgress(0); // 复位进度条
					}else{
						aholder.progressBar.setProgress(currentDownSize);
					}
					
					break;
				default:
					break;
			}
		}
	};

	private class AttachListHolder
	{

		TextView textTitle;

		TextView textSize;

		Button leftIcon;

		Button rightBtn;
		
		ProgressBar progressBar;

		private AttachListHolder(View convertView)
		{
			textTitle = (TextView) convertView.findViewById(R.id.text_title);
			textSize = (TextView) convertView.findViewById(R.id.text_size);
			leftIcon = (Button) convertView.findViewById(R.id.btn_left);
			rightBtn = (Button) convertView.findViewById(R.id.btn_right);
			progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);
		}

		private void setData(Map<String, Object> map)
		{
			String[] a;// 分隔整个url
			String[] b;// 分隔标题和文件类型
			String url = StringUtil.Object2String(map.get("URL"));
			if (!StringUtil.isBlank(url))
			{
				a = url.split("/");
				url = a[a.length - 1];
			}
			b = url.replace(".", "!").split("!");
			textTitle.setText(StringUtil.Object2String(url));
			textSize.setText(FileUtils.FormetFileSize(Long.parseLong(StringUtil.Object2String(map.get("Size")))));
			leftIcon.setText(StringUtil.Object2String(b[b.length - 1]));
		}
	}
	
	
	/**
	 * 下载服务器文件
	 * @param serverUrl 服务器地址
	 * @param localUrl 本地存储地址
	 * @return
	 */
	public static boolean downloadFile(String serverUrl, String localUrl,Handler mHandler,AttachListHolder mholder) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection conn = null;
		try {

			File file = new File(localUrl);
			if (file.exists()) {
				Log.i(tag = "AttachListAdapter", localUrl + "is exist");
				return true;
			} else {
				
				//httpGet连接对象 
		        HttpGet httpRequest = new HttpGet(serverUrl);
		      //取得HttpClient 对象  
		        HttpClient httpclient = new DefaultHttpClient();  
		      //请求httpClient ，取得HttpRestponse  
	            HttpResponse httpResponse = httpclient.execute(httpRequest);  
	            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
	                //取得相关信息 取得HttpEntiy  
	                HttpEntity httpEntity = httpResponse.getEntity();  
	                //获得一个输入流  
	                input = httpEntity.getContent();  
	                int fileSize = 0;
	                if(input != null){
	                	fileSize = input.toString().length();
	                }
	                
	                String dir = localUrl.substring(0,
							localUrl.lastIndexOf("/") + 1);
					new File(dir).mkdirs();// 新建文件夹
					file.createNewFile();// 新建文件
					output = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
	                int len = 0;
	                int lentemp = 0;
	                while ((len = input.read(buffer)) != -1) {
	                	output.write(buffer, 0, len);
	                	if(mHandler != null){
	                		Message msg = new Message();
	                		msg.what = 2014;
	                		msg.obj = mholder;
	                		lentemp = lentemp + len;
	                		msg.arg1 = (lentemp * 100) / fileSize;
	                		mHandler.sendMessage(msg);
	                		
	                	}
	                }
	                if(mHandler != null){
                		Message msg = new Message();
                		msg.what = 2014;
                		msg.obj = mholder;
                		msg.arg1 = 100;
                		mHandler.sendMessage(msg);
                		
                	}
	                
					
					// 读取大文件
//					byte[] buffer = new byte[4 * 1024];
//					while (input.read(buffer) != -1) {
//						output.write(buffer);
//					}
					output.flush();
	            }
				
//				URL url = new URL(serverUrl);
//				conn = (HttpURLConnection) url
//						.openConnection();
//				input = conn.getInputStream();
				
			}
			Log.i(tag, localUrl + "download success");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(tag, localUrl + "download fail");
			return false;
		} finally {
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				if(output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		return true;
	}

}
