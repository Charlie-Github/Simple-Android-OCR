package com.thebluecheese.android.activity;

import com.thebluecheese.android.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class StarterActivity extends Activity {

	private static final String TAG = "BlueCheese";
	protected Button _button;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*=====================Test area below===========================*/
		
		/*=======================Test Ends================================*/		
		_button = (Button) findViewById(R.id.button);
		_button.setOnClickListener(new ButtonClickHandler());
	}

	public class ButtonClickHandler implements View.OnClickListener {		
		public void onClick(View view) {
			Log.v(TAG, "Starting Camera Result Intent");
			Intent intent = new Intent(StarterActivity.this, CameraResultActivity.class);
			startActivity(intent);
		}
	}
	
}
