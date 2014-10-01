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
import com.thebluecheese.android.network.RegisterHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SignupActivityAsyncTask extends AsyncTask<String, Integer, String> {
	String TAG = "BlueCheese";
	
	Context _context;
	EditText _emailText;
	EditText _pwdText;
	EditText _userNameText;
	Button _submitButton;
	protected ProgressDialog _progressDialog;
	
	String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	String responsText;
	String _name;
	String _email;
	String _pwd;
	boolean _loginState;
	User user;

	
	public SignupActivityAsyncTask(EditText emailText, EditText pwdText,EditText userNameText,ProgressDialog progressDialog, Context context){
		
		responsText = "";
		_emailText = emailText;
		_pwdText = pwdText;
		_userNameText = userNameText;
		
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
		// TODO Auto-generated method stub		
		
		// 1.try login
		publishProgress(1);//call onProgressUpdate
		
		
		_name = _userNameText.getText().toString().trim();
		_email = _emailText.getText().toString().trim();
		_pwd = md5(_pwdText.getText().toString().trim());
		_loginState = login(_email,_pwd,_name);
		
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
	

	public class submitClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			

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
	
	public boolean login(String email, String password,String userName){
		User tempUser = new User();
		boolean loginState = false;
		SharedPreferences sharedPreferences = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		
		//post request
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
		Log.i(TAG, "signup login state: "+ loginState);
		return loginState;
	}
	
	
}
