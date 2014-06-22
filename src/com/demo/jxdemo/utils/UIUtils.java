package com.demo.jxdemo.utils;

import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UIUtils {

	public static int getTotalHeightofListView(ListView listView) {
	    ListAdapter mAdapter = listView.getAdapter(); 
	   if (mAdapter == null) {
	       return 0;
	   }
	    int totalHeight = 0;
	    for (int i = 0; i < mAdapter.getCount(); i++) {
	        View mView = mAdapter.getView(i, null, listView);
	        mView.measure(
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        //mView.measure(0, 0);
	        totalHeight += mView.getMeasuredHeight();
	        Log.w("HEIGHT" + i, String.valueOf(totalHeight));
	    }
//	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    int totalH = totalHeight           + (listView.getDividerHeight() * (mAdapter.getCount()-1));
//	    params.height = totalH;
//	    listView.setLayoutParams(params);
//	    listView.requestLayout();    
	    return totalH;
	}
}
