package com.thebluecheese.android.activity;



import com.thebluecheese.android.activity.FoodDetailActivityAsyncTask.lessClickHandler;
import com.thebluecheese.android.activity.FoodDetailActivityAsyncTask.moreClickHandler;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {
	
	


	String TAG = "BlueCheese";
	Button loginButton;
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
		loginButton.setOnClickListener(new loginClickHandler());
		
		//tamp to login auto
		SharedPreferences sharedPre = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		String email = sharedPre.getString("email", "");
		String password = sharedPre.getString("pwd", "");
		
		LoginActivityAsyncTask lhelper = new LoginActivityAsyncTask(email,password,context);
		lhelper.execute();
		
	}
	
	public class loginClickHandler implements View.OnClickListener {
		public void onClick(View view) {			
			LoginActivityAsyncTask lhelper = new LoginActivityAsyncTask(emailText.getText().toString(),pwdText.getText().toString(),context);
			lhelper.execute();
		}
	}
	
	
}
