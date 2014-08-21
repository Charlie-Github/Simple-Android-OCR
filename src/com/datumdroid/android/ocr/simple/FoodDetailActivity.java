package com.datumdroid.android.ocr.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class FoodDetailActivity extends Activity {
	// protected ImageView _image;
		protected EditText _field5;
		protected EditText _field6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_detail);
		_field5 = (EditText) findViewById(R.id.field5);
		_field6 = (EditText) findViewById(R.id.filed6);
		Intent intent = getIntent();
		String foodTitlte = intent.getStringExtra("FOOD_TITLE");
		String foodName = intent.getStringExtra("FOOD_NAME");
		_field5.setText(foodTitlte);
		_field6.setText(foodName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.food_detail, menu);
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
