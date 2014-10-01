package com.thebluecheese.android.network;

import java.util.HashMap;

import com.thebluecheese.android.basic.User;

public class thirdPartyUserParser{

	public static User parseFacebook(HashMap<String, Object> res){
		User user = new User();		
		user._name = (String) res.get("name");
		user._gender = (String) res.get("gender");
		
		HashMap<String, Object> picture =(HashMap<String,Object>)res.get("picture");
		HashMap<String, Object> data =(HashMap<String,Object>)picture.get("data");
		user._selfie = (String) data.get("url");
		
		HashMap<String, Object> age_range =(HashMap<String,Object>)res.get("age_range");	
		user._age = age_range.get("min").toString();
		
		return user;
	}
}
