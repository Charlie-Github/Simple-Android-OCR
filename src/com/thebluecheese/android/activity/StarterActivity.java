package com.thebluecheese.android.activity;

import cn.sharesdk.framework.ShareSDK;


import com.thebluecheese.android.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class StarterActivity extends Activity {

	private static final String TAG = "BlueCheese";
	protected Button _button;
	protected TextView _aboutUsText;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*=====================Test area below===========================*/
		
		ShareSDK.initSDK(this);
		ShareSDK.removeCookieOnAuthorize(true); // in order to allow another user login
		
		
		
		
		
		/*=======================Test Ends================================*/		
		_button = (Button) findViewById(R.id.button);
		_button.setOnClickListener(new ButtonClickHandler());
		_aboutUsText = (TextView) findViewById(R.id.aboutUsText);
		_aboutUsText.setOnClickListener(new TextClickHandler());
	}

	public class ButtonClickHandler implements View.OnClickListener {		
		public void onClick(View view) {
			Log.v(TAG, "Starting Camera Result Intent");
			Intent intent = new Intent(StarterActivity.this, CameraResultActivity.class);
			startActivity(intent);
		}
	}
	public class TextClickHandler implements View.OnClickListener {		
		public void onClick(View view) {
			Log.v(TAG, "Starting About us");
			Intent intent = new Intent(StarterActivity.this, AboutusActivity.class);
			startActivity(intent);
		}
	}
	
}
