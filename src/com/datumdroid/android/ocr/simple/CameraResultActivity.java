package com.datumdroid.android.ocr.simple;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
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

public class CameraResultActivity extends Activity {
	public static final String PACKAGE_NAME = "com.datumdroid.android.ocr.simple";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";	

	public static final String lang = "eng";

	private static final String TAG = "SimpleOCR";

	protected EditText _field3;
	protected EditText _field4;
	
	protected String _path;
	protected boolean _taken;
	protected static final String PHOTO_TAKEN = "photo_taken";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_result);
		_field3 = (EditText) findViewById(R.id.field3);
		_field4 = (EditText) findViewById(R.id.filed4);
		_path = DATA_PATH + "/ocr.jpg";
		startCameraActivity();
	}

	protected void startCameraActivity() {
		// Simple android photo capture:
		// http://labs.makemachine.net/2010/03/simple-android-photo-capture/
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);		
		startActivityForResult(intent, 0);//tells the system that when the user is done with the camera app to return to this activity 
		readImage();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);

		if (resultCode == -1) {
			
			//onPhotoTaken();
						
		} else {
			Log.v(TAG, "User cancelled");
		}
	}
	
	protected void readImage() {	
			
			_taken = true;		
	
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
			
			// Start Tess
			
			TessRunnable tessRunnable = new TessRunnable(DATA_PATH,lang,bitmap);
			Thread tessThread = new Thread(tessRunnable);
			tessThread.start();
			
			
			try {
				tessThread.join();
			} catch (InterruptedException e) {
				Log.v(TAG,"tessThread Join Fail: "+ e.getMessage());
			}
			
			
			
			String	recognizedText = tessRunnable.getResult();
		
			
			//Ends Tess		
			
			if ( lang.equalsIgnoreCase("eng") ) {
				//remove dump marks
				recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9,.&-?!@%$*+=/]+", " ");
			}
			
			recognizedText = recognizedText.trim();
			
			Log.v(TAG, "Tesseract output: " + recognizedText);
			
			
			
			
			
			if ( recognizedText.length() != 0 ) {
				_field3.setText(recognizedText);
				_field3.setSelection(_field3.getText().toString().length());
			}		
			
			final String searchKey = recognizedText;
			/*
			MyThread wikiThread = new MyThread(searchKey);
			wikiThread.start();
			*/
			
			
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
		
			
			_field4.setText(wikiresult);
			_field4.setSelection(0);
			
			
	
		}// onPhotoTaken Ends

	protected void onPhotoTaken() {	
		
		_taken = true;		

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
		
		// Start Tess
		
		TessRunnable tessRunnable = new TessRunnable(DATA_PATH,lang,bitmap);
		Thread tessThread = new Thread(tessRunnable);
		tessThread.start();
		
		
		try {
			tessThread.join();
		} catch (InterruptedException e) {
			Log.v(TAG,"tessThread Join Fail: "+ e.getMessage());
		}
		
		
		
		String	recognizedText = tessRunnable.getResult();
	
		
		//Ends Tess		
		
		if ( lang.equalsIgnoreCase("eng") ) {
			//remove dump marks
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9,.&-?!@%$*+=/]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		
		Log.v(TAG, "Tesseract output: " + recognizedText);
		
		if ( recognizedText.length() != 0 ) {
			_field3.setText(recognizedText);
			_field3.setSelection(_field3.getText().toString().length());
		}		
		
		final String searchKey = recognizedText;
		/*
		MyThread wikiThread = new MyThread(searchKey);
		wikiThread.start();
		*/
		
		
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
	
		
		_field4.setText(wikiresult);
		_field4.setSelection(0);
		
		

	}// onPhotoTaken Ends
}
