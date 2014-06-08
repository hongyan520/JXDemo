package com.demo.jxdemo.ui.customviews;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.R;

@SuppressLint("ResourceAsColor")
public class MainPagerAdapter extends PagerAdapter implements OnPageChangeListener
{

	public List<View> mListViews;

	private Context context;

	private int pageNum;

	public int cruurentItem = 0;

	private ViewPager viewPager;

	/**
	 * 标值，0为主界面使用，1,介绍界面使用
	 */
	private int flag = 0;

	public void setTypeFlag(int f)
	{
		flag = f;
	}

	public MainPagerAdapter(Context context, List<View> mListViews, int pageNum, ViewPager viewPager)
	{
		this.mListViews = mListViews;
		this.context = context;
		this.pageNum = pageNum;
		this.viewPager = viewPager;
		initBottomBar();
	}

	private void initBottomBar()
	{

		if (mListViews.size() == 0)
		{
			return;
		}

		LinearLayout bottomIndex = (LinearLayout) ((Activity) context).findViewById(R.id.bottomIndex);
//		bottomIndex.setVisibility(View.VISIBLE);
		bottomIndex.setBackgroundColor(R.color.transparent);
		bottomIndex.removeAllViews();

		if (mListViews.size() > 1)
		{ // 屏幕大于1时加载底部的屏幕按钮
			bottomIndex.setVisibility(View.VISIBLE);
			for (int i = 0; i < pageNum; i++)
			{
				ImageView imageView;
				imageView = new ImageView(context);
				imageView.setLayoutParams(new GridView.LayoutParams(40, 40));
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);//
				if (cruurentItem == i)
				{
					imageView.setImageResource(R.drawable.home_unpoint);
				}
				else
				{
					imageView.setImageResource(R.drawable.home_selectpoint);
				}
				bottomIndex.addView(imageView, new LinearLayout.LayoutParams(30, 30));
			}
		}
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2)
	{

	}

	@Override
	public void finishUpdate(View arg0)
	{
	}

	@Override
	public int getCount()
	{
		return mListViews == null ? 0 : mListViews.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1)
	{
		try
		{

			((MyViewPager) arg0).addView(mListViews.get(arg1), 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mListViews.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{
	}

	@Override
	public Parcelable saveState()
	{
		return null;
	}

	@Override
	public void startUpdate(View arg0)
	{
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0)
	{

		cruurentItem = arg0; // 减去左边的空白页
		Constant.PAGENUMHOW = cruurentItem; // 减去左边的空白页
		viewPager.setCurrentItem(arg0);// 第一屏向左滑动的话.
		initBottomBar();
	}

}
