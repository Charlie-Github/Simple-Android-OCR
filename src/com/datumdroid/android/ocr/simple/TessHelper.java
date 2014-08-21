package com.datumdroid.android.ocr.simple;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessHelper extends AsyncTask<String,Integer,String> {
	private String DATA_PATH;
	private String lang;
	private Bitmap bitmap;	
	private EditText _field_tess;
	protected ImageView _imageView;
	protected String image_path;
	protected Uri imageUri;

	private LinearLayout _linearlayout;
	private Context context;
	
	private String recognizedText;
	private String[] keywords;
	
	public TessHelper(String path, String language, Bitmap bit, EditText _field3, ImageView ex_imageView, LinearLayout ex_linearlayout, Context ex_context){
		DATA_PATH = path;
		image_path = path + "/ocr.jpg";
		lang = language;
		bitmap = bit;
		_field_tess = _field3;
		_imageView = ex_imageView;
		
		_linearlayout = ex_linearlayout;
		context = ex_context;
		
		
		File file = new File(image_path);
		imageUri = Uri.fromFile(file);

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
		
		publishProgress(1);
		recognizedText = recognize();
		
		//local search begin
		LocalDbOperator ldboperator = new LocalDbOperator(context);
		keywords = ldboperator.search(recognizedText);
		
		//local search ends		
		
		return recognizedText;
	}
	
	protected void setfields(){
		_field_tess.setText("");
		//_field_tess.setSelection(0);		
		
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
		_imageView.setImageURI(imageUri);
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		_field_tess.setText("Analysying Image ...");
		
    }
    
}
