package com.thebluecheese.android.ocr;

import java.io.File;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.thebluecheese.android.activity.CameraResultActivity;
import com.thebluecheese.android.activity.FoodDetailActivity;
import com.thebluecheese.android.localdb.LocalDbOperator;

public class TessHelper extends AsyncTask<String,Integer,String> {
	private String DATA_PATH;
	private String lang;
	private Bitmap bitmap;	
	
	protected ImageView _imageView;
	protected ImageView _backgroudimageView;
	protected String image_path;
	protected Uri imageUri;

	private LinearLayout _linearlayout;
	private Context context;
	
	protected ProgressDialog _progressDialog;
	
	private String recognizedText;
	private String[] keywords;
	
	public TessHelper(String path, String language, Bitmap bit, ImageView ex_imageView,ImageView ex_backgroudimageView, LinearLayout ex_linearlayout,ProgressDialog progressDialog, Context ex_context){
		DATA_PATH = path;
		image_path = path + "/ocr.jpg";
		lang = language;
		bitmap = bit;
		
		_imageView = ex_imageView;
		
		_backgroudimageView = ex_backgroudimageView;
		_progressDialog = progressDialog;
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
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9&]", " ");
			recognizedText = recognizedText.replaceAll("   ", " ");
			recognizedText = recognizedText.replaceAll("  ", " ");
		}
		
		recognizedText = recognizedText.trim();
		Log.v("SimpleOCR", "Tesseract output: " + recognizedText);
		

		
		return recognizedText;
		
	}

	@Override
	protected String doInBackground(String... params) {
		
		publishProgress(1);//call onProgressUpdate
		recognizedText = recognize();
		
		//local search begin
		LocalDbOperator ldboperator = new LocalDbOperator(context);
		keywords = ldboperator.search(recognizedText);
		
		//local search ends		
		
		return recognizedText;
	}
	
	protected void setfields(){
		
		for(int i = 0; i<keywords.length; i++){
			String[] key_pair = keywords[i].split("\\|");
			final String title = key_pair[0];
			final String name = key_pair[1];
			
			
			String ui_title = title.toLowerCase(Locale.ENGLISH);
			ui_title =  Character.toString(ui_title.charAt(0)).toUpperCase(Locale.ENGLISH)+ui_title.substring(1);
			
			
			Button button = new Button(context);
			button.setText(ui_title+" \n"+name);
			
			//set button height
			int dps = 100;
			float scale = context.getResources().getDisplayMetrics().density;			
			int pixels = (int) (dps * scale + 0.5f);
			button.setHeight(pixels);
			button.setBackgroundColor(Color.WHITE);
			button.setGravity(Gravity.CENTER_VERTICAL);
			
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
		
		// Go back button
		Button button = new Button(context);
		button.setText("Take Pictur \nAgain");
		
		//set button height
		int dps = 100;
		float scale = context.getResources().getDisplayMetrics().density;			
		int pixels = (int) (dps * scale + 0.5f);
		button.setHeight(pixels);
		button.setBackgroundColor(Color.LTGRAY);
		button.setGravity(Gravity.CENTER_VERTICAL);
		
		_linearlayout.addView(button);
		button.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {
				//set button's intent, opening detail activity
				Intent intent = new Intent(context,CameraResultActivity.class);				
				context.startActivity(intent);
			}
		});
		
	}
	
	@Override
	protected void onPostExecute(String Text) {
	   // execution of result of Long time consuming operation
		setfields();
		_imageView.setImageURI(imageUri);
		_progressDialog.dismiss();
		_backgroudimageView.setClickable(false);
	  }
	
	protected void onProgressUpdate(Integer... progress) {
		_imageView.setImageURI(imageUri);
		_progressDialog = new ProgressDialog(context);
		_progressDialog.setMessage("Scanning Image...");
		_progressDialog.show();
		_progressDialog.setCanceledOnTouchOutside(false);
		_progressDialog.setCancelable(false);
		_backgroudimageView.setClickable(false);
    }
    
}
