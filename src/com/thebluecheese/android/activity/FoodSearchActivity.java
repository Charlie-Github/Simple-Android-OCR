package com.thebluecheese.android.activity;


import com.thebluecheese.android.localdb.LocalSearchHelper;
import android.app.Activity;
import android.os.Bundle;
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
		
		_searchButton = (ImageButton)findViewById(R.id.searchButton);
		_linearResult = (LinearLayout) findViewById(R.id.linear_search_results);
		_searchField = (EditText) findViewById(R.id.searchText);
		
		_searchButton.setOnClickListener(new ButtonClickHandler());
	}	
	
	
	public class ButtonClickHandler implements View.OnClickListener {		
		public void onClick(View view) {
			String inputString = _searchField.getText().toString();
			LocalSearchHelper lsh = new LocalSearchHelper(inputString,_linearResult,FoodSearchActivity.this);
			lsh.execute();
		}
	}
	

	
}
