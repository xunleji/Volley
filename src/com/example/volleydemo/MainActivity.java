package com.example.volleydemo;

import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

public class MainActivity extends Activity {

	private ImageView mImageView;
	private NetworkImageView mNetworkImageView;
	private String url = "http://t.juzi.cn/weather/getweather";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		mImageView = (ImageView) findViewById(R.id.imageView);
		mNetworkImageView = (NetworkImageView) findViewById(R.id.networkImageView);
		Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showImageByNetworkImageView();
			}
		});
		getJSONByVolley();
		getStringByVolley();
		loadImageByVolley();
		showImageByNetworkImageView();
	}

	// volley以get方式获取json数据
	private void getJSONByVolley() {
		RequestQueue requestQueue = Volley.newRequestQueue(this);
//		String JSONDataUrl = "http://pipes.yahooapis.com/pipes/pipe.run?_id=giWz8Vc33BG6rQEQo_NLYQ&_render=json";
		String JSONDataUrl = "http://t.juzi.cn/weather/getweather";
		final ProgressDialog progressDialog = ProgressDialog.show(this,
				"This is title", "...Loading...");
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, JSONDataUrl, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e("JsonObjectRequest", "response=" + response);
						if (progressDialog.isShowing()
								&& progressDialog != null) {
							progressDialog.dismiss();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (progressDialog.isShowing()
								&& progressDialog != null) {
							progressDialog.dismiss();
						}
						System.out.println("sorry,Error");
					}
				});
		requestQueue.add(jsonObjectRequest);
	}
	
	// volley以get方式获取json数据
	private void getStringByVolley() {
		RequestQueue requestQueue = Volley.newRequestQueue(this);
//		String JSONDataUrl = "http://pipes.yahooapis.com/pipes/pipe.run?_id=giWz8Vc33BG6rQEQo_NLYQ&_render=json";
		String JSONDataUrl = "http://t.juzi.cn/weather/getweather";
		final ProgressDialog progressDialog = ProgressDialog.show(this,
				"This is title", "...Loading...");
		StringRequest stringRequest = new StringRequest(Request.Method.GET, JSONDataUrl, 
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("StringRequest", "response=" + response);
				if (progressDialog.isShowing()
						&& progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				if (progressDialog.isShowing()
						&& progressDialog != null) {
					progressDialog.dismiss();
				}
				System.out.println("sorry,Error");
			}
		});
		requestQueue.add(stringRequest);
	}

	/**
	 * 利用Volley异步加载图片
	 * 
	 * 注意方法参数: getImageListener(ImageView view, int defaultImageResId, int
	 * errorImageResId) 第一个参数:显示图片的ImageView 第二个参数:默认显示的图片资源 第三个参数:加载错误时显示的图片资源
	 */
	private void loadImageByVolley() {
		String imageUrl = "http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg";
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(
				20);
		ImageCache imageCache = new ImageCache() {
			@Override
			public void putBitmap(String key, Bitmap value) {
				if(getBitmap(key)==null){
					lruCache.put(key, value);
				}
			}

			@Override
			public Bitmap getBitmap(String key) {
				return lruCache.get(key);
			}
		};
		ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
		ImageListener listener = ImageLoader.getImageListener(mImageView,
				R.drawable.ic_launcher, R.drawable.ic_launcher);
		imageLoader.get(imageUrl, listener);
	}

	/**
	 * 利用NetworkImageView显示网络图片
	 */
	private void showImageByNetworkImageView() {
		Log.e("showImageByNetworkImageView", "showImageByNetworkImageView");
		String imageUrl = "http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg";
		RequestQueue requestQueue = Volley.newRequestQueueInDisk(this,
				"/sdcard/", null);
		final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(
				20);
		ImageCache imageCache = new ImageCache() {
			@Override
			public void putBitmap(String key, Bitmap value) {
				if(lruCache.get(key)==null){
					lruCache.put(key, value);
				}
			}

			@Override
			public Bitmap getBitmap(String key) {
				return lruCache.get(key);
			}
		};
		ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
		mNetworkImageView.setTag("url");
		mNetworkImageView.setImageUrl(imageUrl, imageLoader);
	}

}
