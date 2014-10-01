package com.thebluecheese.android.network;

import java.util.HashMap;

import android.util.Log;

import com.thebluecheese.android.basic.User;

public class thirdPartyUserParser{
	static String TAG = "BlueCheese";

	public static User parseFacebook(HashMap<String, Object> res){
		User user = new User();	
		user._name = (String) res.get("name");
		user._gender = (String) res.get("gender");
		user._email = res.get("id").toString()+"@thirdparty.com";
		user._pwd = user._email;
		HashMap<String, Object> picture =(HashMap<String,Object>)res.get("picture");
		HashMap<String, Object> data =(HashMap<String,Object>)picture.get("data");
		user._selfie = (String) data.get("url");
		
		HashMap<String, Object> age_range =(HashMap<String,Object>)res.get("age_range");	
		user._age = age_range.get("min").toString();
		
		return user;
	}
}
