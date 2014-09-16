package com.thebluecheese.android.activity;

import java.util.ArrayList;

import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.basic.Food;
import com.thebluecheese.android.basic.FoodPhoto;




import com.thebluecheese.android.network.DownloadImageTask;
import com.thebluecheese.android.network.GetRunner;
import com.thebluecheese.android.network.JsonParser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FoodDetailActivityAsyncTask extends AsyncTask<String, Integer, String> {
	
	private EditText _field_foodDetail;
	private LinearLayout _linearlayout;
	private Context _context;
	private String foodDetailResult;
	private String title;
	private String serverAddress;
	private String s3Address;
	private String TAG = "BlueCheese";
	
	public FoodDetailActivityAsyncTask(String foodTitle,EditText _field7,LinearLayout linearlayout,Context context){
		
		_field_foodDetail = _field7;
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
		String loading = _context.getResources().getString(R.string.loading);
		_field_foodDetail.setText(loading);
   }
	
	protected void setImageViews(ArrayList<FoodPhoto> photos){		
		for(int i = 0; i<photos.size(); i++){
			String photoKey = photos.get(i)._url;
			ImageView imageView = new ImageView(_context);
			new DownloadImageTask(imageView).execute(s3Address + photoKey);
			_linearlayout.addView(imageView);
		}
		
	}

}
