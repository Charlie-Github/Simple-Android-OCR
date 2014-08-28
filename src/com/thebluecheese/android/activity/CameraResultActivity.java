package com.thebluecheese.android.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.ocr.TessHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraResultActivity extends Activity {
	public static final String PACKAGE_NAME = "com.thebluecheese.android.activity";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/BlueCheese/";
	protected String _path = DATA_PATH + "/ocr.jpg"; // image file
	
	public static final String lang = "eng";
	private static final String TAG = "SimpleOCR";
	
	protected ImageView _imageView;
	protected ImageView _backgroudimageView;
	protected LinearLayout scroll_layout;
	protected Button _searchBytype;
	
	
	
	protected boolean _taken;
	protected static final String PHOTO_TAKEN = "photo_taken";	
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// first install, prepare folder for tesseract
		prepareFolder();		
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_result);
		
		_imageView = (ImageView)findViewById(R.id.imagview);	
		_searchBytype = (Button)findViewById(R.id.searhByTypeButton);
		_searchBytype.setOnClickListener(new ButtonClickHandler());
		scroll_layout = (LinearLayout) findViewById(R.id.camera_result_scroll_linear);		
		_backgroudimageView = (ImageView)findViewById(R.id.imagbackground);
		_backgroudimageView.setOnClickListener(new BackgroundClickHandler());
		
		startCameraActivity();
	}
	
	public class PhotoClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			// return back to this activity
			Log.v(TAG, "Starting Camera Result Activity");
			Intent intent = new Intent(CameraResultActivity.this, CameraResultActivity.class);
			startActivity(intent);
		}
	}
	
	public class BackgroundClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			// return back to this activity
			Log.v(TAG, "Starting Camera Result Activity");
			Intent intent = new Intent(CameraResultActivity.this, CameraResultActivity.class);
			startActivity(intent);
		}
	}
	
	public class ButtonClickHandler implements View.OnClickListener {
		// call food search activity
		public void onClick(View view) {
			Log.v(TAG, "Starting Food search activity");
			Intent intent = new Intent(CameraResultActivity.this, FoodSearchActivity.class);
			startActivity(intent);
		}
	}
	
	protected void prepareFolder(){		
		//create a file folder
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };		
		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on external dir");
				}
			}			
		}
		
		// lang.traineddata file with the app (in assets folder)
		// http://code.google.com/p/tesseract-ocr/downloads/list
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {			
				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/" + lang + ".traineddata");			
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;					
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();					
				out.close();					
				Log.v(TAG, "Copied " + lang + " trainedData");
			} catch (IOException e) {
				Log.e(TAG, "Exception when copy " + lang + " traineddata " + e.toString());
			}
		}
	}

	protected void startCameraActivity() {
		// Simple android photo capture:
		// http://labs.makemachine.net/2010/03/simple-android-photo-capture/
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);
		Log.i(TAG,"OCR image path: "+ _path);
		// create camera intent
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "Camera result Code: " + resultCode);

		if (resultCode == -1) {			
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
				
				Log.v(TAG, "Image Orient: " + exifOrientation);	
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
	
				Log.v(TAG, "Image Rotation: " + rotate);
	
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
					
			
			TessHelper tesshp = new TessHelper(DATA_PATH,lang,bitmap,_imageView,_backgroudimageView,scroll_layout,progressDialog,this);//"this"is context
			//tesshp.execute();
			// Execute in parallel
			tesshp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}// readImage Ends
	
	
}