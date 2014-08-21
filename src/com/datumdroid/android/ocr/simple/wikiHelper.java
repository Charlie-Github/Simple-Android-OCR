package com.datumdroid.android.ocr.simple;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

public class wikiHelper extends AsyncTask<String, Integer, String> {
	
	private EditText _field_wiki;	
	private Context context;
	private String wikiresult;
	private String title;
	
	public wikiHelper(String foodTitle,EditText _field7,Context ex_context){	
		
		_field_wiki = _field7;
		context = ex_context;
		title = foodTitle;
		wikiresult = "";
		
	}
	
	@Override
	 protected void onPreExecute(){
		 
	 }
	@Override
	protected String doInBackground(String... params) {
		// execution of result of Long time consuming operation
		// Wiki search begin
		final String searchKey = title;
		
		MyRunnable wikiRunnable = new MyRunnable(searchKey);
		Thread wikiThread = new Thread(wikiRunnable);
		wikiThread.start();
		
		publishProgress(1);
		
		try {
			wikiThread.join();
		} catch (InterruptedException e) {
			Log.v("SimpleOCR","wikiThread Join Fail: "+ e.getMessage());
		}				
							
		wikiresult = wikiRunnable.getResult();
		return wikiresult;
	}
	@Override
	protected void onPostExecute(String Text) {
		_field_wiki.setText(wikiresult);
		_field_wiki.setSelection(0);
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		_field_wiki.setText("Searching Wikipedia ...");
    }
}
