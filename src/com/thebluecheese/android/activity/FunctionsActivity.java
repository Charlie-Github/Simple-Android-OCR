package com.thebluecheese.android.activity;

import com.thebluecheese.android.activity.FoodSearchActivity.ButtonClickHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class FunctionsActivity extends Activity { 
	private TabHost myTabHost; 
	@Override protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.functions); 
		// Recuperation du	TabHost 
		myTabHost =(TabHost) findViewById(R.id.TabHost01); 
		// Before adding tabs, it is imperative to call the method setup() 
		myTabHost.setup(); 
		// Adding tabs 
		// tab1 settings 
		TabSpec spec = myTabHost.newTabSpec("tab_creation"); 
		// text and image of tab 
		spec.setIndicator("Create adresse",getResources().getDrawable(android.R.drawable.ic_menu_add)); 
		// specify layout of tab 
		spec.setContent(R.id.onglet1);
		// adding tab in TabHost 
		myTabHost.addTab(spec);	
		ImageButton _searchButton = (ImageButton)findViewById(R.id.searchButton);
		_searchButton.setOnClickListener(new ButtonClickHandler());
		
		
		myTabHost.addTab(myTabHost.newTabSpec("tab_inser").setIndicator("Delete",getResources().getDrawable(android.R.drawable.ic_menu_edit)).setContent(R.id.Onglet2));
		
		myTabHost.addTab(myTabHost.newTabSpec("tab_affiche").setIndicator("Show All",getResources().getDrawable(android.R.drawable.ic_menu_view)).setContent(R.id.Onglet3));
	}
	public class ButtonClickHandler implements View.OnClickListener {		
		public void onClick(View view) {
			Log.i("BlueCheese","oaoidkja");
		}
	}
}



