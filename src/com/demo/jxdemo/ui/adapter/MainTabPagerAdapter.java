package com.demo.jxdemo.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.demo.jxdemo.R;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.main.MainActivity;

/**
 * ViewPager适配器
 */
@SuppressLint("ResourceAsColor")
public class MainTabPagerAdapter extends PagerAdapter implements OnPageChangeListener
{
	public List<View> mListViews;

	private Context context;

	private int pageNum;

	// public int cruurentItem = 0;

	private ViewPager viewPager;

	private Animation animation = null;

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
		((ViewPager) arg0).removeView(mListViews.get(arg1));
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
		// try
		// {
		//
		// ((MyViewPager) arg0).addView(mListViews.get(arg1), 0);
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		// return mListViews.get(arg1);
		//

		try
		{
			if (mListViews.get(arg1).getParent() == null)
				((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			else
			{
				// 很难理解新添加进来的view会自动绑定一个父类，由于一个儿子view不能与两个父类相关，所以得解绑
				// 不这样做否则会产生 viewpager java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's
				// parent first.
				// 还有一种方法是viewPager.setOffscreenPageLimit(3); 这种方法不用判断parent 是不是已经存在，但多余的listview不能被destroy
				((ViewGroup) mListViews.get(arg1).getParent()).removeView(mListViews.get(arg1));
				((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			Log.d("parent=", "" + mListViews.get(arg1).getParent());
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
		Constant.PAGENUMHOW = MainActivity.cruurentItem; // 减去左边的空白页
		viewPager.setCurrentItem(arg0);// 第一屏向左滑动的话.

		int one = 2 * MainActivity.offset + MainActivity.cursorWidth;
		int two = 2 * one;
		int three = 3 * one;

		switch (MainActivity.cruurentItem)
		{
			case 0:
				if (arg0 == 0)
				{
					setBtnColor(MainActivity.btn1);
					animation = new TranslateAnimation(0, 0, 0, 0);
				}
				if (arg0 == 1)
				{
					setBtnColor(MainActivity.btn2);
					animation = new TranslateAnimation(0, one, 0, 0);
				}
				break;
			case 1:
				if (arg0 == 2)
				{
					setBtnColor(MainActivity.btn3);
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				if (arg0 == 1)
				{
					setBtnColor(MainActivity.btn2);
					animation = new TranslateAnimation(0, one, 0, 0);
				}
				if (arg0 == 0)
				{
					setBtnColor(MainActivity.btn1);
					animation = new TranslateAnimation(0, 0, 0, 0);
				}
				break;
			case 2:
				if (arg0 == 3)
				{
					setBtnColor(MainActivity.btn4);
					animation = new TranslateAnimation(two, three, 0, 0);
				}
				if (arg0 == 2)
				{
					setBtnColor(MainActivity.btn3);
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				if (arg0 == 1)
				{
					setBtnColor(MainActivity.btn2);
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 3:
				if (arg0 == 2)
				{
					setBtnColor(MainActivity.btn3);
					animation = new TranslateAnimation(three, two, 0, 0);
				}
//				if (arg0 == 3)
//				{
//					setBtnColor(MainActivity.btn4);
//					animation = new TranslateAnimation(two, three, 0, 0);
//				}
				break;

			default:
				break;
		}

		// if (arg0 == 0)
		// {
		// // // 第一个
		// setBtnColor(MainActivity.btn1);
		// animation = new TranslateAnimation(0, 0, 0, 0);
		// }
		// else if (arg0 == 1)
		// {
		// // 中间的
		// setBtnColor(MainActivity.btn2);
		// animation = new TranslateAnimation(0, one, 0, 0);
		//
		// }
		// else if (arg0 == 2)
		// {
		// // 倒数第二个
		// setBtnColor(MainActivity.btn3);
		// animation = new TranslateAnimation(one, two, 0, 0);
		//
		// }
		// else if (arg0 == 3)
		// {
		// // 最后一个
		// setBtnColor(MainActivity.btn4);
		// animation = new TranslateAnimation(two, three, 0, 0);
		// }
		//
		animation.setFillAfter(true);
		animation.setDuration(300);
		MainActivity.cursor.startAnimation(animation);
		MainActivity.cruurentItem = arg0; // 减去左边的空白页
		initBottomBar();
	}

	public void setBtnColor(Button btn)
	{
		MainActivity.btn1.setTextAppearance(context, R.style.textview_gray16_717171);
		MainActivity.btn2.setTextAppearance(context, R.style.textview_gray16_717171);
		MainActivity.btn3.setTextAppearance(context, R.style.textview_gray16_717171);
		MainActivity.btn4.setTextAppearance(context, R.style.textview_gray16_717171);
		switch (btn.getId())
		{
			case R.id.btn_1:
				MainActivity.btn1.setTextAppearance(context, R.style.textview_red16_ee4136);
				break;
			case R.id.btn_2:
				MainActivity.btn2.setTextAppearance(context, R.style.textview_red16_ee4136);
				break;
			case R.id.btn_3:
				MainActivity.btn3.setTextAppearance(context, R.style.textview_red16_ee4136);
				break;
			case R.id.btn_4:
				MainActivity.btn4.setTextAppearance(context, R.style.textview_red16_ee4136);
				break;
			default:
				break;
		}
	}
}
