package com.datumdroid.android.ocr.simple;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class LocalDbOperator {
	private ExternalDbOpenHelper dbOpenHelper;
	private SQLiteDatabase database;
	private TextParser textparser;
	
	public LocalDbOperator(Context context){
		dbOpenHelper = new ExternalDbOpenHelper(context,"localDB.db");
		database = dbOpenHelper.openDataBase();		
	}
	
	public String[] search(String fromTess){
		
		String[] translates = TextParser.spliteAll(fromTess);
		
		ArrayList<String> translated = new ArrayList<String>();
		for(int i = 0; i < translates.length; i++){			
			int fid = this.searchByName(translates[i]);
			String chinese_name = this.searchById(fid);
			if(chinese_name != ""){
				translated.add(chinese_name);
				Log.i("LocalDB","Translate: "+chinese_name);
			}
		}
		
		String[] array = translated.toArray(new String[translated.size()]);
		return array;
	}
	
	public String searchById(int id){
		//search Chinese name by id
		//Local DB test		
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
		//search food _id name by name
		name = name.toUpperCase();
		Cursor foodCursor = database.query("Keyword",new String[]{"_id"}, "upper(title) = \'"+name+"\'",null, null, null, null);
		foodCursor.moveToFirst();
		int id = 0;
		if(!foodCursor.isAfterLast()) {
            do {
                id = foodCursor.getInt(0);
                Log.i("LocalDB", "_id: "+ id);
            } while (foodCursor.moveToNext());
        }
		foodCursor.close();
		return id;		
	}
}
