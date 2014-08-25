package com.thebluecheese.android.network;

import com.thebluecheese.android.basic.Food;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

public class FoodDetailHelper extends AsyncTask<String, Integer, String> {
	
	private EditText _field_foodDetail;	

	private String foodDetailResult;
	private String title;
	
	public FoodDetailHelper(String foodTitle,EditText _field7){
		
		_field_foodDetail = _field7;
		title = foodTitle.replace(" ", "%20");
		foodDetailResult = "";
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
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		_field_foodDetail.setText("搜索中 ...");
   }

}
