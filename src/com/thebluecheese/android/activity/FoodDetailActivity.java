package com.thebluecheese.android.activity;

import java.util.Locale;

import com.thebluecheese.android.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class FoodDetailActivity extends Activity {
	// protected ImageView _image;
	protected String TAG = "BlueCheese";
	
	protected ImageButton _shareButton;
	Button _moreinfoButton;
	protected EditText _field5;
	protected EditText _field6;
	protected EditText _field7;
	protected LinearLayout root_view;
	protected LinearLayout detail_layout;
	protected LinearLayout scroll_layout;
	protected LinearLayout reviews_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_detail);
		_shareButton = (ImageButton) findViewById(R.id.shareButton);
		_moreinfoButton = (Button) findViewById(R.id.moreinfoButton);
		_field5 = (EditText) findViewById(R.id.field5);
		_field6 = (EditText) findViewById(R.id.filed6);
		_field7 = (EditText) findViewById(R.id.filed7);
		root_view = (LinearLayout) findViewById(R.id.rootView);
		detail_layout = (LinearLayout) findViewById(R.id.detailView);
		scroll_layout = (LinearLayout) findViewById(R.id.food_photo_scroll_linear);
		reviews_layout = (LinearLayout) findViewById(R.id.reviewsView);
		Intent intent = getIntent();
		
		String foodTitlte = intent.getStringExtra("FOOD_TITLE");
		String foodName = intent.getStringExtra("FOOD_NAME");
		
		//convert food title to "Upper lower" format
		foodTitlte = foodTitlte.toLowerCase(Locale.ENGLISH);		
		foodTitlte = Character.toString(foodTitlte.charAt(0)).toUpperCase(Locale.ENGLISH)+foodTitlte.substring(1);
		
		_field5.setText(foodTitlte);
		_field6.setText(foodName);
		
		//wiki sample
		//wikiHelper whelper = new wikiHelper(foodTitlte,_field7,this);
		//whelper.execute();
		
		FoodDetailActivityAsyncTask fhelper = new FoodDetailActivityAsyncTask(foodTitlte,_field7,_shareButton,_moreinfoButton,root_view,detail_layout,scroll_layout,reviews_layout,this);
		fhelper.execute();
		
		
	}
	
		
}
