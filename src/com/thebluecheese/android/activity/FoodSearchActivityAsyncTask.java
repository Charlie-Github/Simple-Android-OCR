package com.thebluecheese.android.activity;

import java.util.Locale;






import com.thebluecheese.android.activity.R;
import com.thebluecheese.android.localdb.LocalDbOperator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FoodSearchActivityAsyncTask extends AsyncTask<String, Integer, String> {
	private Context _context;
	private LinearLayout _linearlayout;
	private String[] searchResult;
	private String _input;
	private String _searchType;

	public FoodSearchActivityAsyncTask(String inputSearch, String searchType,LinearLayout linearlayout,Context context){
		_input = inputSearch;
		_linearlayout = linearlayout;
		_context = context;
		_searchType = searchType;
	}
	
	 @Override
	 protected void onPreExecute(){
		
	 }
	
	@Override
	protected String doInBackground(String... params) {
		
		publishProgress(1);//call onProgressUpdate
		if(_searchType.equals("all")){
			searchFood(_input);		
		}else{
			int number = Integer.parseInt(_searchType);
				searchSomeFood(_input);
		}
		return _input;
	}
	
	protected void searchFood(String inputText){		
		//local search begin		
		LocalDbOperator ldboperator = new LocalDbOperator(_context);
		searchResult = ldboperator.blurSearch(inputText);
		
	}
	
	protected void searchSomeFood(String inputText){		
		//local search begin		
		LocalDbOperator ldboperator = new LocalDbOperator(_context);
		searchResult = ldboperator.blurTopSearch(inputText);
		
	}
	
	@Override
	protected void onPostExecute(String Text) {
	   	_linearlayout.removeAllViews();
		setfields();
	}
	
	protected void onProgressUpdate(Integer... progress) {		
		_linearlayout.removeAllViews();
		Button text = new Button(_context);
		text.setText(R.string.loading);
		
		//set button height
		//int dps = 100;
		//float scale = _context.getResources().getDisplayMetrics().density;
		//int pixels = (int) (dps * scale + 0.5f);
		//text.setHeight(pixels);
		text.setBackgroundColor(Color.TRANSPARENT);
		text.setGravity(Gravity.CENTER_VERTICAL);
		
		_linearlayout.addView(text);		
	}
	
	protected void setfields(){
		
		for(int i = 0; i<searchResult.length; i++){
			String[] key_pair = searchResult[i].split("\\|");
			final String title = key_pair[0];
			final String name = key_pair[1];			
			String ui_title = title.toLowerCase(Locale.ENGLISH);
			ui_title =  Character.toString(ui_title.charAt(0)).toUpperCase(Locale.ENGLISH)+ui_title.substring(1);			
			
			Button text = new Button(_context);
			text.setText(ui_title+" \n"+name);
			
			//set button height
			//int dps = 100;
			//float scale = _context.getResources().getDisplayMetrics().density;			
			//int pixels = (int) (dps * scale + 0.5f);
			//text.setHeight(pixels);
			text.setBackgroundColor(Color.TRANSPARENT);
			text.setGravity(Gravity.CENTER_VERTICAL);			
			_linearlayout.addView(text);
			
			// call food detail activity
			text.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					//set button's intent, opening detail activity
					Intent intent = new Intent(_context,FoodDetailActivity.class);
					intent.putExtra("FOOD_TITLE", title);
					intent.putExtra("FOOD_NAME", name);
					_context.startActivity(intent);
				}
			});		
			
		}		
		
	}


}
