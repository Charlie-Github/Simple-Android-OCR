package com.thebluecheese.android.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;


public class wikiHelper extends AsyncTask<String, Integer, String> {
	
	private EditText _field_wiki;	
	private String wikiresult;
	private String title;
	private String TAG = "BlueCheese";
	
	public wikiHelper(String foodTitle,EditText _field7){	
		
		_field_wiki = _field7;		
		title = foodTitle.replace(" ", "%20");
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
		
		WikiRunner wikiRunnable = new WikiRunner(searchKey);
		Thread wikiThread = new Thread(wikiRunnable);
		wikiThread.start();
		
		publishProgress(1);
		
		try {
			wikiThread.join();
		} catch (InterruptedException e) {
			Log.v(TAG,"wikiThread Join Fail: "+ e.getMessage());
		}				
							
		wikiresult = wikiRunnable.getResult();
		return wikiresult;
	}
	@Override
	protected void onPostExecute(String Text) {
		_field_wiki.setText("维基百科： \n" + wikiresult);
		_field_wiki.setSelection(0);
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		_field_wiki.setText("Searching Wikipedia ...");
    }
}
