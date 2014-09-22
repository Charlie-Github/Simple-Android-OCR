package com.thebluecheese.android.activity;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AboutusActivity extends Activity {
	protected Button _testbutton;
	String TAG = "TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		_testbutton = (Button)findViewById(R.id.testbutton);
		_testbutton.setOnClickListener(new ButtonClickHandler());
		
		
	}
	
	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			
			// return back to this activity
			Log.v(TAG, "Starting Camera Result Activity");
			Intent intent = new Intent(AboutusActivity.this, LoginActivity.class);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aboutus, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
