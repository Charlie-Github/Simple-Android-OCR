package com.datumdroid.android.ocr.simple;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class LocalDbOperator {
	private ExternalDbOpenHelper dbOpenHelper;
	private SQLiteDatabase database;
	
	public LocalDbOperator(Context context){
		dbOpenHelper = new ExternalDbOpenHelper(context,"localDB.db");
		database = dbOpenHelper.openDataBase();		
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
