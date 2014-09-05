package com.thebluecheese.android.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class CameraActivity extends Activity {

	private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private Bitmap mbitmap;
	protected ImageView _imageView;
	protected ImageView _backgroudimageView;
	protected ImageButton captureButton;
	protected LinearLayout scroll_layout;
	private ProgressDialog progressDialog;
    
    public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/BlueCheese/";
	protected String _path = DATA_PATH + "/ocr.jpg"; // image file	
	public static final String lang = "eng";

    public static String TAG = "BlueCheese";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        
                
        // Add a listener to the Capture button
		
        captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new PhotoTakenHandler());
    }
    
    public class PhotoTakenHandler implements View.OnClickListener {
		public void onClick(View view) {
			
			// return back to this activity
			Log.v(TAG, "Starting Camera Preview");
			 mCamera.takePicture(new Camera.ShutterCallback() { @Override public void onShutter(){}}, 
             		null, 
             		mPicture);
		}
	}
    
	@Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Camera onPause");
        try
        {    
            // release the camera immediately on pause event
             mCamera.stopPreview(); 
             mCamera.setPreviewCallback(null);
             mPreview.getHolder().removeCallback(mPreview);
             mCamera.release();
             mCamera = null;

        }
        catch(Exception e)
        {
            Log.e(TAG,"error onPause: "+e.getMessage());
        }
    }
	
	@Override
	protected void onResume(){		
		Log.d(TAG, "Camera onResume");
		super.onResume();
		// Create an instance of Camera
        mCamera = getCameraInstance();
        setCameraParameters();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        setDisplayOrientation();// let preview vertical
        preview = (FrameLayout) findViewById(R.id.camera_preview);        
        preview.addView(mPreview);	
	}
	
	
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	    	Log.e(TAG,"get Camera Instance error: "+ e.getMessage());
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	public void setCameraParameters(){
		// get Camera parameters
		Camera.Parameters params = mCamera.getParameters();
		// set the focus mode
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		// set zoom
		params.setZoom(10);
		// set Camera parameters
		mCamera.setParameters(params);
	}	
	
	
	private PictureCallback mPicture = new PictureCallback() {
	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {	    	
	    	mbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	    	// rotate picture
	    	mbitmap = rotateBitmap(mbitmap,90);
	    	
	    	mbitmap = drawRect(mbitmap);
	    	
	    	// create file
	        File pictureFile = new File(DATA_PATH+ File.separator +"ocr.jpg");
	        File corppedFile = new File(DATA_PATH+ File.separator +"ocr_crop.jpg");

	        //original
	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            Log.v("test", "file: "+ pictureFile.getAbsolutePath().toString() );
	            // write bitmap to file //fos.write(data);
	            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);	            
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d(TAG, "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d(TAG, "Error accessing file: " + e.getMessage());
	        }	        
	        
	        // crop
	    	mbitmap = cropBitmap(mbitmap);	
	        try {
	            FileOutputStream fos = new FileOutputStream(corppedFile);
	            Log.v("test", "file: "+ corppedFile.getAbsolutePath().toString() );
	            // write bitmap to file //fos.write(data);
	            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);	            
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d(TAG, "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d(TAG, "Error accessing file: " + e.getMessage());
	        }
	        
	        Intent returnIntent = new Intent();
	        setResult(RESULT_OK, returnIntent);
	        finish();
	        
	    }
	};
	
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
	private static File getOutputMediaFile(){
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile = new File(DATA_PATH+ File.separator +"OCR.jpg");	   
        //mediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpg");
    	//mediaFile = new File(DATA_PATH+ File.separator +"IMG_"+ timeStamp + ".jpg");	  
	    return mediaFile;
	}	
	private Bitmap rotateBitmap(Bitmap bitmap,int degree){
		Matrix matrix = new Matrix();
    	matrix.preRotate(degree);
    	bitmap = Bitmap.createBitmap(bitmap ,0,0, bitmap .getWidth(), bitmap .getHeight(),matrix,true);
    	return bitmap;
	}
	private void setDisplayOrientation(){
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degree = 0;
		switch (rotation) {
			case Surface.ROTATION_0:	degree = 0; break;
			case Surface.ROTATION_90:	degree = 90; break;
			case Surface.ROTATION_180:	degree = 180; break;
			case Surface.ROTATION_270:	degree = 270; break;
		}
		int result;
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(0, info);
		if(info.facing == CameraInfo.CAMERA_FACING_FRONT){
			result = (info.orientation + degree) % 360;
			result = (360 - result) % 360;
		}else{
			result =(info.orientation - degree + 360 ) % 360;
		}
		mCamera.setDisplayOrientation(result);		
	}
}