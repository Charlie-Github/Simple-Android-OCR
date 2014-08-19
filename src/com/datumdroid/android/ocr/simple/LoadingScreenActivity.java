package com.datumdroid.android.ocr.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LoadingScreenActivity extends Activity {

	//Introduce an delay
    private final int WAIT_TIME = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	System.out.println("LoadingScreenActivity  screen started");
	setContentView(R.layout.loading_screen);
	findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

	new Handler().postDelayed(new Runnable(){ 
	@Override 
	    public void run() { 
               
		    System.out.println("Going to Profile Data");	  
		    LoadingScreenActivity.this.finish(); 
		} 
	}, WAIT_TIME);
      }
}
