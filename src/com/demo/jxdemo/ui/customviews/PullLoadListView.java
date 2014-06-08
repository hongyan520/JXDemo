package com.demo.jxdemo.ui.customviews;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.jxdemo.R;

/**
 * 上拉加载更多
 */
public class PullLoadListView extends ListView implements OnScrollListener
{

	private final static int RELEASE_To_REFRESH = 0;

	private final static int PULL_To_REFRESH = 1;

	// 正在刷新
	private final static int REFRESHING = 2;

	// 刷新完成
	private final static int DONE = 3;

	private final static int LOADING = 4;

	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;

	// private LinearLayout footView;

	private TextView tipsTextview;

	private TextView lastUpdatedTextView;

	// private ImageView arrowImageView;
	private ProgressBar progressBar;

	// private RotateAnimation animation;
	// private RotateAnimation reverseAnimation;
	private boolean isRecored;

	private int headContentWidth;

	private int headContentHeight;

	private int startY;

	// private int firstItemIndex;
	private int state;

	private boolean isBack;

	private OnRefreshListener refreshListener;

	private boolean isRefreshable;

	private boolean isLastRow = false;

	private int listCount = 0;

	public PullLoadListView(Context context)
	{
		super(context);
		init(context);
	}

	public PullLoadListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	private void init(Context context)
	{
		// setCacheColorHint(context.getResources().getColor(R.color.transparent));
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.head, null);

		// footView =(LinearLayout) inflater.inflate(R.layout.layout_listview_loadmore_item,null);

		// arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		// arrowImageView.setMinimumWidth(70);
		// arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);

		// measureView(footView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		// footView.invalidate();

		// addHeaderView(headView, null, false);

		addFooterView(headView, null, false);
		setOnScrollListener(this);

		// animation = new RotateAnimation(0, -180,RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// animation.setInterpolator(new LinearInterpolator());
		// animation.setDuration(250);
		// animation.setFillAfter(true);
		//
		// reverseAnimation = new RotateAnimation(-180, 0,RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// reverseAnimation.setInterpolator(new LinearInterpolator());
		// reverseAnimation.setDuration(200);
		// reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
		// footView.setVisibility(View.GONE);
	}

	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		// firstItemIndex = firstVisibleItem;
		listCount = totalItemCount;
		if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0)
		{
			isLastRow = true;
		}
		else
		{
			isLastRow = false;
		}

	}

	public void onScrollStateChanged(AbsListView arg0, int arg1)
	{
	}

	public boolean onTouchEvent(MotionEvent event)
	{

		Log.d("", "FLAG---onTouchEvent=" + state);

		if (isRefreshable)
		{
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					if (isLastRow && !isRecored)
					{
						isRecored = true;
						startY = (int) event.getY();

					}
					break;
				case MotionEvent.ACTION_UP:
					if (state != REFRESHING && state != LOADING)
					{
						if (state == DONE)
						{
						}
						if (state == PULL_To_REFRESH)
						{
							state = DONE;

							changeHeaderViewByState();

						}
						if (state == RELEASE_To_REFRESH)
						{
							state = REFRESHING;

							changeHeaderViewByState();
							onRefresh();

						}
					}
					isRecored = false;
					isBack = false;
					break;
				case MotionEvent.ACTION_MOVE:
					int tempY = (int) event.getY();
					if (!isRecored && isLastRow)
					{
						isRecored = true;
						startY = tempY;

					}
					if (state != REFRESHING && isRecored && state != LOADING)
					{
						if (state == RELEASE_To_REFRESH)
						{
							setSelection(listCount);
							if (((startY - tempY) / RATIO < headContentHeight) && (tempY - startY) < 0)
							{
								state = PULL_To_REFRESH;
								changeHeaderViewByState();

							}
							else if (startY - tempY <= 0)
							{
								state = DONE;
								changeHeaderViewByState();

							}
						}
						if (state == PULL_To_REFRESH)
						{
							setSelection(listCount);
							if ((startY - tempY) / RATIO >= headContentHeight)
							{
								state = RELEASE_To_REFRESH;
								isBack = true;

								changeHeaderViewByState();

							}
							else if (startY - tempY <= 0)
							{
								state = DONE;
								changeHeaderViewByState();

							}
						}
						if (state == DONE)
						{
							if (startY - tempY > 0)
							{
								state = PULL_To_REFRESH;
								changeHeaderViewByState();

							}
						}
						if (state == PULL_To_REFRESH)
						{
							headView.setPadding(0, 0, 0, -1 * headContentHeight + (startY - tempY) / RATIO);

						}
						if (state == RELEASE_To_REFRESH)
						{
							headView.setPadding(0, 0, 0, (startY - tempY) / RATIO - headContentHeight);

						}
					}
					break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void changeHeaderViewByState()
	{

		Log.d("", "FLAG---changeHeaderViewByState=" + state);
		switch (state)
		{
			case RELEASE_To_REFRESH:
				// arrowImageView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				// arrowImageView.clearAnimation();
				// arrowImageView.startAnimation(animation);
				tipsTextview.setText("请释放 刷新");

				break;
			case PULL_To_REFRESH:
				progressBar.setVisibility(View.GONE);
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				// arrowImageView.clearAnimation();
				// arrowImageView.setVisibility(View.VISIBLE);
				if (isBack)
				{
					isBack = false;
					// arrowImageView.clearAnimation();
					// arrowImageView.startAnimation(reverseAnimation);
					tipsTextview.setText("请释放 刷新");
				}
				else
				{
					tipsTextview.setText("继续拉可刷新");
				}

				break;
			case REFRESHING:
				headView.setPadding(0, 0, 0, 0);
				progressBar.setVisibility(View.VISIBLE);
				// arrowImageView.clearAnimation();
				// arrowImageView.setVisibility(View.GONE);
				tipsTextview.setText("正在加载中 ...");
				lastUpdatedTextView.setVisibility(View.VISIBLE);

				break;
			case DONE:
				headView.setPadding(0, -1 * headContentHeight, 0, 0);
				progressBar.setVisibility(View.GONE);
				// arrowImageView.clearAnimation();
				// arrowImageView.setImageResource(R.drawable.arrow);
				tipsTextview.setText("已经加载完毕 ");
				lastUpdatedTextView.setVisibility(View.VISIBLE);

				break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener)
	{
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener
	{
		public void onRefresh();
	}

	public void onRefreshComplete()
	{
		state = DONE;
		lastUpdatedTextView.setText("上次更新: " + new Date().toLocaleString());
		changeHeaderViewByState();

	}

	private void onRefresh()
	{
		if (refreshListener != null)
		{
			refreshListener.onRefresh();

		}
	}

	private void measureView(View child)
	{
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null)
		{
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0)
		{
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		}
		else
		{
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter)
	{
		lastUpdatedTextView.setText("刷新加载:" + new Date().toLocaleString());
		super.setAdapter(adapter);
	}

}
