package com.thebluecheese.android.network;

import java.util.ArrayList;
import java.util.Locale;

import com.thebluecheese.android.activity.FoodDetailActivity;
import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.basic.Food;
import com.thebluecheese.android.basic.FoodPhoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FoodDetailHelper extends AsyncTask<String, Integer, String> {
	
	private EditText _field_foodDetail;
	private LinearLayout _linearlayout;
	private Context _context;
	private String foodDetailResult;
	private String title;
	
	
	
	public FoodDetailHelper(String foodTitle,EditText _field7,LinearLayout linearlayout,Context context){
		
		_field_foodDetail = _field7;
		title = foodTitle.replace(" ", "%20");
		foodDetailResult = "";
		_context = context;
		_linearlayout = linearlayout;
		
	}
	
	@Override
	 protected void onPreExecute(){
		 
	 }
	@Override
	protected String doInBackground(String... params) {
		// execution of result of Long time consuming operation
		// search begin
		final String searchKey = title;
		
		GetRunner getR = new GetRunner("http://default-environment-9hfbefpjmu.elasticbeanstalk.com/food", "lang=CN&title="+searchKey);
		Thread getThread = new Thread(getR);
		getThread.start();
		
		publishProgress(1);
		
		try {
			getThread.join();
		} catch (InterruptedException e) {
			Log.e("SimpleOCR", "FoodDetailHelper Exception: "+e.getMessage());			
		}
		
		foodDetailResult = getR.getResult();
		return foodDetailResult;
	}
	@Override
	protected void onPostExecute(String Text) {
		
		JsonParser jp = new JsonParser();
		Food f = jp.parseFood(foodDetailResult);
			
		_field_foodDetail.setText(f._description);
		_field_foodDetail.setSelection(0);
		setImageViews(f._photos);
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		_field_foodDetail.setText("搜索中 ...");
   }
	
	protected void setImageViews(ArrayList<FoodPhoto> photos){		
		for(int i = 0; i<photos.size(); i++){
			String photoKey = photos.get(i)._url;
			ImageView imageView = new ImageView(_context);					
			new DownloadImageTask(imageView).execute("https://s3-us-west-2.amazonaws.com/blue-cheese-deployment/"+photoKey);
			_linearlayout.addView(imageView);
		}
		
	}

}
