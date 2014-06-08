package com.demo.jxdemo.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.demo.base.util.AsyncTaskUtil;

public abstract class BaseFragment extends Fragment
{

	/**
	 * 数据保存
	 */
	protected Object dataObj;

	/**
	 * 布局解析器
	 */
	protected LayoutInflater inflater;

	protected boolean isShowDialg = false;

	protected boolean isCanCancel = true;

	protected String dialogText = "正在处理,请稍候...";

	protected Object paraData = null;

	private boolean isBaseOnCreate = true;

    protected ProgressDialog myProgressDialog;

    /**
     * 暂时不做对话框的可取消性，不需要阻碍UI则设置isShowdialog为false，否则视为阻碍UI线程 必须等后台操作执行完之后才释放UI
     */
    protected void showProgress()
    {
        if (myProgressDialog == null)
        {
            myProgressDialog = new ProgressDialog(getActivity());
            myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            myProgressDialog.setMessage("正在加载数据");
            myProgressDialog.setCancelable(true);
            myProgressDialog.setCanceledOnTouchOutside(false);
        }

        if (!myProgressDialog.isShowing())
        {
            myProgressDialog.show();
        }

    }

    /**
     * dialog的消息由于异步线程执行返回后，UI可能由于其他异常导致已经销毁，此时dialog的dismss会出现
     * attach的归属错误，对其进行异常捕捉。
     */
    protected void closeProgress()
    {
        try
        {
            if (myProgressDialog != null && myProgressDialog.isShowing())
            {
                myProgressDialog.dismiss();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    // 如果要更改fragment的dialog的相关数据，可以在其实现类里面重写onCreate()
	// 方法，直接修改上面三个数据,然后super.onCreate()执行里面的异步数据操作
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(getActivity());

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 进行数据的加载操作
		new AsyncTaskUtil(getActivity(), dialogText, isShowDialg, isCanCancel, paraData)
		{

			@Override
			protected Object backResult(Object result)
			{

				return onEndTaskAddView(result);

			}

			@Override
			protected Object backDataProcess(Object... params)
			{
				dataObj = onCreateTaskLoadData(params);
				return dataObj;
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO 创建和fragment关联的view hierarchy.
		return new View(getActivity());

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//onEndTaskAddView(dataObj);

	}

	/**
	 * 数据下载，得到的返回结果用于初始化成员变量 object
	 * 
	 * @return
	 */
	protected abstract Object onCreateTaskLoadData(Object... params);

	/**
	 * 创建并初始化视图
	 */
	protected abstract Object onEndTaskAddView(Object result);


	@Override
	public void onResume()
	{
		super.onResume();
		if (isBaseOnCreate)
		{
			isBaseOnCreate = false;
		}
		else
		{
			onBaseResume();
		}
	}

	/**
	 * onCreate进入时不调用 用于onResume()处理
	 */
	protected void onBaseResume()
	{

	}

}
