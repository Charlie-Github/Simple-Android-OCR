package com.thebluecheese.android.activity;

import com.thebluecheese.android.network.DownloadImageTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class UserCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_center);
	}


	/*
	 * ImageView imageView = new ImageView(_context);
			foodImageAddress = s3Address + photoKey;
			new DownloadImageTask(imageView).execute(foodImageAddress);			
			_linearlayout.addView(imageView);
	 * */
}
