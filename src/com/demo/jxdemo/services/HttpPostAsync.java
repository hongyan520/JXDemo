package com.demo.jxdemo.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;

public class HttpPostAsync extends AsyncTask<Object, Object, Object> implements ICallBack
{

	private String tag = "HttpPostAsync";

	private Context context;

	public HttpPostAsync(Context context)
	{
		this.context = context;
	}

	/**
	 * <b>Summary: </b>
	 * postByteData(发送http请求)
	 * 
	 * @param urlString
	 *            请求url地址
	 * @param data
	 *            byte[]类型的参数
	 * @return
	 */
	public synchronized Object postByteData(String urlString, byte[] data)
	{
		HttpURLConnection con = null;
		DataOutputStream ds = null;
		InputStream is = null;
		BufferedReader bufferedReader = null;
		try
		{
			URL url = new URL(urlString);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");// 设置传送的method=POST
			con.setDoInput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
			con.setDoOutput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
			con.setUseCaches(false);// Post 请求不能使用缓存

			// 设置头信息
			con.setRequestProperty("Connection", "Keep-Alive"); // TODO 暂时去掉长连接
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("content-type", "text/html");
			con.setRequestProperty("postMethod", Constant.POST_BYTE_DATA);// 设置http请求的方式
			con.setRequestProperty("userId", SharedPreferencesConfig.config(context).get(Constant.USER_ID));// 用户ID存储参数
			// 设置请求超时时间
			int readtime = Integer.parseInt(Constant.RESPONSEREADTIMEVALUE);
			con.setReadTimeout(readtime);
			// 设置输入流
			ds = new DataOutputStream(con.getOutputStream());
			ds.write(data);
			ds.flush();
			is = con.getInputStream();// 发送请求获取输出流
			bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			Object line;
			while ((line = bufferedReader.readLine()) != null)
			{
				sb.append(line);
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			Log.e(tag, StringUtil.deNull(e == null ? "" : e.getMessage()));
		}
		finally
		{
			if (con != null)
			{
				try
				{
					con.disconnect();
				}
				catch (Exception e)
				{

				}
			}
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				}
				catch (IOException e)
				{

				}
			}
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{

				}
			}
			if (ds != null)
			{
				try
				{
					ds.close();
				}
				catch (IOException e)
				{

				}
			}

		}
		return Constant.HTTP_REQUEST_FAIL;
	}

	/**
	 * <b>Summary: </b>
	 * postKeyValueData(发送http请求)
	 * 
	 * @param urlString
	 *            请求url地址
	 * @param param
	 *            Map键值对
	 * @return
	 */
	public synchronized Object postKeyValueData(String urlString, Map<String, String> param)
	{
		DefaultHttpClient httpClient = null;
		try
		{
			HttpPost httpPost = new HttpPost(urlString);
			// 在http请求头中添加请求方法类型
			httpPost.setHeader("postMethod", Constant.POST_KEYVALUE_DATA);
			httpPost.setHeader("userId", SharedPreferencesConfig.config(context).get(Constant.USER_ID));// 用户ID存储参数

			// Post传送参数数必须用NameValuePair[]阵列储存
			// 传参数服务端获取的方法为request.getParameter("name")
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Set<String> keyset = param.keySet();
			for (String key : keyset)
			{
				params.add(new BasicNameValuePair(key, param.get(key)));
			}
			// 发出HTTP请求
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpClient = new DefaultHttpClient();
			// 设置请求超时时间
			int readtime = Integer.parseInt(Constant.RESPONSEREADTIMEVALUE);
			httpClient.getParams().setIntParameter("http.socket.timeout", readtime);
			// 取得HTTP response
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{// 正常响应
				// 返回响应字串
				return EntityUtils.toString(httpResponse.getEntity());
			}
			else
			{
				throw new Exception("http请求响应不正确，响应值为" + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (Exception e)
		{
			Log.e(urlString, StringUtil.deNull(e == null ? "" : e.getMessage()));
		}
		finally
		{
			if (httpClient != null)
			{
				try
				{
					httpClient.getConnectionManager().shutdown();
				}
				catch (Exception e)
				{

				}

			}
		}
		return Constant.HTTP_REQUEST_FAIL;
	}

	/**
	 * <b>Summary: </b>
	 * 复写方法 doInBackground
	 * 
	 * @param param
	 * @return
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Object doInBackground(Object... param)
	{
		if (Constant.POST_KEYVALUE_DATA.equals(param[0]))
		{
			@SuppressWarnings("unchecked") Map<String, String> map = (Map<String, String>) param[2];
			return postKeyValueData(param[1].toString(), map);
		}
		else if (Constant.POST_BYTE_DATA.equals(param[0]))
		{
			return postByteData(param[1].toString(), (byte[]) param[2]);
		}
		else
		{
			return Constant.HTTP_REQUEST_FAIL;
		}
	}

	/**
	 * <b>Summary: </b>
	 * 复写方法 onPostExecute
	 * 
	 * @param result
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Object result)
	{
		backResult(result);
	}

	/**
	 * <b>Summary: </b>
	 * 复写方法 backResult
	 * 
	 * @param result
	 * @return
	 * @see com.chinacreator.framework.services.ICallBack#backResult(java.lang.Object)
	 */
	public Object backResult(Object result)
	{
		return result;
	}
}
