package com.thebluecheese.android.network;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;

import android.util.Log;

import com.edible.entity.Account;
import com.edible.service.AccountService;
import com.edible.service.DictionaryService;
import com.edible.service.DiscountService;
import com.edible.service.FoodService;
import com.edible.service.ImageService;
import com.edible.service.RestaurantService;
import com.edible.service.ReviewService;
import com.edible.service.UserService;
import com.edible.service.impl.AccountServiceImpl;
import com.edible.service.impl.DictionaryServiceImpl;
import com.edible.service.impl.FoodServiceImpl;
import com.edible.service.impl.ImageServiceImpl;
import com.edible.service.impl.ReviewServiceImpl;
import com.edible.service.impl.UserServiceImpl;
import com.thebluecheese.android.basic.User;

public class LoginHelper  implements Runnable{
	String TAG = "BlueCheese";
	String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	String responsText;
	String _email;
	String _pwd;
	User user;
	
	Account currentAccount;
	AccountService accountService;
	UserService userService;
	DictionaryService dictionaryService;
	FoodService foodService;
	ReviewService reviewService;
	ImageService imageService;
    RestaurantService restaurantService;
    DiscountService discountService;
	
	public LoginHelper(String loginemail, String loginpwd){
		_email = loginemail;
		_pwd = loginpwd;
		user = new User();
		//-------------------------
				
			    
			    accountService = new AccountServiceImpl();
				userService = new UserServiceImpl();
				dictionaryService = new DictionaryServiceImpl();
				foodService = new FoodServiceImpl();
				reviewService = new ReviewServiceImpl();
				imageService = new ImageServiceImpl();
		
	}
	
	public User getUser(){
		return user;
	}
	
	@Override
	public void run() {
		//user = loginServer(_email,_pwd);
		loginV2("bluecheese@edibleinnovationsllc.com","edible");
	}
	
	public User loginServer(String loginemail, String loginpwd) {
		User tempUser = new User();
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(userServerAddress);
	    JSONObject json = new JSONObject();		    
	    
	    try {
	        // Add your data	       
	        json.put("email", loginemail);
	        json.put("pwd", loginpwd);
	        json.put("action", "login");
	        StringEntity se = new StringEntity(json.toString());
	        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        httppost.setEntity(se);
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	        Log.v(TAG, "response code: "+ response.getStatusLine().getStatusCode());
	        
	        if (response.getStatusLine().getStatusCode() == 200) {
	        	// 获取返回的数据
	        	responsText = EntityUtils.toString(response.getEntity(), "UTF-8");
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
	
	public void loginV2(String email,String password){

	    
		
		try {
			/**
			 * 成功案例:登陆成功，并返回账号信息
			 * 失败案例:账户或密码错误,返回提示信息
			 * 错误案例:提示出错信息
			 */
			//从用户界面获取用户输入的邮件和密码
			//String email = "bluecheese@edibleinnovationsllc.com";
			//String password = "edible";
			Log.i(TAG,"V2 login: "+email+","+password);
			Account account = accountService.signIn(email, password);
			
			if(account == null) {
				Log.i(TAG,"账号或密码错误，请重试");
			} else {
				Log.i(TAG,"登陆成功，欢迎进入蓝芝士的世界");
				//currentAccount = account;
				
			}
		} catch(Exception e) {
			Log.i(TAG,"系统错误，请稍后再试"+e);
			//e.printStackTrace();
		}
	}

}
