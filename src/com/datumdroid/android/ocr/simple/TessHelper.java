package com.datumdroid.android.ocr.simple;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessHelper   {
	private String DATA_PATH;
	private String lang;
	private Bitmap bitmap;	
	
	public TessHelper(String path, String language, Bitmap bit){
		DATA_PATH = path;
		lang = language;
		bitmap = bit;		
	}	
	
	public String recognize(){
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();
		
		if ( lang.equalsIgnoreCase("eng") ) {
			//remove dump marks
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9,.&-?!@%$*+=/]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		
		return recognizedText;
		
	}
	
}
