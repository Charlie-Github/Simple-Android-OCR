package com.edible.service.impl;

import com.edible.entity.Restaurant;
import com.edible.other.Status;
import com.edible.service.RestaurantService;
import com.edible.service.UserService;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantServiceImpl extends BasicServiceImpl implements RestaurantService {

	public static final String RESTAURANT_URL = "/restaurant";
	
	@Override
	public List<Restaurant> searchRestaurantsByName(String name) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + RESTAURANT_URL + "/search/name";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("name", name);
		JSONObject response = doGet(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode == Status.SUCCESS.getStatusCode()) {
			return gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<List<Restaurant>>() {}.getType());
		} else {
			throw new Exception(statusMsg);
		}
	}

    @Override
    public List<Restaurant> searchRestaurantsByType(String type) throws Exception {
        String url = BasicServiceImpl.HOST + RESTAURANT_URL + "/search/type";
        Map<String, String> params = new HashMap<String, String> ();
        params.put("type", type);
        JSONObject response = doGet(url, params);
        int statusCode = response.getInt("status_code");
	    String statusMsg = response.getString("status_msg");
        if(statusCode == Status.SUCCESS.getStatusCode()) {
	        return gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<List<Restaurant>>() {}.getType());
        } else {
            throw new Exception(statusMsg);
        }
    }

    @Override
    public List<Restaurant> searchRestaurantsByCoordinates(Double latitude, Double longitude) throws Exception {
        String url = BasicServiceImpl.HOST + RESTAURANT_URL + "/search/coordinates";
        Map<String, String> params = new HashMap<String, String> ();
        params.put("latitude", latitude + "");
        params.put("longitude", longitude + "");
        JSONObject response = doGet(url, params);
        int statusCode = response.getInt("status_code");
	    String statusMsg = response.getString("status_msg");
        if(statusCode == Status.SUCCESS.getStatusCode()) {
	        return gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<List<Restaurant>>() {}.getType());
        } else {
            throw new Exception(statusMsg);
        }
    }





}
