package com.thebluecheese.android.activity;


import com.thebluecheese.android.basic.User;
import com.thebluecheese.android.network.LoginHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class LoginTryAsyncTask extends AsyncTask<String, Integer, String>{
	
	String TAG = "BlueCheese";	
	Context _context;
	ProgressDialog _progressDialog;
	
	String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	String responsText;
	String _email;
	String _pwd;
	boolean _loginState;
	User user;
	
	public LoginTryAsyncTask(Context context, ProgressDialog progressDialog){
		responsText = "";
		user = new User();
		_context = context;
		_loginState = false;
		_progressDialog = progressDialog;
		_progressDialog = new ProgressDialog(_context);				
		_progressDialog.setCancelable(false);
		_progressDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected String doInBackground(String... params) {
		publishProgress(1);//call onProgressUpdate
		_loginState = tryLogin();//no input args
		
		if(_loginState == true){			
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
	
	@Override
	protected void onPostExecute(String Text) {
	   // execution of result of Long time consuming operation
		_progressDialog.dismiss();
	 }
	
	public boolean tryLogin(){
		
		//try to login server using exist info
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
		Log.i(TAG, "try login state: "+ loginState);
		return loginState;
		
	}

}
