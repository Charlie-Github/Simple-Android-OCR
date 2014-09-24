package com.thebluecheese.android.activity;



import android.app.Activity;
import android.os.Bundle;


public class LoginActivity extends Activity {
	
	String TAG = "BlueCheese";

	
	
	String result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		LoginActivityAsyncTask lhelper = new LoginActivityAsyncTask();
		lhelper.execute();
	}
	
	
	

}
