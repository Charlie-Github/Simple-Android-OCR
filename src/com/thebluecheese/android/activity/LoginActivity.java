package com.thebluecheese.android.activity;


import java.util.HashMap;

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
			
			authorize(new SinaWeibo(context));
			
		}
	}
	
	private void authorize(Platform plat) {
		if (plat == null) {
			
			return;
		}
		
		if(plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				UIHandler.sendEmptyMessage(1, this);
				login(plat.getName(), userId, null);
				return;
			}
		}
		//plat.setPlatformActionListener(this);
		plat.SSOSetting(false);
		plat.showUser(null);
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
		}
		break;
		case MSG_LOGIN: {
			
			String text = getString(R.string.logining, msg.obj);
			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			
//			Builder builder = new Builder(this);
//			builder.setTitle(R.string.if_register_needed);
//			builder.setMessage(R.string.after_auth);
//			builder.setPositiveButton(R.string.ok, null);
//			builder.create().show();
		}
		break;
		case MSG_AUTH_CANCEL: {
			Toast.makeText(this, "auth_cancel", Toast.LENGTH_SHORT).show();
		}
		break;
		case MSG_AUTH_ERROR: {
			Toast.makeText(this, "auth_error", Toast.LENGTH_SHORT).show();
		}
		break;
		case MSG_AUTH_COMPLETE: {
			Toast.makeText(this, "auth_complete", Toast.LENGTH_SHORT).show();
		}
		break;
	}
	return false;
	}

	
	
}
