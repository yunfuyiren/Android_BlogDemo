package com.example.pulltorefreshlistviewdemo2;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class AsyncImageLoader {
	private HashMap<String, SoftReference<Drawable>> imageCache;
	Context curContext;
	public AsyncImageLoader(Context context) {
		curContext=context;
		imageCache=new HashMap<String, SoftReference<Drawable>>();
	}
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String tag);
	}
}
