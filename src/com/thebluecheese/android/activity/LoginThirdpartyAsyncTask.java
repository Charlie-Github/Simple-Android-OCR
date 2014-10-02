package com.thebluecheese.android.activity;

import com.thebluecheese.android.basic.User;
import com.thebluecheese.android.network.LoginHelper;
import com.thebluecheese.android.network.RegisterHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class LoginThirdpartyAsyncTask extends AsyncTask<String, Integer, String> {
	ProgressDialog _progressDialog;
	Context _context;
	String TAG = "BlueCheese";
	
	String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	
	public LoginThirdpartyAsyncTask(ProgressDialog pd,Context ct){
		_progressDialog = pd;
		_context = ct;
	}

	@Override
	protected String doInBackground(String... params) {		
		
		if(tryLogin()||login()){
			Intent intent = new Intent(_context, CameraResultActivity.class);
			_context.startActivity(intent);
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {	
		
		String loadingmessage = _context.getResources().getString(R.string.logining);		
		_progressDialog.setMessage(loadingmessage);
		_progressDialog.show();	

    }

	
	public boolean login(){
		//register third party user and login
		User tempUser = new User();
		boolean loginState = false;
		SharedPreferences sharedPre = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		String email = sharedPre.getString("email", "");
		String password = sharedPre.getString("pwd", "");
		String userName = sharedPre.getString("name","");
		//post register request
		RegisterHelper rh = new RegisterHelper(email,password,userName);
		Thread postThread = new Thread(rh);
		postThread.start();			
		try {
			//waiting for get response
			postThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Exception" + e);			
		}		
		tempUser = rh.getUser();		
		
		//check login status
		if(tempUser._log.equals("register user succeed")){
						
			loginState = true;
			
		}else{
			//login failed
			loginState = false;
			
		}
		Log.i(TAG, "third party user signup login state: "+ loginState);
		return loginState;
	}
	
	public boolean tryLogin(){
		
		//try to login server using third party user info seeking existing user
		boolean loginState = false;
		User tempUser = new User();
				
		SharedPreferences sharedPre = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		String email = sharedPre.getString("email", "");
		String password = sharedPre.getString("pwd", "");

		//Post login request		
		LoginHelper lh = new LoginHelper(email , password);
		Thread postThread = new Thread(lh);
		postThread.start();			
		try {
			//waiting for get response
			postThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Exception" + e);			
		}		
		tempUser = lh.getUser();
		
		if(tempUser._log.equals("login succeed")){
			//user already loged in once
			loginState = true;
		}else{
			loginState = false;
		}
		Log.i(TAG, "third party user try login state: "+ loginState);
		return loginState;
		
	}
}
