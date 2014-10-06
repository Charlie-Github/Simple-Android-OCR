package com.edible.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.edible.entity.Account;
import com.edible.other.BasicException;
import com.edible.other.Status;
import com.edible.service.AccountService;
import com.google.gson.JsonSyntaxException;

public class AccountServiceImpl extends BasicServiceImpl implements AccountService {

	public static final String ACCOUNT_URL = "/account";
	@Override
	public Account signUp(String email, String name, String password) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + ACCOUNT_URL + "/signup";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("email", email);
		params.put("name", name);
		params.put("password", password);
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode == Status.SUCCESS.getStatusCode()) {
			return gson.fromJson(response.getJSONObject("result").toString(), Account.class);
		} else {
			throw new BasicException(statusCode, statusMsg);
			
		}
	}

	@Override
	public Account signIn(String email, String password) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + ACCOUNT_URL + "/signin";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("email", email);
		params.put("password", password);
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode == Status.SUCCESS.getStatusCode()) {
			return gson.fromJson(response.getJSONObject("result").toString(), Account.class);
		} else {
			throw new BasicException(statusCode, statusMsg);
		}
		
	}

	@Override
	public void updatePassword(Long uid, String oldpwd, String newpwd) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + ACCOUNT_URL + "/password/update";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("uid", uid + "");
		params.put("oldpwd", oldpwd);
		params.put("newpwd", newpwd);
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode != Status.SUCCESS.getStatusCode()) {
			throw new Exception(statusMsg);
		}
	}

	@Override
	public void updatePassword(String email, String oldpwd, String newpwd)
			throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + ACCOUNT_URL + "/password/update";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("email", email);
		params.put("oldpwd", oldpwd);
		params.put("newpwd", newpwd);
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode != Status.SUCCESS.getStatusCode()) {
			throw new Exception(statusMsg);
		}
	}

	@Override
	public Account signInThirdParty(String type, String id, String name, int age, String gender, String region, String portrait) throws Exception {
		String url = BasicServiceImpl.HOST + ACCOUNT_URL + "/signin/" + type;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("name", name);
		params.put("age", age + "");
		params.put("gender", gender);
		params.put("region", region);
		params.put("portrait", portrait);
		params.put("timestamp", new Date().getTime() + "");
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode == Status.SUCCESS.getStatusCode()) {
			return gson.fromJson(response.getJSONObject("result").toString(), Account.class);
		} else {
			throw new Exception(statusMsg);
		}
	}

	@Override
	public Account signInByQQ(String qq, String name, int age, String gender, String region, String portrait) throws Exception {
		return signInThirdParty("qq", qq, name, age, gender, region, portrait);
	}

	@Override
	public Account signInByFacebook(String facebookId, String name, int age, String gender, String region, String portrait) throws Exception {
		return signInThirdParty("facebook", facebookId, name, age, gender, region, portrait);
	}


}
