package com.demo.jxdemo.ui.customviews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.demo.jxdemo.constant.Constant;

public class MyViewPager extends ViewPager
{

	public MyViewPager(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0)
	{
		if (Constant.ISITEMSCROLL)
			return false;// 拦截分发给子控件
		return super.onInterceptTouchEvent(arg0);
	}

	// 这个构造函数必须有
	public MyViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

}
