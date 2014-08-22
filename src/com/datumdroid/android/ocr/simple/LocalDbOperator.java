package com.datumdroid.android.ocr.simple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class LocalDbOperator {
	private ExternalDbOpenHelper dbOpenHelper;
	private SQLiteDatabase database;
	private TextParser textparser;
	private HashSet<String> keywordsCombo;
	
	public LocalDbOperator(Context context){
		dbOpenHelper = new ExternalDbOpenHelper(context,"localDB.db");
		database = dbOpenHelper.openDataBase();
		keywordsCombo = new HashSet<String>();
	}
	
	public String[] search(String fromTess){
		
		String[] translates = TextParser.spliteAll(fromTess);
		
		this.combineKeywords(translates,3);
		Log.i("LocalDB", "Combo: "+ keywordsCombo.toString());		
		String[] comboArray = keywordsCombo.toArray(new String[keywordsCombo.size()]);
		
		ArrayList<String> translated = new ArrayList<String>();
		for(int i = 0; i < comboArray.length; i++){			
			int fid = this.searchByName(comboArray[i]);			
			String chinese_name = this.searchById(fid);
			if(chinese_name != ""){
				translated.add(comboArray[i]+"|"+chinese_name);// en_Name|zh_name, pair
				Log.i("LocalDB","Translate: "+chinese_name);
			}
		}
		
		String[] array = translated.toArray(new String[translated.size()]);
		return array;
	}
	
	public String searchById(int id){
		// search Chinese name by id
		// query(table_name,col_names[],where_statement,where_args_)
		Cursor foodCursor = database.query("Chinese",new String[]{"name"}, "_id = "+id,null, null, null, null);
		foodCursor.moveToFirst();
		String name = "";
		if(!foodCursor.isAfterLast()) {
            do {
                name = foodCursor.getString(0);
                Log.i("LocalDB", "zh_name: "+name);
            } while (foodCursor.moveToNext());
        }
		foodCursor.close();
		return name;		
	}
	public int searchByName(String name){
		// search food _id by name
		name = name.toUpperCase(Locale.ENGLISH);
		name = name.trim();
		int id = 0;
		if (name!=""){
			// query(table_name,col_names[],where_statement,where_args_)
			Cursor foodCursor = database.query("Keyword",new String[]{"_id"}, "upper(title) = \'"+name+"\'",null, null, null, null);
			foodCursor.moveToFirst();
			
			if(!foodCursor.isAfterLast()) {
	            do {
	                id = foodCursor.getInt(0);
	                Log.i("LocalDB", "_id: "+ id);
	            } while (foodCursor.moveToNext());
	        }
			foodCursor.close();
		}
		return id;
	}
	
	public int searchByNamePreBlur(String name){
		// search food _id by blur name
		name = name.toUpperCase(Locale.ENGLISH);
		
		// query(table_name,col_names[],where_statement,where_args_)
		Cursor foodCursor = database.query("Keyword",new String[]{"_id"}, "upper(title) like \'"+name+"%\'",null, null, null, null);
		foodCursor.moveToFirst();
		int id = 0;
		if(!foodCursor.isAfterLast()) {
            do {
                id = foodCursor.getInt(0);
                Log.i("LocalDB", "blur search: "+ id);
            } while (foodCursor.moveToNext());
        }
		foodCursor.close();
		return id;		
	}
	
	public void combineKeywords(String[] keywords, int window_size){		
		for(int window_current_size = window_size; window_current_size > 0; window_current_size--){
			Log.i("LocalDB","window_current_size: "+window_current_size);
			for(int pointer = 0; pointer < keywords.length; ){
				String tempCombination = "";
				int tempComboLength = pointer + Math.min(window_current_size, keywords.length-pointer-1);
				for(int index = pointer; index < tempComboLength; index++){					
					if (index != tempComboLength -1){
						tempCombination += keywords[index]+ " ";
					}else{
						tempCombination += keywords[index];
					}									
				}
				Log.i("LocalDB","tempCombo: "+tempCombination);
				int tempId = this.searchByName(tempCombination);
				if(tempId != 0){
					keywordsCombo.add(tempCombination);	
				}	
				//pointer = pointer + Math.min(window_current_size, keywords.length-pointer);
				pointer++;
			}
		}
		
	}	
	
}
