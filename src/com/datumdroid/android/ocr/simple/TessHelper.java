package com.datumdroid.android.ocr.simple;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessHelper extends AsyncTask<String,String,String> {
	private String DATA_PATH;
	private String lang;
	private Bitmap bitmap;	
	private EditText _field_tess;
	private EditText _field_wiki;
	private LinearLayout _linearlayout;
	private Context context;
	private String wikiresult;
	private String recognizedText;
	private String[] keywords;
	
	public TessHelper(String path, String language, Bitmap bit,EditText _field3,EditText _field4,LinearLayout ex_linearlayout,Context ex_context){
		DATA_PATH = path;
		lang = language;
		bitmap = bit;
		_field_tess = _field3;
		_field_wiki = _field4;
		_linearlayout = ex_linearlayout;
		context = ex_context;
		wikiresult = "";
	}	
	
	 @Override
	 protected void onPreExecute(){
		 
	 }
	
	public String recognize(){
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();
		
		if ( lang.equalsIgnoreCase("eng") ) {
			//remove dump marks
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9,.&-?!@%$*+=/]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		Log.v("SimpleOCR", "Tesseract output: " + recognizedText);
		

		
		return recognizedText;
		
	}

	@Override
	protected String doInBackground(String... params) {
		recognizedText = recognize();
		
		//local search begin
		LocalDbOperator ldboperator = new LocalDbOperator(context);
		keywords = ldboperator.search(recognizedText);
		
		//local search ends
		
		// Wiki search begin
		final String searchKey = recognizedText;
		
		MyRunnable wikiRunnable = new MyRunnable(searchKey);
		Thread wikiThread = new Thread(wikiRunnable);
		wikiThread.start();		
		
		try {
			wikiThread.join();
		} catch (InterruptedException e) {
			Log.v("SimpleOCR","wikiThread Join Fail: "+ e.getMessage());
		}				
							
		wikiresult = wikiRunnable.getResult();
		return recognizedText;
	}
	
	protected void setfields(){
		_field_tess.setText(recognizedText);
		_field_tess.setSelection(0);
		_field_wiki.setText(wikiresult);
		_field_wiki.setSelection(0);		
		
		for(int i = 0; i<keywords.length; i++){
			String[] key_pair = keywords[i].split("\\|");
			final String title = key_pair[0];
			final String name = key_pair[1];
			Button button = new Button(context);
			button.setText(title+" \n"+name);
			_linearlayout.addView(button);
			button.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					//set button's intent, opening detail activity
					Intent intent = new Intent(context,FoodDetailActivity.class);
					intent.putExtra("FOOD_TITLE", title);
					intent.putExtra("FOOD_NAME", name);
					context.startActivity(intent);
				}
			});
			
			
		}
		
	}
	
	@Override
	protected void onPostExecute(String Text) {
	   // execution of result of Long time consuming operation
		setfields();
		
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		
    }
    
}
