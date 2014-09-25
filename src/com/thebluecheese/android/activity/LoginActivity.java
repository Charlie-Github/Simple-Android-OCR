package com.thebluecheese.android.activity;



import com.thebluecheese.android.activity.FoodDetailActivityAsyncTask.lessClickHandler;
import com.thebluecheese.android.activity.FoodDetailActivityAsyncTask.moreClickHandler;
import com.thebluecheese.android.activity.LoginActivityAsyncTask.loginClickHandler;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {
	
	


	String TAG = "BlueCheese";
	Button loginButton;
	Button signUpButton;
	EditText emailText;
	EditText pwdText;
	Context context;
	String result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		context = this;
		emailText = (EditText)findViewById(R.id.textEmail);
		pwdText = (EditText)findViewById(R.id.textPwd);
		
		loginButton = (Button)findViewById(R.id.loginButton);
		signUpButton = (Button)findViewById(R.id.signUpButton);
		signUpButton.setOnClickListener(new signupClickHandler());
		
		LoginActivityAsyncTask lhelper = new LoginActivityAsyncTask(emailText,pwdText,loginButton,context);
		lhelper.execute();
		
	}
	
	public class signupClickHandler implements View.OnClickListener {
		public void onClick(View view) {			
			SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString("email", "");
			editor.putString("pwd", "");
			editor.putString("name", "");
			editor.putString("uid", "");
			editor.putString("loginState", "false");
			editor.commit();
			
		}
	}

	
	
}
