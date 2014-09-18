package com.thebluecheese.android.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class CameraActivity extends Activity {

	protected Camera mCamera;
	protected CameraPreview mPreview;
    protected FrameLayout preview;
    protected Bitmap mbitmap;
	protected ImageView _imageView;
	protected ImageView _backgroudimageView;
	protected ImageButton captureButton;
	protected LinearLayout scroll_layout;
    
	protected static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/BlueCheese/";
	
	protected String org_img = "ocr.jpg";
	protected static final String lang = "eng";
	
	protected File pictureFile;

	protected static String TAG = "BlueCheese";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);		
        captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new PhotoTakenHandler());
        // create file
        pictureFile = new File(DATA_PATH+ File.separator +org_img);
    }
    
    public class PhotoTakenHandler implements View.OnClickListener {
		public void onClick(View view) {
			// Add a listener to the Capture button
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
		List<String> focusModes = params.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		}		
		// set hdr
		//List<String> sceneModes = params.getSupportedSceneModes();
		//if (sceneModes.contains(Camera.Parameters.SCENE_MODE_HDR)) {
			//params.setSceneMode(Camera.Parameters.SCENE_MODE_HDR);
		//}		
		// set zoom
		int zoom = 10;
		int maxZoom = params.getMaxZoom(); 
		   if (params.isZoomSupported()) {
		      if (zoom >=0 && zoom < maxZoom) {
		    	  params.setZoom(zoom);
		      } else {
		        // zoom parameter is incorrect
		      }
		   }
		// set Camera parameters
		mCamera.setParameters(params);
	}	
	
	
	private PictureCallback mPicture = new PictureCallback() {
	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {	    	
	    	//mbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	    	// rotate picture
	    	//mbitmap = rotateBitmap(mbitmap,90);	

	        //original img
	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            Log.v(TAG, "file: "+ pictureFile.getAbsolutePath().toString() );
	            // write bitmap to file 
	            fos.write(data);
	            //mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);	            
	            fos.close();
	           //mbitmap.recycle();
	        } catch (FileNotFoundException e) {
	            Log.d(TAG, "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d(TAG, "Error accessing file: " + e.getMessage());
	        }
	        
	        // start result activity
	        Intent returnIntent = new Intent();
	        setResult(RESULT_OK, returnIntent);
	        finish();
	        
	    }
	};
	
	private File getOutputMediaFile(){
	    // Create a media file name
	    //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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