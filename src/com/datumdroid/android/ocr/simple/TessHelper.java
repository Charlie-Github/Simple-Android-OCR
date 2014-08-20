package com.datumdroid.android.ocr.simple;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessHelper extends AsyncTask<String,String,String> {
	private String DATA_PATH;
	private String lang;
	private Bitmap bitmap;	
	private EditText _field_tess;
	protected EditText _field_wiki;
	private Context context;
	private String wikiresult;
	private String recognizedText;
	
	public TessHelper(String path, String language, Bitmap bit,EditText _field3,EditText _field4,Context ex_context){
		DATA_PATH = path;
		lang = language;
		bitmap = bit;
		_field_tess = _field3;
		_field_wiki = _field4;
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
		
		LocalDbOperator ldboperator = new LocalDbOperator(context);
		ldboperator.search(recognizedText);
		
		return recognizedText;
		
	}

	@Override
	protected String doInBackground(String... params) {
		recognizedText = recognize();		
		
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
	
	protected void setfield(String recognizedText, String wikiText){
		_field_tess.setText(recognizedText);
		_field_tess.setSelection(0);
		_field_wiki.setText(wikiText);
		_field_wiki.setSelection(0);
		
	}
	
	@Override
	protected void onPostExecute(String Text) {
	   // execution of result of Long time consuming operation
		setfield(recognizedText,wikiresult);
		
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		
    }
    
}
