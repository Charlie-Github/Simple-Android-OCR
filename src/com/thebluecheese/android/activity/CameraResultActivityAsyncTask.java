package com.thebluecheese.android.activity;

import java.util.Locale;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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


public class CameraResultActivityAsyncTask extends AsyncTask<String,Integer,String> {
	protected String DATA_PATH;	
	protected String lang;
	protected Bitmap bitmap;	
	protected Bitmap original_bitmap;
	protected ImageView _imageView;
	protected ImageView _backgroudimageView;
	protected LinearLayout _linearlayout;
	protected Context context;
	protected ProgressDialog _progressDialog;	
	protected String _path;
	protected String recognizedText;
	protected String[] keywords;
	protected String TAG = "BlueCheese";

	
	public CameraResultActivityAsyncTask(String path, String language, ImageView ex_imageView, LinearLayout ex_linearlayout,ProgressDialog progressDialog, Context ex_context){
		
		DATA_PATH = path;
		_path =  DATA_PATH + "/ocr.jpg";
		lang = language;		
		_imageView = ex_imageView;		
		_progressDialog = progressDialog;		
		_linearlayout = ex_linearlayout;
		context = ex_context;
		//bitmap = bit;		
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		bitmap = BitmapFactory.decodeFile(_path, options);
		//bitmap = ImageResizer.rotate(_path);
	}	
	
	@Override
	protected void onPreExecute(){		
		//set imageview		
		bitmap = rotateBitmap(bitmap,90);
		bitmap = drawRect(bitmap);
		_imageView.setImageBitmap(bitmap);
		
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		publishProgress(1);//call onProgressUpdate		
		// test start		
//		ImagePreProcessor ipp = new ImagePreProcessor();
//		bitmap = ipp.process(bitmap);		
		// test ends		
		
		//crop bitmap
		
		//_imageView.setImageBitmap(bitmap);
		bitmap = cropBitmap(bitmap);
		
		//Tesseract
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
		bitmap.recycle();
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
	
	public String recognize(){
		// image pre processor
		// ImagePreProcessor ipp = new ImagePreProcessor();
		// Bitmap tempBit = ipp.GrayscaleToBin(bitmap);		
		Log.v(TAG, "Tesseract API begin");
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
	
	private Bitmap rotateBitmap(Bitmap bitmap,int degree){
		Matrix matrix = new Matrix();
    	matrix.preRotate(degree);
    	bitmap = Bitmap.createBitmap(bitmap ,0,0, bitmap .getWidth(), bitmap .getHeight(),matrix,true);
    	return bitmap;
	}
	
	public Bitmap cropBitmap(Bitmap bitmap){
		Bitmap resizedbitmap;		
		int x = 0;
		int y = bitmap.getHeight()*1/5;
		int width = bitmap.getWidth();
		int height= bitmap.getHeight()*1/5;
		Log.d(TAG, "Croping image" + x+"|"+y+"|"+width+"|"+height);
		resizedbitmap=Bitmap.createBitmap(bitmap, x,y,width, height);		
		return resizedbitmap;
	}
	
	public Bitmap drawRect(Bitmap bitmap){
		// drwa rectangle on bitmap    	
    	Canvas tempCanvas = new Canvas(bitmap);
    	
    	Paint paint = new Paint();
    	//#33b5e5
    	paint.setColor(Color.rgb(0,0,0));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(20);        
        paint.setAlpha(100);
        
		int top = bitmap.getHeight()*1/5;
		int right = bitmap.getWidth();
		int bottom= bitmap.getHeight()*2/5;
		int height = bitmap.getHeight();        
    	tempCanvas.drawRect(0,0,right,top, paint);
    	tempCanvas.drawRect(0,bottom,right,height, paint);
    	
    	//#33b5e5
    	paint.setColor(Color.rgb(51,181,229));    	
    	paint.setStrokeWidth(20);
    	tempCanvas.drawLine(0, bottom, right, bottom, paint);
    	//tempCanvas.drawLine(right-100, top+20, right-100, bottom-20, paint);
    	
    	tempCanvas.drawBitmap(bitmap, 0, 0, null);
    	
    	return bitmap;
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
		
		// Go back button, take another picture
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