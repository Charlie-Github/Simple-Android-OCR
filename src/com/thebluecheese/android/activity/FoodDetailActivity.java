package com.thebluecheese.android.activity;

import java.util.Locale;

import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.network.FoodDetailHelper;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

public class FoodDetailActivity extends Activity {
	// protected ImageView _image;
		protected EditText _field5;
		protected EditText _field6;
		protected EditText _field7;
		protected LinearLayout scroll_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_detail);
		_field5 = (EditText) findViewById(R.id.field5);
		_field6 = (EditText) findViewById(R.id.filed6);
		_field7 = (EditText) findViewById(R.id.filed7);
		scroll_layout = (LinearLayout) findViewById(R.id.food_photo_scroll_linear);
		Intent intent = getIntent();
		
		String foodTitlte = intent.getStringExtra("FOOD_TITLE");
		String foodName = intent.getStringExtra("FOOD_NAME");
		foodTitlte = foodTitlte.toLowerCase(Locale.ENGLISH);
		foodTitlte =  Character.toString(foodTitlte.charAt(0)).toUpperCase(Locale.ENGLISH)+foodTitlte.substring(1);
		
		_field5.setText(foodTitlte);
		_field6.setText(foodName);
		
		//wiki sample
		//wikiHelper whelper = new wikiHelper(foodTitlte,_field7,this);
		//whelper.execute();
		
		FoodDetailHelper fhelper = new FoodDetailHelper(foodTitlte,_field7,scroll_layout,this);
		fhelper.execute();
		
	}

		
}
