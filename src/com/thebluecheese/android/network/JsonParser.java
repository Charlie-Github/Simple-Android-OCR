package com.thebluecheese.android.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thebluecheese.android.basic.Food;
import com.thebluecheese.android.basic.FoodPhoto;
import com.thebluecheese.android.basic.FoodReview;
import com.thebluecheese.android.basic.User;

public class JsonParser {
	String TAG = "BlueCheese";
	
	public JsonParser(){
		
	}
	
	public Food parseFood(String jsonStr){
		Food localFood = new Food();
		if (jsonStr != null) {
			
            try {
                JSONObject jsonObj = new JSONObject(jsonStr); 
                String resultStr = jsonObj.getString("result");                
                if(!resultStr.equals("")){
                
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
	                    localFood._avg_rate = food.getDouble("avg_rate");                    
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
                }
            } catch (JSONException e) {
                Log.e(TAG,"Exception on Json parser: "+e.getMessage());
            }
        }
		return localFood;
	}
	
	public ArrayList<FoodReview> parseReview(String jsonStr){
		ArrayList<FoodReview> foodReviews = new ArrayList<FoodReview>();
		
		if (jsonStr != null) {
			
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String resultStr = jsonObj.getString("result");                
                if(!resultStr.equals("")){                
	                JSONArray results = jsonObj.getJSONArray("result");                
	                
	                if(results.length() > 0){
		                // looping through "result"
		                for (int i = 0; i < results.length(); i++) {
		                	
		                	FoodReview tempReview = new FoodReview();
		                    JSONObject review = results.getJSONObject(i);
		                    tempReview._fid = review.getInt("fid");
		                    tempReview._comments = review.getString("comments");                     
		                    JSONObject review_creater = review.getJSONObject("review_creater");                   	
		                    tempReview. _review_creater= review_creater.getString("name"); 
		                    
		                    foodReviews.add(tempReview);             
		                }
	                }
                }
            } catch (JSONException e) {
                Log.e(TAG,"Exception on Json parser review: "+e.getMessage());
            }
        }
		
		return foodReviews;
	}
	
	public User parseUser(String jsonStr){
		/*
		{
			"result":
				{
					"selfie":"default_selfie.png",
					"uid":45,
					"last_login_time":1411574612000,
					"email":"gcte2010@gmail.com",
					"privilege":1,
					"name":"Charlie is long"
				},
			"status":true,
			"action":"login",
			"log":"login succeed"
		}
		*/
		
		User user = new User();
		
		if (jsonStr != null) {
			
        	try {
        		
            	JSONObject jsonObj = new JSONObject(jsonStr);
            	String status = jsonObj.getString("log");
            	String resultStr = jsonObj.getString("result");                
                if(!resultStr.equals("")){
                	user._log = status;
	                JSONObject result = jsonObj.getJSONObject("result");
	                user._selfie = result.getString("selfie");
	                user._uid = result.getInt("uid");
	                user._name = result.getString("name");
	                user._email = result.getString("email");
                }
            
            } catch (JSONException e) {
                Log.i(TAG,"Exception on Json parser user: " + e.getMessage());
            }
        }
		return user;
		
	}

}
