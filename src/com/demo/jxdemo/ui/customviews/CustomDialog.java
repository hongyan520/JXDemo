package com.demo.jxdemo.ui.customviews;

import java.util.Iterator;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;

public class CustomDialog extends Dialog
{
	private Context context;

	private View view;

	private String title;

	private Map<String, android.view.View.OnClickListener> btnMap;

	private String lastBtnName;

	android.view.View.OnClickListener lastBtnOnclick;

	/**
	 * 通用按钮 Dialog
	 * 
	 * @param context
	 * @param title
	 *            标题描述(传入空字符串即没有)
	 * @param btnMap
	 *            多个按钮传入方式(key作为按钮名称,value作为按钮点击事件)
	 */
	public CustomDialog(Context context, String title, Map<String, android.view.View.OnClickListener> btnMap, String lastBtnName,
			android.view.View.OnClickListener lastBtnOnclick)
	{
		super(context, R.style.NoBgDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		this.title = title;
		this.btnMap = btnMap;
		this.lastBtnName = lastBtnName;
		this.lastBtnOnclick = lastBtnOnclick;
		init();
	}

	private void init()
	{

		view = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
		TextView titleTextView = (TextView) view.findViewById(R.id.text_custom_title);
		Button lastBtn = (Button) view.findViewById(R.id.btn_custom_last);
		if (StringUtil.isBlank(lastBtnName))
			lastBtn.setVisibility(View.GONE);
		else
		{
			lastBtn.setText(lastBtnName);
			lastBtn.setOnClickListener(lastBtnOnclick);
		}
		if (StringUtil.isBlank(title))
		{
			titleTextView.setVisibility(View.GONE);
			((ImageView) view.findViewById(R.id.img_custom_line)).setVisibility(View.GONE);
		}
		else
			titleTextView.setText(title);

		LinearLayout customBtnsView = (LinearLayout) view.findViewById(R.id.layout_custom_btn);
		Iterator<String> iterator = btnMap.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String) iterator.next();
			View childView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_button, null);
			Button btn = (Button) childView.findViewById(R.id.btn_custom_dynamic);
			btn.setText(key);
			btn.setOnClickListener((android.view.View.OnClickListener) btnMap.get(key));

			ImageView img = (ImageView) childView.findViewById(R.id.img_custom_line);
			if (!iterator.hasNext())
				img.setVisibility(View.GONE);
			customBtnsView.addView(childView);
		}

		this.setDialogView(view);
		this.setDialogGravity(Gravity.BOTTOM);
		this.setDialogLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		setCanceledOnTouchOutside(true);
		getWindow().setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
