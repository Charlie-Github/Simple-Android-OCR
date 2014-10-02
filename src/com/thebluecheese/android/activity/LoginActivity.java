package com.thebluecheese.android.activity;


import java.util.HashMap;

import com.thebluecheese.android.basic.User;
import com.thebluecheese.android.network.thirdPartyUserParser;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity implements Callback, PlatformActionListener {

	String TAG = "BlueCheese";
	Button loginButton;
	Button signUpButton;
	Button thirdPartyButton;
	EditText emailText;
	EditText pwdText;
	TextView errorText;
	Context context;
	ProgressDialog progressDialog;
	User user;
	String result;
	
	//message code for shareSDK
	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR= 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	Platform plt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		context = this;
		emailText = (EditText)findViewById(R.id.textEmail);
		pwdText = (EditText)findViewById(R.id.textPwd);
		errorText = (TextView)findViewById(R.id.loginErrortext);
		
		loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new loginClickHandler());
		
		signUpButton = (Button)findViewById(R.id.signUpButton);
		signUpButton.setOnClickListener(new signupClickHandler());
		
		thirdPartyButton = (Button)findViewById(R.id.thirdButton);
		thirdPartyButton.setOnClickListener(new thirdPartyClickHandler());
		
		
		plt = ShareSDK.getPlatform(this,Facebook.NAME);//new Facebook(context);
		user = new User();
		
		LoginTryAsyncTask lhelper = new LoginTryAsyncTask(context,progressDialog);
		lhelper.execute();
		
	}
	
	public class loginClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			LoginActivityAsyncTask lhelper = new LoginActivityAsyncTask(emailText,pwdText,errorText,loginButton,progressDialog,context);
			lhelper.execute();
		}
	}
	
	public class signupClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			//start sign up
			Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
			startActivity(intent);
		}
	}
	
	public class thirdPartyClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			//third party button
			authorize(plt);			
		}
	}
	
	private void authorize(Platform plat) {
				
		if (plat == null) {
			Log.e(TAG,"third party login is null.");
			return;
		}
		
		if(plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
				login(plat.getName(), userId, null);
				return;
			}
			
		}
		
		plat.setPlatformActionListener(this);
		plat.SSOSetting(true); // true表示不使用SSO方式授权
		plat.showUser(null);
		//plat.authorize();
		
	}
	
	public void onComplete(Platform platform, int action,HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
			login(platform.getName(), platform.getDb().getUserId(), res);
			
			//parse third party user info from "res"
			user = thirdPartyUserParser.parseFacebook(res);			
			storeUser(user);
			
			//Tiger blue cheese user system
			LoginThirdpartyAsyncTask lhelper = new LoginThirdpartyAsyncTask(progressDialog,context);
			lhelper.execute();
		}
		
		Log.i(TAG, "login onComplete res: "+ res.get("name").toString());
	}
	
	private void login(String plat, String userId, HashMap<String, Object> userInfo) {
		Message msg = new Message();
		msg.what = MSG_LOGIN;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}
	
	public void storeUser(User user){
		// treat this as first time login user
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);		
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
	
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
		}
		Log.i(TAG, "login onError: "+ t);		
	}
	
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
		}
	}	
	
	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
		case MSG_USERID_FOUND: {
			//Toast.makeText(this, "User found", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "userid_found");
		}
		break;
		case MSG_LOGIN: {			
			//String text = getString(R.string.logining, msg.obj);
			//Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "login ing");
			
		}
		break;
		case MSG_AUTH_CANCEL: {
			//Toast.makeText(this, "Auth cancel", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "auth_cancel");
		}
		break;
		case MSG_AUTH_ERROR: {
			//Toast.makeText(this, "Auth error", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "auth_error");
		}
		break;
		case MSG_AUTH_COMPLETE: {
			Toast.makeText(this, "Auth complete", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "auth_complete");
		}
		break;
	}
	return false;
	}	
	
}
