package com.demo.jxdemo.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnItemClickAvoidForceListener;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.application.ConfigCache;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.services.HttpPostAsync;
import com.demo.jxdemo.ui.adapter.BaseListAdapter;
import com.demo.jxdemo.ui.customviews.PullToRefreshView;
import com.demo.jxdemo.ui.customviews.PullToRefreshView.OnFooterRefreshListener;
import com.demo.jxdemo.ui.customviews.PullToRefreshView.OnHeaderRefreshListener;
import com.demo.jxdemo.utils.ToastManager;
import com.demo.jxdemo.R;

/**
 * 该类适用于只有一个上拉刷新列表的activiyt界面，子类继承与次，并
 * 实现抽象方法。
 */
public abstract class BaseListActivity extends BaseActivity
{

	private ListView listview;

	private PullToRefreshView mPullToRefreshView;

	private BaseListAdapter adapter;

	private final int loadCount = 10;

	protected List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	/**
	 * 布局解析器
	 */
	protected LayoutInflater inflater;

	protected LinearLayout returnLayout;

	/*-----子类必须初始化的参数-----------*/
	protected String cmd = ""; // 命令

	protected Map<String, Object> params = new HashMap<String, Object>(); // 数据请求所需的参数

	protected int layoutId = 0;// activity的布局

	protected String title = ""; // activity的标题

	protected int listLayoutId = 0; // listview控件的id

	/*----------------------------------*/

	private boolean needInit = true;

	private boolean shouldReLoad = false;

	private View emptyView;

	protected abstract void onClickItem(int pos, Map<String, Object> record);

	public abstract View getView(Map<String, Object> record, int position, View convertView, ViewGroup parent);

	/**
	 * 子类在执行oncreate()方法的时候先执行该方法初始化
	 * 
	 * @param cmd
	 * @param params
	 * @param layoutId
	 * @param title
	 * @param listLayoutId
	 */
	public void initData(String cmd, Map<String, Object> params, int layoutId, String title, int listLayoutId)
	{
		this.cmd = cmd;
		this.params = params;
		this.layoutId = layoutId;
		this.title = title;
		this.listLayoutId = listLayoutId;
		initViews();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(BaseListActivity.this);
	}

	private void initViews()
	{

		if (listLayoutId == 0 || layoutId == 0)
		{
			throw new IllegalStateException("Please set the initData() menthod after call super.onCreate()!");
		}

		if (!needInit)
		{
			return;
		}

		needInit = false;

		((Button) findViewById(R.id.remarkBtn)).setVisibility(View.GONE);

		listview = (ListView) findViewById(this.listLayoutId);

		mPullToRefreshView = (PullToRefreshView) listview.getParent();

		setPullListEmptyView(this, "抱歉,暂时没有相关数据");
		((TextView) findViewById(R.id.formTilte)).setText(title);
		returnLayout = (LinearLayout) findViewById(R.id.returnBtn);
		returnLayout.setVisibility(View.VISIBLE);
		returnLayout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				BaseListActivity.this.finish();
			}
		});

		adapter = new BaseListAdapter(BaseListActivity.this);
		listview.setDrawSelectorOnTop(true);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(onItemClickAvoidForceListener);

		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener()
		{

			@Override
			public void onHeaderRefresh(PullToRefreshView view)
			{
				list.clear();
				requestListData(0, loadCount, false);

			}
		});
		mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener()
		{

			@Override
			public void onFooterRefresh(PullToRefreshView view)
			{
				// 加载更多
				requestListData(list.size(), loadCount, true);

			}
		});

		// requestListData(list.size() + 1, loadCount, true);
		requestListData(list.size(), loadCount, true);
	}

	private void setPullListEmptyView(Context contenxt, String text)
	{
		emptyView = LayoutInflater.from(contenxt).inflate(R.layout.webview_error_page, null);
		TextView textview = (TextView) emptyView.findViewById(R.id.tv_errorpage);
		textview.setText(text);

		((ViewGroup) listview.getParent()).addView(emptyView);
		listview.setEmptyView(emptyView);
	}

	// 如果有缓存数据,先读缓存的数据
	protected synchronized void requestListData(int start, int count, boolean useCache)
	{
		mHandler.sendEmptyMessage(0);

		Map<String, Object> paraMap = new HashMap<String, Object>(this.params);
		paraMap.put("command_id", cmd);
		paraMap.put("start", start);
		paraMap.put("count", count);
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		String json = JsonUtil.encodeCmd(paraMap);
		parasTemp.put("string", json);
		new HttpPostAsync(BaseListActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				Log.i("json", result.toString());

				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(BaseListActivity.this).showToast("服务器异常，请联系管理员!");
					Log.e("err", "服务器异常，请联系管理员!");
					readCacheFile("list.txt");
				}
				else if (Constant.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(BaseListActivity.this).showToast("连接不上服务器");
					Log.e("err", "连接不上服务器");
					readCacheFile("list.txt");
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					Map<String, Object> headMap = JsonUtil.getMapString(mapstr.get("head").toString());
					Map<String, Object> dataMap = JsonUtil.getMapString(mapstr.get("data").toString());
					boolean isSuccess = false;
					if ("true".equals(headMap.get("success")))
						isSuccess = true;
					String desc = (String) headMap.get("desc");
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						dismissProgress();
						ToastManager.getInstance(BaseListActivity.this).showToast(desc);
						Log.e("err", desc);
						readCacheFile("list.txt");
					}
					else
					{
						List<Map<String, Object>> resultListMap = (List<Map<String, Object>>) JsonUtil.getList(dataMap.get("dataList")
								.toString());
						responseListData(isSuccess, resultListMap, desc);

						// @SuppressWarnings("unused") String json = "";
						// for (Map<String, Object> map : resultListMap)
						// {
						// json += JsonUtil.encodeCmd(map);
						// }
						cacheFileToData(JsonUtil.getJsonForList(resultListMap), "list.txt");
					}
				}
				return "";
			}
		}.execute(Constant.POST_KEYVALUE_DATA, SharedPreferencesConfig.config(BaseListActivity.this).get(Constant.MOBILE_POST_URL),
				parasTemp);

	}

	@SuppressWarnings("unchecked")
	protected void responseListData(boolean success, List<Map<String, Object>> recordList, String desc)
	{
		if (success)
		{
			list = recordList;
			if (recordList.size() > 0)
				mHandler.sendEmptyMessage(1);
			else
				mHandler.sendEmptyMessage(2);
		}
	}

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{

			switch (msg.what)
			{
				case 0:
					showProgress();
					break;
				case 1:
					dismissProgress();

					if (list != null && list.size() > 0)
					{
						adapter.setDataList(list);
						adapter.notifyDataSetChanged();
					}

					mPullToRefreshView.onRefreshComplete();
					break;
				case 2:
					mPullToRefreshView.onRefreshComplete();
					dismissProgress();
					ToastManager.getInstance(BaseListActivity.this).showToast("暂时没有更多数据");
					break;
				case 3:
					mPullToRefreshView.onRefreshComplete();
					dismissProgress();
					ToastManager.getInstance(BaseListActivity.this).showToast("获取数据失败");
					break;
				case 4:
					mPullToRefreshView.onRefreshComplete();
					dismissProgress();
					ToastManager.getInstance(BaseListActivity.this).showToast("连接网络失败!");
					break;

				default:
					break;

			}
			if (msg.what != 0 && list.size() == 0)
			{
				emptyView.setVisibility(View.VISIBLE);

			}
			else
			{
				emptyView.setVisibility(View.GONE);
			}

		}
	};

	private void cacheFileToData(String data, String fileName)
	{
		ConfigCache.setUrlCache(data, fileName);
	}

	private void readCacheFile(String fileName)
	{
		String json = ConfigCache.getUrlCache(fileName);
		List<Map<String, Object>> recordList;
		if (JsonUtil.getlistForJson(json) != null)
			recordList = JsonUtil.getlistForJson(json);
		else
			recordList = new ArrayList<Map<String, Object>>();

		list = recordList;
		if (list != null && list.size() > 0)
			mHandler.sendEmptyMessage(1);
		else
			mHandler.sendEmptyMessage(2);

	}

	private OnItemClickAvoidForceListener onItemClickAvoidForceListener = new OnItemClickAvoidForceListener()
	{
		@Override
		public void onItemClickAvoidForce(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			onClickItem(arg2, list.get(arg2));
		}
	};

	public void setShouldReLoadFlag(boolean reLoadFlag)
	{

		// this.shouldReLoad = reLoadFlag;
		// if (this.shouldReLoad)
		// {
		// for (int i = 0; i < 10; i++)
		// {
		// Map<String, Object> callParams = new HashMap<String, Object>(this.params);
		// callParams.put("start", i * loadCount + 1);
		// callParams.put("count", loadCount);
		// if (ServerEngine.removeRequestCache(this.cmd, callParams) == false)
		// break;
		// }
		// Log.d("", "FLAG----## ---reloadDate---");
		// list.clear();
		// requestListData(list.size() + 1, loadCount, true);
		// shouldReLoad = false;
		// }

	}

}
