package com.datumdroid.android.ocr.simple;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessRunnable implements Runnable {
	private String DATA_PATH;
	private String lang;
	private Bitmap bitmap;
	private volatile String result;
	
	public TessRunnable(String path, String language, Bitmap bit){
		DATA_PATH = path;
		lang = language;
		bitmap = bit;		
	}
	
	@Override
	public void run() {
		
		result = recognize();

	}
	
	public String recognize(){
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();	
		
		return recognizedText;
		
	}
	
	public String getResult() {
        return result;
    }

}
