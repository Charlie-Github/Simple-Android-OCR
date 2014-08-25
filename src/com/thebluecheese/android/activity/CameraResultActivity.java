package com.thebluecheese.android.activity;

import java.io.File;
import java.io.IOException;

import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.ocr.TessHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraResultActivity extends Activity {
	public static final String PACKAGE_NAME = "com.thebluecheese.android.activity";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";	

	public static final String lang = "eng";
	private static final String TAG = "SimpleOCR";
	
	protected ImageView _imageView;
	protected ImageView _backgroudimageView;
	protected LinearLayout scroll_layout;
	
	protected String _path;
	protected boolean _taken;
	protected static final String PHOTO_TAKEN = "photo_taken";	
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_result);
		
		_imageView = (ImageView)findViewById(R.id.imagview);
		scroll_layout = (LinearLayout) findViewById(R.id.camera_result_scroll_linear);
		_path = DATA_PATH + "/ocr.jpg";		
		
		_backgroudimageView = (ImageView)findViewById(R.id.imagbackground);		
		_backgroudimageView.setOnClickListener(new BackgroundClickHandler());
		startCameraActivity();		
	}
	
	public class BackgroundClickHandler implements View.OnClickListener {
		//button handler class. Handle click event
		public void onClick(View view) {
			Log.v(TAG, "Starting CameraResultIntent");
			//startCameraActivity();//sample
			Intent intent = new Intent(CameraResultActivity.this, CameraResultActivity.class);
			startActivity(intent);
		}
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
					
			
			 TessHelper tesshp = new TessHelper(DATA_PATH,lang,bitmap,_imageView,_backgroudimageView,scroll_layout,progressDialog,this);//"this"is context
			 //tesshp.execute();
			// Execute in parallel
			 tesshp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}// readImage Ends
	
	
}
