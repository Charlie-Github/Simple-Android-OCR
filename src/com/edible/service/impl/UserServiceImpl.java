package com.edible.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.edible.other.Status;
import com.edible.service.UserService;

public class UserServiceImpl extends BasicServiceImpl implements UserService {

	public static final String USER_URL = "/user";
	
	@Override
	public void updateUserName(Long uid, String name) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + USER_URL + "/update";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("uid", uid + "");
		params.put("key", "name");
		params.put("value", name);
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode != Status.SUCCESS.getStatusCode()) {
			throw new Exception(statusMsg);
		}
	}

	@Override
	public void updateUserAge(Long uid, int age) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + USER_URL + "/update";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("uid", uid + "");
		params.put("key", "age");
		params.put("value", age + "");
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode != Status.SUCCESS.getStatusCode()) {
			throw new Exception(statusMsg);
		}
	}

	@Override
	public void updateUserRegion(Long uid, String region) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + USER_URL + "/update";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("uid", uid + "");
		params.put("key", "region");
		params.put("value", region);
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode != Status.SUCCESS.getStatusCode()) {
			throw new Exception(statusMsg);
		}
	}

	@Override
	public void updateUserFlavor(Long uid, String flavor) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + USER_URL + "/update";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("uid", uid + "");
		params.put("key", "flavor");
		params.put("value", flavor);
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode != Status.SUCCESS.getStatusCode()) {
			throw new Exception(statusMsg);
		}
	}

    @Override
    public void updateUserGender(Long uid, String gender) throws Exception {
        String url = BasicServiceImpl.HOST + USER_URL + "/update";
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid + "");
        params.put("key", "gender");
        params.put("value", gender);
        JSONObject response = doPost(url, params);
        int statusCode = response.getInt("status_code");
	    String statusMsg = response.getString("status_msg");
        if (statusCode != Status.SUCCESS.getStatusCode()) {
	        throw new Exception(statusMsg);
        }
    }

	@Override
	public void updateUserPortrait(Long uid, String portrait) throws Exception {
		String url = BasicServiceImpl.HOST + USER_URL + "/update";
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", uid + "");
		params.put("key", "portrait");
		params.put("value", portrait);
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if (statusCode != Status.SUCCESS.getStatusCode()) {
			throw new Exception(statusMsg);
		}
	}

}
