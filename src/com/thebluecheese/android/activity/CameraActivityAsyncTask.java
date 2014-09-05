package com.thebluecheese.android.activity;

import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.localdb.LocalDbOperator;

public class CameraActivityAsyncTask extends AsyncTask<String,Integer,String> {
	protected String DATA_PATH;
	protected String uncropPath;
	protected String lang;
	protected Bitmap bitmap;	
	protected Bitmap original_bitmap;
	protected ImageView _imageView;
	protected ImageView _backgroudimageView;
	protected LinearLayout _linearlayout;
	protected Context context;
	protected ProgressDialog _progressDialog;	
	
	protected String recognizedText;
	protected String[] keywords;
	protected String TAG = "BlueCheese";

	
	public CameraActivityAsyncTask(String path, String language, Bitmap bit, ImageView ex_imageView, LinearLayout ex_linearlayout,ProgressDialog progressDialog, Context ex_context){
		
		DATA_PATH = path;
		uncropPath = path+"ocr.jpg";	
		lang = language;		
		_imageView = ex_imageView;		
		_progressDialog = progressDialog;		
		_linearlayout = ex_linearlayout;
		context = ex_context;
		bitmap = bit;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		original_bitmap = BitmapFactory.decodeFile(uncropPath, options);

	}	
	
	 @Override
	 protected void onPreExecute(){		
	 }
	
	public String recognize(){
		// image pre processor
		// ImagePreProcessor ipp = new ImagePreProcessor();
		// Bitmap tempBit = ipp.GrayscaleToBin(bitmap);
		
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
		Log.v(TAG, "Tesseract output: " + recognizedText);
		
		return recognizedText;
		
	}

	@Override
	protected String doInBackground(String... params) {
		
		publishProgress(1);//call onProgressUpdate

		_imageView.setImageBitmap(original_bitmap);
		
		// test start		
//		ImagePreProcessor ipp = new ImagePreProcessor();
//		bitmap = ipp.process(bitmap);
		// test ends		
		
		
		recognizedText = recognize();
		
		//local search begin
		LocalDbOperator ldboperator = new LocalDbOperator(context);
		keywords = ldboperator.search(recognizedText);
		
		//local search ends		
		return recognizedText;
	}
	
	@Override
	protected void onPostExecute(String Text) {
	   // execution of result of Long time consuming operation
		setfields();		
		_progressDialog.dismiss();
		//_imageView.setImageURI(imageUri);
		//_backgroudimageView.setClickable(false);
		
	  }
	
	protected void onProgressUpdate(Integer... progress) {		
		
		_progressDialog = new ProgressDialog(context);
		String loadingmessage = context.getResources().getString(R.string.scanning);
		_progressDialog.setMessage(loadingmessage);		
		_progressDialog.setCancelable(false);
		_progressDialog.setCanceledOnTouchOutside(false);
		_progressDialog.show();
		
		//_imageView.setImageURI(imageUri);
		//_backgroudimageView.setClickable(false);
    }
	
	
	protected void setfields(){
		
		for(int i = 0; i<keywords.length; i++){
			String[] key_pair = keywords[i].split("\\|");
			final String title = key_pair[0];
			final String name = key_pair[1];
			
			// set title format
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
		button.setText(R.string.takePicAgain);
		
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
}