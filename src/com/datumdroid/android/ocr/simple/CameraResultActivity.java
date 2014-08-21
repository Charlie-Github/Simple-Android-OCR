package com.datumdroid.android.ocr.simple;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraResultActivity extends Activity {
	public static final String PACKAGE_NAME = "com.datumdroid.android.ocr.simple";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";	

	public static final String lang = "eng";

	private static final String TAG = "SimpleOCR";

	protected EditText _field3;
	protected ImageView _imageView;
	protected LinearLayout scroll_layout;
	
	protected String _path;
	protected boolean _taken;
	protected static final String PHOTO_TAKEN = "photo_taken";
	
	private ProgressDialog progressDialog;
	
	private SQLiteDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_result);
		_field3 = (EditText) findViewById(R.id.field3);
		_imageView = (ImageView)findViewById(R.id.imagview);		
		scroll_layout = (LinearLayout) findViewById(R.id.camera_result_scroll_linear);
		
		_path = DATA_PATH + "/ocr.jpg";
		
		Log.i(TAG,"Path: "+DATA_PATH);
		startCameraActivity();
		
	}

	protected void startCameraActivity() {
		// Simple android photo capture:
		// http://labs.makemachine.net/2010/03/simple-android-photo-capture/
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 0);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);

		if (resultCode == -1) {			
			//onPhotoTaken();
			_taken = true;
			
			readImage();
						
		} else {
			Log.v(TAG, "User cancelled");
		}
	}
	
	protected void readImage() {
	
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
	
			Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
	
			try {
				ExifInterface exif = new ExifInterface(_path);
				int exifOrientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);
	
				Log.v(TAG, "Orient: " + exifOrientation);
	
				int rotate = 0;
	
				switch (exifOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
			}
	
				Log.v(TAG, "Rotation: " + rotate);
	
				if (rotate != 0) {
	
					// Getting width & height of the given image.
					int w = bitmap.getWidth();
					int h = bitmap.getHeight();
	
					// Setting pre rotate
					Matrix mtx = new Matrix();
					mtx.preRotate(rotate);
	
					// Rotating Bitmap
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
				}
	
				// Convert to ARGB_8888, required by tess
				bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
	
			} catch (IOException e) {
				Log.e(TAG, "Correct orientation failed: " + e.toString());
			}
	
			Log.v(TAG, "Tesseract API begin");	
			String recognizedText = "";
			
			/*
			// Start Tess			
			TessHelper tesshelper = new TessHelper(DATA_PATH,lang,bitmap);			
			String recognizedText = tesshelper.recognize();			
			//Ends Tess
			*/
			
			 TessHelper tesshp = new TessHelper(DATA_PATH,lang,bitmap,_field3,_imageView,scroll_layout,this);//"this"is context
			 tesshp.execute();
			
			
			//_field3.setText(recognizedText);
			//_field3.setSelection(_field3.getText().toString().length());
			
			// Local search begin				
			//LocalDbOperator ldboperator = new LocalDbOperator(this);
			//ldboperator.search(recognizedText);
			//int fid = ldboperator.searchByName(recognizedText);
			//String chinese_name = ldboperator.searchById(fid);
			//Log.i(TAG,"Translate: "+chinese_name);
			// Local search Ends
			
			 /*
			// Wiki search begin
			final String searchKey = recognizedText;
			
			MyRunnable wikiRunnable = new MyRunnable(searchKey);
			Thread wikiThread = new Thread(wikiRunnable);
			wikiThread.start();		
			
			try {
				wikiThread.join();
			} catch (InterruptedException e) {
				Log.v(TAG,"wikiThread Join Fail: "+ e.getMessage());
			}			
			
			String wikiresult = "";					
			wikiresult = wikiRunnable.getResult();
			// Wiki search ends
			
			_field4.setText(wikiresult);
			_field4.setSelection(0);
			*/
	
		}// readImage Ends
}
