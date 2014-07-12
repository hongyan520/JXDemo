package com.demo.jxdemo.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.main.LearningMaterialsActivity;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.activity.menu.ViewWebViewActivity;

public class SwitherImageAdapter extends BaseAdapter
{
	private List<String> linkLists;

	private Context context;

	private SwitherImageAdapter self;

	Uri uri;

	Intent intent;

	ImageView imageView;

	public static List<Bitmap> bitmaps;

	public SwitherImageAdapter(/* List<String> imageUrls, */Context context)
	{
		// this.imageUrls = imageUrls;
		this.context = context;
		this.self = this;
	}

	public void setImg(List<Bitmap> bitmaps, List<String> linkLists)
	{
		this.bitmaps = bitmaps;
		this.linkLists = linkLists;
	}

	public int getCount()
	{
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position)
	{
		return linkLists.get(position % bitmaps.size());
	}

	public long getItemId(int position)
	{
		return position;
	}

	@SuppressWarnings("unused")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			try
			{
				switch (msg.what)
				{
					case 0:
					{
						self.notifyDataSetChanged();
					}
						break;
				}

				super.handleMessage(msg);
			}
			catch (Exception e)
			{
			}
		}
	};

	public View getView(int position, View convertView, ViewGroup parent)
	{
		final String link = StringUtil.Object2String(linkLists.get(position % linkLists.size()));
		// Bitmap image;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_imageswither_item, null); // 实例化convertView
			Gallery.LayoutParams params = new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT);
			convertView.setLayoutParams(params);
			// image = ((ImageActivity)context).imagesCache.get(imageUrls.get(position % imageUrls.size())); //从缓存中读取图片
			/*
			 * if(image==null){
			 * //当缓存中没有要使用的图片时，先显示默认的图片
			 * image = ((ImageActivity)context).imagesCache.get("background_non_load");
			 * //异步加载图片
			 * LoadImageTask task = new LoadImageTask(convertView);
			 * task.execute(imageUrls.get(position % imageUrls.size()));
			 * }
			 */

			convertView.setTag(bitmaps);

		}
		else
		{
			// image = (Bitmap) convertView.getTag();
		}
		imageView = (ImageView) convertView.findViewById(R.id.gallery_image);
		// imageView.setImageResource(imgs[position % imgs.length]);
		imageView.setImageBitmap(bitmaps.get(position % bitmaps.size()));
		// 设置缩放比例：保持原样
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// textView.setText(position % imgs.length+" sdfsdfdsfdf");
		// ((SwitherImageActivity) context).changePointView(position % imgs.length);
		((MainActivity) context).changePointView(position % bitmaps.size());
		/*
		 * imageView.setOnClickListener(new OnClickListener() {
		 * @Override
		 * public void onClick(View v) {
		 * // TODO Auto-generated method stub
		 * //((String) imageView).setSpan(new URLSpan("http://www.36939.net/"), 13, 15,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * }
		 * });
		 */
		// imageView.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// if (!StringUtil.isBlank(link))
		// {
		// Intent intent = new Intent();
		// intent.putExtra("url", link);
		// intent.setClass(context, ViewWebViewActivity.class);
		// context.startActivity(intent);
		// }
		// }
		// });
		return convertView;

	}

	/*
	 * //加载图片的异步任务
	 * class LoadImageTask extends AsyncTask<String,Void,Bitmap>{
	 * private View resultView;
	 * LoadImageTask(View resultView) {
	 * this.resultView = resultView;
	 * }
	 * // doInBackground完成后才会被调用
	 * @Override
	 * protected void onPostExecute(Bitmap bitmap) {
	 * //调用setTag保存图片以便于自动更新图片
	 * resultView.setTag(bitmap);
	 * }
	 * //从网上下载图片
	 * @Override
	 * protected Bitmap doInBackground(String... params) {
	 * Bitmap image=null;
	 * try {
	 * //new URL对象 把网址传入
	 * URL url = new URL(params[0]);
	 * //取得链接
	 * URLConnection conn = url.openConnection();
	 * conn.connect();
	 * //取得返回的InputStream
	 * InputStream is = conn.getInputStream();
	 * //将InputStream变为Bitmap
	 * image = BitmapFactory.decodeStream(is);
	 * is.close();
	 * ((ImageActivity)context).imagesCache.put(params[0],image); //把下载好的图片保存到缓存中
	 * Message m = new Message();
	 * m.what = 0;
	 * mHandler.sendMessage(m);
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * return image;
	 * }
	 * }
	 */
}
