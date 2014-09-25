package com.thebluecheese.android.activity;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;








import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;









import com.thebluecheese.android.basic.User;
import com.thebluecheese.android.network.GetRunner;
import com.thebluecheese.android.network.JsonParser;
import com.thebluecheese.android.network.LoginHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivityAsyncTask extends AsyncTask<String, Integer, String> {
	String TAG = "BlueCheese";
	
	Context _context;
	EditText _emailText;
	EditText _pwdText;
	Button _loginButton;
	
	String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	String responsText;
	String _email;
	String _pwd;
	boolean _loginState;
	User user;

	
	public LoginActivityAsyncTask(EditText emailText, EditText pwdText,Button loginButton, Context context){
		
		responsText = "";
		_emailText = emailText;
		_pwdText = pwdText;
		_loginButton = loginButton;
		user = new User();
		_context = context;
		_loginState = false;
		
		
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub		
		
		// 1.try login
		_loginState = tryLogin();//no input args
		
		
		if(_loginState == true){			
			Intent intent = new Intent(_context, CameraResultActivity.class);
			_context.startActivity(intent);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String Text) {
	   // execution of result of Long time consuming operation
		
		_loginButton.setOnClickListener(new loginClickHandler());
	  }

	public class loginClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			_email = _emailText.getText().toString();
			_pwd = md5(_pwdText.getText().toString());
			
			_loginState = firstTimeLogin(_email, _pwd);
			
			if(_loginState == true){
				Intent intent = new Intent(_context, CameraResultActivity.class);
				_context.startActivity(intent);
			}
		}
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
		SharedPreferences sharedPreferences = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		
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
			Editor editor = sharedPreferences.edit();//获取编辑器
			editor.putString("email", tempUser._email);
			editor.putString("pwd", password);
			editor.putString("name", tempUser._name);
			editor.putString("uid", tempUser._uid+"");
			editor.commit();
			loginState = true;
		}else{
			//login failed
			loginState = false;
		}
		Log.i(TAG, "first time login state: "+ loginState);
		return loginState;
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
