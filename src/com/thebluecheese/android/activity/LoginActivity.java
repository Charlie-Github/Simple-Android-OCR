package com.thebluecheese.android.activity;


import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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


public class LoginActivity extends Activity implements Callback{

	String TAG = "BlueCheese";
	Button loginButton;
	Button signUpButton;
	Button thirdPartyButton;
	EditText emailText;
	EditText pwdText;
	TextView errorText;
	Context context;
	ProgressDialog progressDialog;
	
	String result;
	
	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR= 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		context = this;
		emailText = (EditText)findViewById(R.id.textEmail);
		pwdText = (EditText)findViewById(R.id.textPwd);
		errorText = (TextView)findViewById(R.id.loginErrortext);
		
		loginButton = (Button)findViewById(R.id.loginButton);
		signUpButton = (Button)findViewById(R.id.signUpButton);
		signUpButton.setOnClickListener(new signupClickHandler());
		thirdPartyButton = (Button)findViewById(R.id.thirdButton);
		thirdPartyButton.setOnClickListener(new thirdPartyClickHandler());
		
		LoginActivityAsyncTask lhelper = new LoginActivityAsyncTask(emailText,pwdText,errorText,loginButton,progressDialog,context);
		lhelper.execute();
		
	}
	
	public class signupClickHandler implements View.OnClickListener {
		public void onClick(View view) {			
			SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString("email", "");
			editor.putString("pwd", "");
			editor.putString("name", "");
			editor.putString("uid", "");			
			editor.commit();		
		}
	}
	
	public class thirdPartyClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Platform fc = new Facebook(context);
			authorize(fc);
			fc.getDb().getUserId();
			String openId = fc.getDb().getUserId(); // 获取用户在此平台的ID
			String nickname = fc.getDb().getUserName();//.get("nickname"); // 获取用户昵称
			String userIcon = fc.getDb().getUserIcon();//URL
			
			Log.i(TAG, "login userInfo: "+ nickname);
			Log.i(TAG, "login userInfo: "+ userIcon);
		}
	}
	
	private void authorize(Platform plat) {
		if (plat == null) {
			Log.e(TAG,"login dose not support: "+ plat.getName());
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
		//plat.setPlatformActionListener((PlatformActionListener) this);
		plat.SSOSetting(true);
		plat.showUser(null);
		
	}
	
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
			login(platform.getName(), platform.getDb().getUserId(), res);
		}
		Log.i(TAG, "login onComplete: "+ res);
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
	
	private void login(String plat, String userId, HashMap<String, Object> userInfo) {
		Message msg = new Message();
		msg.what = 2;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
		case MSG_USERID_FOUND: {
			Toast.makeText(this, "userid_found", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "userid_found");
		}
		break;
		case MSG_LOGIN: {			
			//String text = getString(R.string.logining, msg.obj);
			//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "login ing");
		}
		break;
		case MSG_AUTH_CANCEL: {
			Toast.makeText(this, "auth_cancel", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "auth_cancel");
		}
		break;
		case MSG_AUTH_ERROR: {
			Toast.makeText(this, "auth_error", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "auth_error");
		}
		break;
		case MSG_AUTH_COMPLETE: {
			Toast.makeText(this, "auth_complete", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "login message: "+ "auth_complete");
		}
		break;
	}
	return false;
	}

	
	
}
