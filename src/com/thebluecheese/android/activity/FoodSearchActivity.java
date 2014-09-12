package com.thebluecheese.android.activity;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FoodSearchActivity extends Activity {
	
	protected ImageButton _searchButton;
	protected EditText _searchField;
	protected LinearLayout _linearResult;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_search);
		
		
		_linearResult = (LinearLayout) findViewById(R.id.linear_search_results);
		_searchField = (EditText) findViewById(R.id.searchText);
		_searchField.addTextChangedListener(new TextWatcher());
		_searchButton = (ImageButton)findViewById(R.id.searchButton);
		_searchButton.setOnClickListener(new ButtonClickHandler());
	}
	
	public class ButtonClickHandler implements View.OnClickListener {		
		public void onClick(View view) {
			String inputString = _searchField.getText().toString();
			FoodSearchActivityAsyncTask lsh = new FoodSearchActivityAsyncTask(inputString,"all",_linearResult,FoodSearchActivity.this);
			lsh.execute();
		}
	}
	
	public class TextWatcher implements android.text.TextWatcher{
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}

		@Override
		public void afterTextChanged(Editable s) {
			String inputString = _searchField.getText().toString();
			FoodSearchActivityAsyncTask lsh = new FoodSearchActivityAsyncTask(inputString,"50",_linearResult,FoodSearchActivity.this);
			lsh.execute();
			
		}
	}
    
}
