package com.demo.jxdemo.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

import com.demo.jxdemo.R;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.customviews.MyViewPager;

/**
 * ViewPager适配器
 */
@SuppressLint("ResourceAsColor")
public class MainTabPagerAdapter extends PagerAdapter implements OnPageChangeListener
{
	public List<View> mListViews;

	private Context context;

	private int pageNum;

	public int cruurentItem = 0;

	private ViewPager viewPager;

	public MainTabPagerAdapter(Context context, List<View> mListViews, int pageNum, ViewPager viewPager)
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

		// LinearLayout bottomIndex = (LinearLayout) ((Activity) context).findViewById(R.id.bottomIndex);
		// bottomIndex.setTextColor(context.getResources().getColor(R.color.red));
		// bottomIndex.setBackgroundColor(android.R.color.transparent);
		// bottomIndex.removeAllViews();

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

		if (arg0 == 0)
		{
			// // 第一个
			setBtnColor(MainActivity.btn1);
		}
		else if (arg0 == pageNum - 1)
		{
			// 最后一个
			setBtnColor(MainActivity.btn4);
		}
		else if (arg0 == pageNum - 2)
		{
			// 倒数第二个
			setBtnColor(MainActivity.btn3);
		}
		else
		{
			// 中间的
			setBtnColor(MainActivity.btn2);
		}

		initBottomBar();
	}

	public void setBtnColor(Button btn)
	{
		MainActivity.btn1.setTextColor(context.getResources().getColor(R.color.black));
		MainActivity.btn2.setTextColor(context.getResources().getColor(R.color.black));
		MainActivity.btn3.setTextColor(context.getResources().getColor(R.color.black));
		MainActivity.btn4.setTextColor(context.getResources().getColor(R.color.black));
		switch (btn.getId())
		{
			case R.id.btn_1:
				MainActivity.btn1.setTextColor(context.getResources().getColor(R.color.red));
				break;
			case R.id.btn_2:
				MainActivity.btn2.setTextColor(context.getResources().getColor(R.color.red));
				break;
			case R.id.btn_3:
				MainActivity.btn3.setTextColor(context.getResources().getColor(R.color.red));
				break;
			case R.id.btn_4:
				MainActivity.btn4.setTextColor(context.getResources().getColor(R.color.red));
				break;
			default:
				break;
		}
	}
}
