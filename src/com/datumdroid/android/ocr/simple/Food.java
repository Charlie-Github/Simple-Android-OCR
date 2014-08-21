package com.datumdroid.android.ocr.simple;

public class Food {
	public String fid;
	public String ftitle;
	public String fname;

	public Food(String title){	
		
		this.ftitle = title;
				
	}
	
	@Override
	public String toString(){
		return ftitle;		
	}
}
