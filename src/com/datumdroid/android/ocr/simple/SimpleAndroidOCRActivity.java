package com.datumdroid.android.ocr.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.thebluecheese.android.basic.Food;
import com.thebluecheese.android.network.*;

public class SimpleAndroidOCRActivity extends Activity {

	private static final String TAG = "SimpleOCR";
	protected Button _button;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/*Test area below*/
		GetRunner getR = new GetRunner("http://default-environment-9hfbefpjmu.elasticbeanstalk.com/food", "title=chicken");
		Thread getThread = new Thread(getR);
		getThread.start();
		
		try {
			getThread.join();
		} catch (InterruptedException e) {
			
		}	
		
		String res = getR.getResult();
		
		JsonParser jp = new JsonParser();
		Food f = jp.parseFood(res);
		Log.v("SimpleOCR","food: "+ f._photos.get(0)._url);
		
		/*Test Ends*/
		
		
		_button = (Button) findViewById(R.id.button);
		_button.setOnClickListener(new ButtonClickHandler());
	}

	public class ButtonClickHandler implements View.OnClickListener {
		//button handler class. Handle click event
		public void onClick(View view) {
			Log.v(TAG, "Starting CameraResultIntent");
			//startCameraActivity();//sample
			Intent intent = new Intent(SimpleAndroidOCRActivity.this, CameraResultActivity.class);
			startActivity(intent);
		}
	}
	
}
