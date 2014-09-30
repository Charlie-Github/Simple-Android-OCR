package com.thebluecheese.android.network;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thebluecheese.android.basic.User;

public class RegisterHelper  implements Runnable{
	static String TAG = "BlueCheese";
	static String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	static String responsText;
	String _email;
	String _pwd;
	String _name;
	User user;
	
	public RegisterHelper(String loginemail, String loginpwd, String name){
		_email = loginemail;
		_pwd = loginpwd;
		_name = name;
	}
	
	public User getUser(){
		return user;
	}
	
	@Override
	public void run() {
		user = registerServer(_email,_pwd,_name);		
	}
	
	public User registerServer(String loginemail, String loginpwd, String name) {
		User tempUser = new User();
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(userServerAddress);
	    JSONObject json = new JSONObject();		    
	    
	    try {
	        // Add your data	       
	        json.put("email", loginemail);
	        json.put("pwd", loginpwd);
	        json.put("name", name);
	        json.put("action", "register");
	        StringEntity se = new StringEntity(json.toString());
	        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        httppost.setEntity(se);
	        	
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	        Log.v(TAG, "response code: "+ response.getStatusLine().getStatusCode());
	        
	        if (response.getStatusLine().getStatusCode() == 200) {
	        	// 获取返回的数据
	        	responsText = EntityUtils.toString(response.getEntity(), "UTF-8");
	        	Log.v(TAG, "register response: "+ responsText);
	        } else {
	        	Log.i(TAG, "HttpPost方式请求失败");	        	
	        }
	        
	        
	    } catch (Exception e) {
	        Log.e(TAG, "exception on post: "+ e);
	        
	    } 
	    
        JsonParser jp = new JsonParser();
        tempUser = jp.parseUser(responsText);
        
        return tempUser;
	} 

}
