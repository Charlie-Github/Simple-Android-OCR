package com.thebluecheese.android.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.basic.Food;
import com.thebluecheese.android.basic.FoodPhoto;




import com.thebluecheese.android.basic.FoodReview;
import com.thebluecheese.android.network.DownloadImageTask;
import com.thebluecheese.android.network.GetRunner;
import com.thebluecheese.android.network.JsonParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class FoodDetailActivityAsyncTask extends AsyncTask<String, Integer, String> {
	
	private EditText _field_foodDetail;
	private ImageButton _shareButton;
	Button _moreinfoButton;
	LinearLayout _detail_layout;
	LinearLayout _root_view;
	private LinearLayout _linearlayout;
	LinearLayout _reviews_view;
	private Context _context;
	private String foodDetailResult;
	private String foodReviewResult;
	private String title;
	private String foodServerAddress;
	private String reviewServerAddress;
	private String s3Address;
	private String foodName;
	private String foodTitle;
	private int foodId;
	private String foodDesc;
	private String foodImageAddress;
	private String TAG = "BlueCheese";
	private ArrayList<FoodReview> reviews = new ArrayList<FoodReview>();
	private Food food;
	String first_half;
	String second_half;
	Button moreInfo;
	
	public FoodDetailActivityAsyncTask(String foodTitle,EditText _field7,ImageButton shareButton,Button moreinfoButton,LinearLayout root_view,LinearLayout detail_layout,LinearLayout linearlayout,LinearLayout reviews_view,Context context){
		
		_field_foodDetail = _field7;
		_shareButton = shareButton;
		title = foodTitle.replace(" ", "%20");
		foodDetailResult = "";
		first_half = "";
		second_half = "";
		_context = context;
		_root_view = root_view;
		_linearlayout = linearlayout;
		_detail_layout = detail_layout;
		_reviews_view = reviews_view;
		moreInfo = new Button(_context);
		_moreinfoButton = moreinfoButton;
		foodServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/food";		
		reviewServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/review";
		s3Address = "https://s3-us-west-2.amazonaws.com/blue-cheese-deployment/";
	}
	
	@Override
	protected void onPreExecute(){
		 
	 }
	@Override
	
	protected String doInBackground(String... params) {
		// execution of result of Long time consuming operation
		// search begin
		
		publishProgress(1);
		getFoodDetail();
		getFoodReview();
		
		return foodDetailResult;
	}
	
	@Override
	protected void onPostExecute(String Text) {
		
		//trunk desc
		if(foodDesc.length() > 75){
			first_half = foodDesc.substring(0,75);
			second_half = foodDesc.substring(75);
		}else{
			first_half = foodDesc;
		}
		
		if(second_half.length() > 1){
			//add a button			
			_moreinfoButton.setBackgroundColor(Color.TRANSPARENT);
			_moreinfoButton.setGravity(Gravity.CENTER_VERTICAL);
			_moreinfoButton.setText("更多...");
			_moreinfoButton.setOnClickListener(new moreClickHandler());
			//_detail_layout.addView(moreInfo);
			_field_foodDetail.setText(first_half+"...");
		}else{
			_field_foodDetail.setText(first_half);
		}
		
		
		_field_foodDetail.setSelection(0);
		setImageViews(food._photos);
		setReviewViews(reviews);
		
		_shareButton.setOnClickListener(new ButtonClickHandler());
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		// loading
		String loading = _context.getResources().getString(R.string.loading);
		_field_foodDetail.setText(loading);
   }
	
	protected void setImageViews(ArrayList<FoodPhoto> photos){
		// set food images
		for(int i = 0; i<photos.size(); i++){
			String photoKey = photos.get(i)._url;
			ImageView imageView = new ImageView(_context);
			foodImageAddress = s3Address + photoKey;
			new DownloadImageTask(imageView).execute(foodImageAddress);			
			_linearlayout.addView(imageView);
		}
		
	}
	
	protected void setReviewViews(ArrayList<FoodReview> foodReviews){
		// populate food reviews
		for(int i = 0; i<foodReviews.size(); i++){
			
			String creater = foodReviews.get(i)._review_creater;
			String comment = foodReviews.get(i)._comments;
			
			SharedPreferences sharedPre = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
			String selfieURL = sharedPre.getString("selfie","");
			
			
			LinearLayout reviewView = new LinearLayout(_context); 
			reviewView.setOrientation(LinearLayout.HORIZONTAL);
			
			ImageView imageView = new ImageView(_context);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
				
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			//new DownloadImageTask(imageView).execute(selfieURL);
			imageView.setImageResource(R.drawable.user);
			reviewView.addView(imageView);
			
			
			EditText text = new EditText(_context);
			text.setGravity(Gravity.CENTER_VERTICAL);
			text.setKeyListener(null);
			text.setText(creater+": \n"+comment);
			text.setBackgroundColor(Color.TRANSPARENT);
			reviewView.addView(text);			
					
			_reviews_view.addView(reviewView);					
			
		}
		
	}	
	
	protected void getFoodDetail(){
		
		final String searchKey = title;		
		GetRunner getR = new GetRunner(foodServerAddress , "lang=CN&title="+searchKey);
		Thread getThread = new Thread(getR);
		getThread.start();	
		
		try {
			//waiting for get response
			getThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Exception on FoodDetailActivityAsyncTask GetRunner thread: " + e.getMessage());			
		}
		
		foodDetailResult = getR.getResult();
		
		JsonParser jp = new JsonParser();
		food = jp.parseFood(foodDetailResult);
		foodDesc = food._description;
		foodName =food._name;
		foodId = food._fid;
		foodTitle = food._title;
	}
	
	protected void getFoodReview(){
		// review?fid=455&action=get_review
		GetRunner getR = new GetRunner(reviewServerAddress , "action=get_review&fid="+foodId);
		Thread getThread = new Thread(getR);
		getThread.start();
				
		try {
			//waiting for get response
			getThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Exception on FoodDetailActivityAsyncTask GetRunner thread: " + e.getMessage());			
		}
		
		foodReviewResult = getR.getResult();
		
		//test
		JsonParser jp = new JsonParser();
		reviews = jp.parseReview(foodReviewResult);
	}

	public void takeScreenShot(View view ){
		view = view.getRootView();
	    view.setDrawingCacheEnabled( true); 
	    view.buildDrawingCache(); 
	    Bitmap bitmap = view.getDrawingCache(); 
	    if (bitmap != null) { 
	    	try { 
	    		FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory()+File.separator+"BlueCheese"+File.separator+"Charlie-screenshot.png" ); 
	    		bitmap.compress(Bitmap.CompressFormat. PNG, 100, out);
	    		out.flush();
	    		out.close();
	    	} catch (Exception e) { 
	    		e.printStackTrace(); 
	    	} 
	    } 
	} 
	
	
	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {			
			//Weibo share
			ShareParams sp = new ShareParams();
			sp.setText("[蓝芝士]食物小百科：\n"+foodName+"("+foodTitle+")\n"+"下载蓝芝士客户端，实时菜单翻译就在\n"+"https://appsto.re/us/SQgV1.i");
			//sp.setImageUrl(foodImageAddress);
			sp.setImagePath(Environment.getExternalStorageDirectory()+File.separator+"BlueCheese"+File.separator+"Charlie-screenshot.png");
			//Platform pf = ShareSDK.getPlatform(SinaWeibo.NAME);
			Platform pf = ShareSDK.getPlatform(WechatMoments.NAME);
			
			// 执行图文分享
			pf.share(sp);
			
			//test below
			takeScreenShot(_root_view);
			
		}
	}
	public class moreClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			_field_foodDetail.setText(first_half+second_half);
			_moreinfoButton.setText("精简");
			_moreinfoButton.setOnClickListener(new lessClickHandler());
		}
	}
	public class lessClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			_field_foodDetail.setText(first_half+"...");
			_moreinfoButton.setText("更多信息...");
			_moreinfoButton.setOnClickListener(new moreClickHandler());
		}
	}

}
