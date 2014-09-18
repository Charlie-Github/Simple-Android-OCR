package com.thebluecheese.android.activity;

import java.util.ArrayList;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.activity.AboutusActivity.ButtonClickHandler;
import com.thebluecheese.android.basic.Food;
import com.thebluecheese.android.basic.FoodPhoto;




import com.thebluecheese.android.network.DownloadImageTask;
import com.thebluecheese.android.network.GetRunner;
import com.thebluecheese.android.network.JsonParser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FoodDetailActivityAsyncTask extends AsyncTask<String, Integer, String> {
	
	private EditText _field_foodDetail;
	private ImageButton _shareButton;
	private LinearLayout _linearlayout;
	private Context _context;
	private String foodDetailResult;
	private String title;
	private String serverAddress;
	private String s3Address;
	private String foodName;
	private String foodDesc;
	private String foodImageAddress;
	private String TAG = "BlueCheese";
	
	public FoodDetailActivityAsyncTask(String foodTitle,EditText _field7,ImageButton shareButton,LinearLayout linearlayout,Context context){
		
		_field_foodDetail = _field7;
		_shareButton = shareButton;
		title = foodTitle.replace(" ", "%20");
		foodDetailResult = "";
		_context = context;
		_linearlayout = linearlayout;
		serverAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/food";
		s3Address = "https://s3-us-west-2.amazonaws.com/blue-cheese-deployment/";
	}
	
	@Override
	 protected void onPreExecute(){
		 
	 }
	@Override
	protected String doInBackground(String... params) {
		// execution of result of Long time consuming operation
		// search begin
		final String searchKey = title;
		
		GetRunner getR = new GetRunner(serverAddress , "lang=CN&title="+searchKey);
		Thread getThread = new Thread(getR);
		getThread.start();
		
		publishProgress(1);
		
		try {
			//waiting for get response
			getThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Exception on FoodDetailActivityAsyncTask GetRunner thread: " + e.getMessage());			
		}
		
		foodDetailResult = getR.getResult();
		_shareButton.setOnClickListener(new ButtonClickHandler());		
		
		return foodDetailResult;
	}
	@Override
	protected void onPostExecute(String Text) {
		
		JsonParser jp = new JsonParser();
		Food f = jp.parseFood(foodDetailResult);
		foodDesc = f._description;
		foodName =f._name;
		_field_foodDetail.setText(foodDesc);
		_field_foodDetail.setSelection(0);
		setImageViews(f._photos);
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		String loading = _context.getResources().getString(R.string.loading);
		_field_foodDetail.setText(loading);
   }
	
	protected void setImageViews(ArrayList<FoodPhoto> photos){		
		for(int i = 0; i<photos.size(); i++){
			String photoKey = photos.get(i)._url;
			ImageView imageView = new ImageView(_context);
			foodImageAddress = s3Address + photoKey;
			new DownloadImageTask(imageView).execute(foodImageAddress);			
			_linearlayout.addView(imageView);
		}
		
	}
	
	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {			
			//Weibo share
			ShareParams sp = new ShareParams();
			sp.setText("转自：蓝芝士\n "+""+foodName+"\n"+foodDesc);
			sp.setImageUrl(foodImageAddress);
			
			//Platform pf = ShareSDK.getPlatform(SinaWeibo.NAME);
			Platform pf = ShareSDK.getPlatform(WechatMoments.NAME);
			
			// 执行图文分享
			pf.share(sp);
					
			
		}
	}

}
