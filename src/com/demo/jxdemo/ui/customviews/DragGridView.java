package com.demo.jxdemo.ui.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.adapter.ItemMenuApapter;

public class DragGridView extends GridView
{

	private int dragPosition; // 开始拖拽的位置

	private int dropPosition; // 结束拖拽的位置

	private int dragPointX; // 相对于item的x坐标

	private int dragPointY; // 相对于item的y坐标

	private int dragOffsetX;

	private int dragOffsetY;

	private ImageView dragImageView; // 拖动item的preview

	private WindowManager windowManager;

	private WindowManager.LayoutParams windowParams;

	private int itemHeight;

	private final int numColumns = 4; // 每行4列

	private Context context;

	public DragGridView(Context context)
	{
		super(context);
		this.context = context;

	}

	public void setOnItemLongClickListener(final MotionEvent ev, int xx, int yy)
	{
		final int x = xx;
		final int y = yy;
		this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				Constant.ISITEMSCROLL = true;
				dragPosition = dropPosition = arg2;

				if (dragPosition == AdapterView.INVALID_POSITION)
				{

				}
				ViewGroup itemView = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());

				// 得到当前点在item内部的偏移量 即相对于item左上角的坐标
				dragPointX = 0;
				dragOffsetY = 0;
				dragPointX = x - itemView.getLeft();
				dragPointY = y - itemView.getTop();

				dragOffsetX = (int) (ev.getRawX() - x);
				dragOffsetY = (int) (ev.getRawY() - y);

				itemHeight = itemView.getHeight();

				// 解决问题3
				// 每次都销毁一次cache，重新生成一个bitmap
				itemView.destroyDrawingCache();
				itemView.setDrawingCacheEnabled(true);
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
				// 建立item的缩略图
				startDrag(bm, x, y);
				return true;
			};
		});
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if (ev.getAction() == MotionEvent.ACTION_DOWN)
		{
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			setOnItemLongClickListener(ev, x, y);
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void startDrag(Bitmap bm, int x, int y)
	{
		stopDrag();
		windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP | Gravity.LEFT;// 这个必须加
		// 得到preview左上角相对于屏幕的坐标
		windowParams.x = x - dragPointX + dragOffsetX;
		windowParams.y = y - dragPointY + dragOffsetY;
		// 设置宽和高
		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

		windowParams.format = PixelFormat.TRANSLUCENT;
		windowParams.windowAnimations = 0;

		ImageView iv = new ImageView(getContext());
		iv.setImageBitmap(bm);
		windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);// "window"
		windowManager.addView(iv, windowParams);
		dragImageView = iv;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{

		if (dragImageView != null && dragPosition != AdapterView.INVALID_POSITION)
		{
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			switch (ev.getAction())
			{
				case MotionEvent.ACTION_MOVE:
					onDrag(x, y);
					break;
				case MotionEvent.ACTION_UP:
					stopDrag();
					onDrop(x, y);
					setEnabled(true);
					break;
			}
		}
		return super.onTouchEvent(ev);
	}

	private void onDrag(int x, int y)
	{
		if (dragImageView != null)
		{
			windowParams.alpha = 0.6f;
			windowParams.x = x - dragPointX + dragOffsetX;
			windowParams.y = y - dragPointY + dragOffsetY;
			windowManager.updateViewLayout(dragImageView, windowParams);
		}

		int tempScrollX = x - dragPointX + dragOffsetX;
		int tempScrollY = y - dragPointY + dragOffsetY;

		int rangeY = itemHeight;
		int maxHeight = getHeight() - rangeY;

		int position = pointToPosition(x, y);

		int gridViewCount = this.getCount();
		int allContainCount = gridViewCount;
		int leftCount = gridViewCount % numColumns;
		if (leftCount != 0)
		{
			allContainCount += (numColumns - leftCount);
		}
		int upMaxPosition = allContainCount - numColumns;

		// 如果position大于最大的item
		if (position >= upMaxPosition || position < numColumns)
		{
			// viewFlipper.showNext();
			setEnabled(false);
		}
		else
		{

			if (tempScrollY < rangeY)// 假如漂浮的view已经进入第一行,则把当前的gridView滑出一个
			{
				setEnabled(true);
				int position2 = getFirstVisiblePosition();
				// smoothScrollToPosition(position2 - 1);
			}
			else if (tempScrollY > maxHeight)
			{
				setEnabled(true);
				int position1 = getLastVisiblePosition();
				// smoothScrollToPosition(position1 + 1);
			}
		}

	}

	private void onDrop(int x, int y)
	{
		Constant.ISITEMSCROLL = false;
		int tempPosition = pointToPosition(x, y);
		if (tempPosition != AdapterView.INVALID_POSITION)
		{
			dropPosition = tempPosition;
		}

		if (dropPosition != dragPosition)
		{
			int pageNumHow = Constant.PAGENUMHOW.intValue();
			int pageSize = Constant.PAGESIZE.intValue();

			ItemMenuApapter adapter = (ItemMenuApapter) this.getAdapter();
			adapter.exchange(pageNumHow * pageSize + dragPosition, pageNumHow * pageSize + dropPosition);

		}
	}

	private void stopDrag()
	{
		if (dragImageView != null)
		{
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}
}
