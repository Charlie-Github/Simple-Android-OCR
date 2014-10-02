package com.edible.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

import com.edible.entity.Account;
import com.google.gson.JsonSyntaxException;

public interface AccountService extends BasicService{

	public Account signUp(String email, String name, String password) throws Exception;
	public Account signIn(String email, String password) throws Exception;
	public void updatePassword(Long uid, String oldpwd, String newpwd) throws Exception;
	public void updatePassword(String email, String oldpwd, String newpwd) throws Exception;
	public Account signInThirdParty(String type, String id, String name, int age, String gender, String region, String portrait) throws Exception;
	public Account signInByQQ(String qq, String name, int age, String gender, String region, String portrait) throws Exception;
	public Account signInByFacebook(String facebookId, String name, int age, String gender, String region, String portrait) throws Exception;

}
