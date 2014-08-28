package com.thebluecheese.android.basic;

import java.util.ArrayList;

public class Food {
	public int _fid;
	public String _tags;
	public String _title;
	public String _name;
	public int _avg_rate;
	public int _food_creater;
	public String _description;
	public ArrayList<FoodPhoto> _photos;
	public String _lang;
	

	public Food(){	
		_tags = "";
		_title = "";
		_name = "";
		_description = "";
		_photos = new ArrayList<FoodPhoto>();
		_lang= "";
	}
	
	
}
