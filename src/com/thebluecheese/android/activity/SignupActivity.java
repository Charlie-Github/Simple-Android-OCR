package com.thebluecheese.android.activity;

import cn.sharesdk.framework.utils.UIHandler;

import com.thebluecheese.android.activity.LoginActivity.signupClickHandler;
import com.thebluecheese.android.basic.User;
import com.thebluecheese.android.network.RegisterHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity{
	String TAG = "BlueCheese";
	Button signUpButton;
	Context context;
	EditText emailText;
	EditText pwdText;
	EditText userNameText;
	ProgressDialog progressDialog;
	
	User user;
	
	private static final int MSG_REG_SUCCESS = 1;
	private static final int MSG_REG_FAIL = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		context = this;
		
		emailText = (EditText)findViewById(R.id.textEmailsignup);
		pwdText = (EditText)findViewById(R.id.textPwdsignup);
		userNameText = (EditText)findViewById(R.id.textNamesignup);
		
		signUpButton = (Button)findViewById(R.id.submitButton);
		signUpButton.setOnClickListener(new signupClickHandler());
		
		
		user = new User();

	}
	
	public class signupClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			
			SignupActivityAsyncTask lhelper = new SignupActivityAsyncTask(emailText,pwdText,userNameText,progressDialog,context);
			lhelper.execute();
			
			
		}
	}

	


	
	
}
