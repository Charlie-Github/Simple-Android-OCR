package com.thebluecheese.android.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.thebluecheese.android.basic.User;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class AboutusActivity extends Activity implements PlatformActionListener{
	protected Button _testbutton;
	protected Button _testbutton2;
	protected RelativeLayout aboutView;
	Context context;
	String TAG = "BlueCheese";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		_testbutton = (Button)findViewById(R.id.testbutton);
		_testbutton.setOnClickListener(new ButtonClickHandler());
		
		_testbutton2 = (Button)findViewById(R.id.testbutton2);
		_testbutton2.setOnClickListener(new Button2ClickHandler());
		
		aboutView = (RelativeLayout)findViewById(R.id.aboutusView);
		context = this;
	}
	
	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			
			Intent intent = new Intent(AboutusActivity.this, LoginActivity.class); //login activity
			//Intent intent = new Intent(AboutusActivity.this, FunctionsActivity.class);// functions activity
			startActivity(intent);
			
			
			/*
			//Weibo share
			ShareParams sp = new ShareParams();
			sp.setText("测试分享的文本");
			sp.setImagePath("");
			Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			
			// 执行图文分享
			weibo.share(sp);
			*/	
			
		}
	}
	
	public class Button2ClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			
			removeThirdAccount();
			cleanUser();
			Log.i(TAG,"Account Removed");
			
		}
	}
	
	public void removeThirdAccount(){
		Platform plat = ShareSDK.getPlatform(this, Facebook.NAME);
		plat.removeAccount();		
	}
	
	public void cleanUser(){
		// treat this as first time login user
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);		
		Editor editor = sharedPreferences.edit();
		editor.putString("email", "");
		editor.putString("pwd", "");
		editor.putString("name", "");
		editor.putString("uid", "");
		editor.putString("selfie", "");
		editor.putString("gender", "");
		editor.putString("age","");
		editor.commit();
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	
}
