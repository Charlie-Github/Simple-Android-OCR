package com.thebluecheese.android.ocr;

import java.util.ArrayList;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.util.Log;

public class ImagePreProcessor{
	public String TAG = "Image Process";
	
	public Bitmap process(Bitmap inputBitmap){
		Bitmap tempBitmap = inputBitmap;
		int height = tempBitmap.getHeight();
		int width = tempBitmap.getWidth();
		
		Mat imgMAT = new Mat(height , width , CvType.CV_8U, new Scalar(4));
		Utils.bitmapToMat(tempBitmap, imgMAT);
		
		
		imgMAT = adaptiveThreshold(imgMAT);

		
		try {
			Imgproc.cvtColor(imgMAT, imgMAT, Imgproc.COLOR_RGBA2GRAY, 4);
		    Bitmap nbitmap = Bitmap.createBitmap(imgMAT.cols(), imgMAT.rows(), Bitmap.Config.ARGB_8888);   
		    Utils.matToBitmap(imgMAT, nbitmap);		    
		    tempBitmap = nbitmap;
		}
		catch (CvException e){
			Log.d(TAG,e.getMessage());
		}
		
		return tempBitmap;		
	}
	
	
	public Mat erode(Mat inputMat){		
		int erosion_size = 1;
		Mat element  = Imgproc.getStructuringElement(
		    Imgproc.MORPH_ELLIPSE, new Size(2 * erosion_size + 1, 2 * erosion_size + 1), 
		    new Point(erosion_size, erosion_size)
		);
		Imgproc.erode(inputMat, inputMat, element);
	    return inputMat;		
	}
	
	public Mat dilate(Mat img){
	    int dilation_type;	    
	    int dilation_size = 1;
	    
	    dilation_type = Imgproc.MORPH_ELLIPSE;	    
	    Mat element = Imgproc.getStructuringElement( dilation_type,
	                                            new Size( 2*dilation_size + 1, 2*dilation_size+1 ),
	                                            new Point( dilation_size, dilation_size ) );
	    /// Apply the dilation operation
	    Imgproc.dilate( img, img, element );
	    
	    return img;
	    
	}
	
	public Mat adaptiveThreshold(Mat img_threshold){
	   try { 
	    	Imgproc.cvtColor(img_threshold, img_threshold, Imgproc.COLOR_RGBA2GRAY, 4);
	    
	    
		    Size size = new Size(3,3);
		    size.height = 3;
		    size.width = 3;
		    
		    //Imgproc.GaussianBlur(img_threshold, img_threshold, size, 0.5);
		    //Imgproc.threshold(img_threshold, img_threshold, 0,255, Imgproc.THRESH_TRUNC | Imgproc.THRESH_OTSU);//
		    
		    //--Simple end here
		    
		    Imgproc.adaptiveThreshold(img_threshold, img_threshold, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,11, 2);
		    
		    Imgproc.cvtColor(img_threshold, img_threshold, Imgproc.COLOR_GRAY2BGRA);
		}
		catch (CvException e){
			Log.d(TAG,e.getMessage());
		}
	    return img_threshold;
	}
	
	
}