package com.demo.jxdemo.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

public class SpannableUtil
{
	public static void spannable(String showText, String url, TextView textView)
	{
		SpannableString sp = new SpannableString(showText);
		// 设置超链接
		sp.setSpan(new URLSpan(url), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// SpannableString对象设置给TextView
		textView.setText(sp);
		// 设置TextView可点击
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
