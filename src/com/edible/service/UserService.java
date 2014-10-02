package com.edible.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;


public interface UserService extends BasicService {

	public void updateUserName(Long uid, String name) throws Exception;
	public void updateUserAge(Long uid, int age) throws Exception;
	public void updateUserRegion(Long uid, String region) throws Exception;
	public void updateUserFlavor(Long uid, String flavor) throws Exception;
    public void updateUserGender(Long uid, String gender) throws Exception;
    public void updateUserPortrait(Long uid, String portrait) throws Exception;

}
