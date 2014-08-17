package com.demo.jxdemo.ui.fragment.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.adapter.main.AllListAdapter;
import com.demo.jxdemo.ui.fragment.BaseFragment;
import com.demo.jxdemo.utils.ToastManager;

public class CollectionFragment extends BaseFragment
{

	private final static String TAG = "CollectionFragment";

	Activity activity;

	String text;

	public final static int SET_NEWSLIST = 0;

	private AllListAdapter adapter = null;

	private ListView listView;

	private List<Map<String, Object>> list;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		request();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.test_main_list, null);
		listView = (ListView) view.findViewById(R.id.listView1);

		// initView();
		return view;
	}

	private void initView()
	{
		adapter = new AllListAdapter(getActivity(), list);
		listView.setAdapter(adapter);
	}

	private void request()
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("UserToken", SharedPreferencesConfig.config(getActivity()).get(Constant.USER_TOKEN));
		parasTemp.put("FragmentType", "3");

		new HttpPostAsync(getActivity())
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				// System.out.println("CourseList=" + result);
				if (result == null || "".equals(result.toString()))
				{
					ToastManager.getInstance(getActivity()).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					ToastManager.getInstance(getActivity()).showToast("连接不上服务器");
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
						ToastManager.getInstance(getActivity()).showToast(desc);
					}
					else
					{
						// 成功后处理
						String data = mapstr.get("Fragments").toString();// mapstr.get("Fragments").toString().replace("\"", "\\\"");
						SharedPreferencesConfig.saveConfig(getActivity(), Constant.USER_TEST, data);
						list = JsonUtil.getList(data);
						mHandler.sendEmptyMessage(1);
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.LEARNINGFRAGMENTS, parasTemp);

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
			}
		}
	};

	/* 摧毁视图 */
	@Override
	public void onDestroyView()
	{
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	/* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onBaseResume()
	{
		request();
		super.onBaseResume();
	}

	@Override
	protected Object onCreateTaskLoadData(Object... params)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object onEndTaskAddView(Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
