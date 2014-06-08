package com.demo.jxdemo.ui.customviews;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.demo.jxdemo.R;

/**
 * CustomProgressDialog
 * 
 * @author 刘尹 <br/>
 * @说明 通用DAILOG
 * @创建日期 2012-12-21
 */
public class CustomProgressDialog extends Dialog
{

	/**
	 * 上下文
	 */
	private Context mContext = null;

	/**
	 * DAILOG对象
	 */
	private static CustomProgressDialog mCustomProgressDialog = null;

	public CustomProgressDialog(Context context)
	{
		super(context);
		this.mContext = context;
	}

	public CustomProgressDialog(Context context, int theme)
	{
		super(context, theme);
	}

	/**
	 * 创建DIALOG
	 * 
	 * @param context
	 * @return
	 */
	public static CustomProgressDialog createDialog(Context context)
	{
		mCustomProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
		mCustomProgressDialog.setContentView(R.layout.custom_progress_dialog);
		mCustomProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		mCustomProgressDialog.setCancelable(false);
		return mCustomProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus)
	{

		if (mCustomProgressDialog == null)
		{
			return;
		}

	}

	/**
	 * [Summary]
	 * setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 */
	public CustomProgressDialog setMessage(String strMessage)
	{
		TextView tvMsg = (TextView) mCustomProgressDialog.findViewById(R.id.tv_loadingmsg);

		if (tvMsg != null)
		{
			tvMsg.setText(strMessage);
		}

		return mCustomProgressDialog;
	}

	public CustomProgressDialog changeMessage(String strMessage)
	{
		TextView tvMsg = (TextView) mCustomProgressDialog.findViewById(R.id.tv_loadingmsg);

		if (tvMsg != null)
		{
			tvMsg.setText(strMessage);
		}

		return mCustomProgressDialog;
	}
}
