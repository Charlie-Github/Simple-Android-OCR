package com.edible.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public interface BasicService {

	public JSONObject doGet(String url, Map<String, String> params) throws IOException, URISyntaxException, JSONException;
	public JSONObject doPost(String url, Map<String, String> params) throws IOException, URISyntaxException, JSONException;
}
