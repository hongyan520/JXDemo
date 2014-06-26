package com.demo.jxdemo.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UIUtils
{

	/**
	 * 根据手机的分辨率从dp的单位转成为px(像素)
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从px(像素)的单位转成dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getTotalHeightofListView(ListView listView)
	{
		ListAdapter mAdapter = listView.getAdapter();
		if (mAdapter == null)
		{
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0; i < mAdapter.getCount(); i++)
		{
			View mView = mAdapter.getView(i, null, listView);
			mView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			// mView.measure(0, 0);
			totalHeight += mView.getMeasuredHeight();
			Log.w("HEIGHT" + i, String.valueOf(totalHeight));
		}
		// ViewGroup.LayoutParams params = listView.getLayoutParams();
		int totalH = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
		// params.height = totalH;
		// listView.setLayoutParams(params);
		// listView.requestLayout();
		return totalH;
	}
}
