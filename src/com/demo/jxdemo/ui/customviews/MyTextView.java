package com.demo.jxdemo.ui.customviews;

import android.content.Context;
import android.text.ClipboardManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyTextView extends TextView {
	private Context mContext;

	public MyTextView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	private void init(){
		this.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				ClipboardManager cm = (ClipboardManager) mContext
						.getSystemService(Context.CLIPBOARD_SERVICE);
				
				// 将文本数据复制到剪贴板
				cm.setText(getText().toString());
				Toast.makeText(mContext, "已经复制：" + getText().toString(), Toast.LENGTH_SHORT).show();
				return false;
			}});
	}

}
