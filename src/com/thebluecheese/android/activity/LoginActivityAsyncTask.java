package com.thebluecheese.android.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class LoginActivityAsyncTask extends AsyncTask<String, Integer, String> {
	String TAG = "BlueCheese";
	String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	String responsText;
	String email = "gcte2010@gmail.com";
	String pwd = "engineer";
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		pwd = md5(pwd);
		postData();
		return null;
	}

	public void postData() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(userServerAddress);
	    JSONObject json = new JSONObject();
	    
	    try {
	        // Add your data
	       
	        json.put("email", email);
	        json.put("pwd", pwd);
	        json.put("action", "login");
	        StringEntity se = new StringEntity( json.toString());
	        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        httppost.setEntity(se);

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	        // 判断是够请求成功
	        if (response.getStatusLine().getStatusCode() == 200) {
	        	// 获取返回的数据
	        	responsText = EntityUtils.toString(response.getEntity(), "UTF-8");
	        } else {
	        	Log.i("HttpPost", "HttpPost方式请求失败");
	        }
	       
	        Log.i(TAG, "User login async : " +  responsText);	
	        
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	    } 
	} 
	
	public static String md5(String string) {
	    byte[] hash;

	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);

	    for (byte b : hash) {
	        int i = (b & 0xFF);
	        if (i < 0x10) hex.append('0');
	        hex.append(Integer.toHexString(i));
	    }

	    return hex.toString();
	}
	
}
