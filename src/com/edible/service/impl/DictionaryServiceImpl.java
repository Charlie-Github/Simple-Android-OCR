package com.edible.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.edible.entity.Dictionary;
import com.edible.other.Status;
import com.edible.service.DictionaryService;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class DictionaryServiceImpl extends BasicServiceImpl implements DictionaryService {

	public static final String DICT_URL = "/dict";
	
	@Override
	public List<String> loopUpDictionary(String query, String lang) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + DICT_URL + "/lookup";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("query", query);
		params.put("lang", lang);
		JSONObject response = doGet(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode == Status.SUCCESS.getStatusCode()) {
			return gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<List<String>>() {}.getType());
		} else {
			throw new Exception(statusMsg);
		}
	}

	@Override
	public Dictionary retrieveDictionary(String title, String lang) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + DICT_URL + "/retrieve";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("title", title);
		params.put("lang", lang);
		JSONObject response = doGet(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode == Status.SUCCESS.getStatusCode()) {
			return gson.fromJson(response.getJSONObject("result").toString(), Dictionary.class);
		} else {
			throw new Exception(statusMsg);
		}
	}

}
