package com.demo.jxdemo.ui.customviews;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.demo.jxdemo.R;

public class MyEditText extends EditText {
	private Drawable drawableRight;
	private Rect rBounds;
	
	/**
	 * 是否不允许删除内容
	 */
	private boolean notAllowDelete = false;

	public void setNotAllowDelete(boolean notAllowDelete) {
		this.notAllowDelete = notAllowDelete;
	}

	public MyEditText(Context context) {
		super(context);
		init();
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyEditText(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		drawableRight = getResources().getDrawable(R.drawable.delete);
		
		this.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(notAllowDelete){
					return ;
				}
				if (arg1){
					setCompoundDrawablesWithIntrinsicBounds(null, null,
							drawableRight, null);
				} else {
					setCompoundDrawablesWithIntrinsicBounds(null, null,
							null, null);
				}
			}
			
		});
		
	}

	@Override
	public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
			Drawable top, Drawable right, Drawable bottom) {
		if (right != null) {
			drawableRight = right;
		}

		super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP && drawableRight != null) {
			rBounds = drawableRight.getBounds();
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			if (x >= (this.getRight() - rBounds.width() - 30)
					&& x <= (this.getRight() - this.getPaddingRight() + 30)
					&& y >= this.getPaddingTop() - 30
					&& y <= (this.getHeight() - this.getPaddingBottom() + 30)) {
				
				this.setText("");
				// use this to prevent the keyboard from coming up
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		drawableRight = null;
		rBounds = null;
		super.finalize();
	}

}
