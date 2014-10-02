package com.thebluecheese.android.activity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.thebluecheese.android.basic.User;
import com.thebluecheese.android.network.LoginHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivityAsyncTask extends AsyncTask<String, Integer, String> {
	String TAG = "BlueCheese";
	
	Context _context;
	EditText _emailText;
	EditText _pwdText;
	TextView _errorText;
	Button _loginButton;
	ProgressDialog _progressDialog;
	
	String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	String responsText;
	String _email;
	String _pwd;
	boolean _loginState;
	User user;

	
	public LoginActivityAsyncTask(EditText emailText, EditText pwdText,TextView errorText,Button loginButton,ProgressDialog progressDialog, Context context){
		
		responsText = "";
		_emailText = emailText;
		_pwdText = pwdText;
		_errorText = errorText;
		_loginButton = loginButton;
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
		
		_email = _emailText.getText().toString();
		_pwd = md5(_pwdText.getText().toString());
		
		_loginState = firstTimeLogin(_email, _pwd);
		
		if(_loginState == true){
			Intent intent = new Intent(_context, CameraResultActivity.class);
			_context.startActivity(intent);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String Text) {
	   // execution of result of Long time consuming operation		
		_progressDialog.dismiss();
		
	 }
	
	protected void onProgressUpdate(Integer... progress) {
		
		String loadingmessage = _context.getResources().getString(R.string.logining);		
		_progressDialog.setMessage(loadingmessage);
		_progressDialog.show();	

    }
	
	public static String md5(String string) {
	    byte[] hash;

	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);

	    for (byte b : hash) {
	        int i = (b & 0xFF);
	        if (i < 0x10) hex.append('0');
	        hex.append(Integer.toHexString(i));
	    }

	    return hex.toString();
	}
	
	public boolean firstTimeLogin(String email, String password){
		User tempUser = new User();
		boolean loginState = false;
			
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
		
		//check login log
		if(tempUser._log.equals("login succeed")){
			//user verified by server
			storeUser(tempUser);
			loginState = true;
			
		}else{
			//login failed
			loginState = false;			
		}
		Log.i(TAG, "first time login state: "+ loginState);
		return loginState;
	}
	
	public void storeUser(User user){
		// treat this as first time login user
		SharedPreferences sharedPreferences = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);		
		Editor editor = sharedPreferences.edit();
		editor.putString("email", user._email);
		editor.putString("pwd", user._pwd);
		editor.putString("name", user._name);
		editor.putString("uid", user._uid+"");
		editor.putString("selfie", user._selfie);
		editor.putString("gender", user._gender);
		editor.putString("age", user._age);
		editor.commit();
	}
}
