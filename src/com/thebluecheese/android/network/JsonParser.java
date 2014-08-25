package com.thebluecheese.android.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thebluecheese.android.basic.Food;
import com.thebluecheese.android.basic.FoodPhoto;

public class JsonParser {
	
	public JsonParser(){
		
	}
	
	public Food parseFood(String jsonStr){
		Food localFood = new Food();
		if (jsonStr != null) {
			
            try {
                JSONObject jsonObj = new JSONObject(jsonStr); 
                JSONArray foods = jsonObj.getJSONArray("result");                
                
                // looping through "result"
                for (int i = 0; i < foods.length(); i++) {
                    JSONObject food = foods.getJSONObject(i);                    
                    
                    localFood._fid = food.getInt("fid");
                    localFood._title = food.getString("title");
                    localFood._name = food.getString("name");                     
                    localFood._tags = food.getString("tags");
                    localFood._lang = food.getString("lang");
                    localFood._description = food.getString("description");
                    localFood._avg_rate = food.getInt("avg_rate");                    
                    localFood._food_creater = food.getInt("food_creater");                   
                    
                    JSONArray foodphotos = food.getJSONArray("photos");
                    
                    for(int j = 0; j < foodphotos.length(); j++){
                    	JSONObject foodphoto = foodphotos.getJSONObject(j);
                    	FoodPhoto localFoodphoto = new FoodPhoto();
                    	localFoodphoto._photo_creater = foodphoto.getInt("photo_creater");
                    	localFoodphoto._pid = foodphoto.getInt("pid");
                    	localFoodphoto._url = foodphoto.getString("url");
                    	localFood._photos.add(localFoodphoto);
                    }
                   
                }
            } catch (JSONException e) {
                Log.e("SimpleOCR","JsonException: "+e.getMessage());
            }
        }
		Log.v("SimpleOCR", "JsonParser: "+ localFood._name);
		return localFood;
	}

}
