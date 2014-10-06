package com.thebluecheese.android.activity;

import com.edible.entity.Account;
import com.edible.service.AccountService;
import com.edible.service.DictionaryService;
import com.edible.service.UserService;
import com.edible.service.impl.AccountServiceImpl;
import com.edible.service.impl.DictionaryServiceImpl;
import com.edible.service.impl.UserServiceImpl;
import com.thebluecheese.android.basic.User;
import com.thebluecheese.android.network.LoginHelper;
import com.thebluecheese.android.network.RegisterHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class LoginThirdpartyAsyncTask extends AsyncTask<String, Integer, String> {
	ProgressDialog _progressDialog;
	Context _context;
	String TAG = "BlueCheese";	
	String userServerAddress = "http://default-environment-9hfbefpjmu.elasticbeanstalk.com/user";
	
	Account currentAccount;
	AccountService accountService;
	UserService userService;
	DictionaryService dictionaryService;
	
	
	
	public LoginThirdpartyAsyncTask(ProgressDialog pd,Context ct){
		_progressDialog = pd;
		_context = ct;
		
		accountService = new AccountServiceImpl();
		userService = new UserServiceImpl();
		dictionaryService = new DictionaryServiceImpl();
		
	}

	@Override
	protected String doInBackground(String... params) {	
		
		//if(tryLogin()||login()){
		if(login()){
			Intent intent = new Intent(_context, CameraResultActivity.class);
			_context.startActivity(intent);
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {	
		
		String loadingmessage = _context.getResources().getString(R.string.logining);		
		_progressDialog.setMessage(loadingmessage);
		_progressDialog.show();	

    }

	
	public boolean login(){
		//register third party user and login
		User tempUser = new User();
		boolean loginState = false;
		SharedPreferences sharedPre = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		String email = sharedPre.getString("email", "");
		String password = sharedPre.getString("pwd", "");
		String userName = sharedPre.getString("name","");
		int age = Integer.parseInt(sharedPre.getString("age",""));
		String gender = sharedPre.getString("gender","");
		String region = "China";
		String type = "facebook";
		String portrait = sharedPre.getString("selfie","");
		
		//test server 2.0
		loginState = loginV2(email, userName, age, gender, region, type, portrait);
		/*
		//post register request
		RegisterHelper rh = new RegisterHelper(email,password,userName);
		Thread postThread = new Thread(rh);
		postThread.start();			
		try {
			//waiting for get response
			postThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Exception" + e);			
		}		
		tempUser = rh.getUser();		
		
		//check login status
		if(tempUser._log.equals("register user succeed")){
						
			loginState = true;
			
		}else{
			//login failed
			loginState = false;
			
		}
		*/
		Log.i(TAG, "third party user signup login state: "+ loginState);
		return loginState;
	}
	
	public boolean tryLogin(){
		
		//try to login server using third party user info seeking existing user
		boolean loginState = false;
		User tempUser = new User();
				
		SharedPreferences sharedPre = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		String email = sharedPre.getString("email", "");
		String password = sharedPre.getString("pwd", "");

		//Post login request		
		LoginHelper lh = new LoginHelper(email , password);
		Thread postThread = new Thread(lh);
		postThread.start();			
		try {
			//waiting for get response
			postThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Exception" + e);			
		}		
		tempUser = lh.getUser();
		
		if(tempUser._log.equals("login succeed")){
			//user already loged in once
			loginState = true;
		}else{
			loginState = false;
		}
		Log.i(TAG, "third party user try login state: "+ loginState);
		return loginState;
		
	}
	
	public boolean loginV2(String id,String name,int age,String gender,String region,String type,String portrait){
		boolean loginState = false;
		try {
			/**
			 * 成功案例:登陆成功，并返回账号信息
			 * 失败案例:账户或密码错误,返回提示信息
			 * 错误案例:提示出错信息
			 */
//			String qq = "175191269";
//			String name = "追影子的风";
//			int age = 25;
//			String gender = "male";
//			String region = "China";
//			String type = "qq";
//			String portrait = "portrait_qq.jpg";
			Account account = accountService.signInByFacebook(id, name, age, gender, region, portrait);
			Log.i(TAG,"注册成功，欢迎进入蓝芝士的世界");
			currentAccount = account;
			loginState = true;
			Log.i(TAG,"current account"+currentAccount);
		} catch(Exception e) {
			Log.i(TAG,"loginV2 : "+e);
		}
		return loginState;
	}
}
