package com.demo.jxdemo.ui.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

	private TextView tView;

	public TextView gettView()
	{
		return tView;
	}

	public void settView(TextView tView)
	{
		this.tView = tView;
	}

	private View view;

	public CustomProgressDialog(Context context)
	{
		super(context);
		this.mContext = context;
	}
	
	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

	/**
	 * 创建DIALOG
	 * 
	 * @param context
	 * @return
	 */
	public CustomProgressDialog createDialog(Context context)
	{
		
		view = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null);
		tView = (TextView) view.findViewById(R.id.tv_loadingmsg);
		this.setDialogView(view);
		
//		this.setDialogGravity(Gravity.CENTER);
//		this.setDialogLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		setCanceledOnTouchOutside(false);
		getWindow().setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		return this;
	}

	public void setDialogView(int layoutId)
	{

		setContentView(layoutId);

	}

	public void setDialogView(View view)
	{

		setContentView(view);

	}

	public void setDialogGravity(int gravity)
	{
		getWindow().setGravity(gravity); // 此处可以设置dialog显示的位置
	}

	public void setDialogLayout(int w, int h)
	{
		getWindow().setLayout(w, h);
	}
}
