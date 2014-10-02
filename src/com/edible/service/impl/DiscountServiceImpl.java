package com.edible.service.impl;

import com.edible.entity.Discount;
import com.edible.other.Status;
import com.edible.service.DiscountService;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountServiceImpl extends BasicServiceImpl implements DiscountService {

	public static final String DISCOUNT_URL = "/discount";

    @Override
    public List<Discount> getRecentDiscounts() throws Exception {
        String url = BasicServiceImpl.HOST + DISCOUNT_URL + "/recent";
        Map<String, String> params = new HashMap<String, String> ();
        JSONObject response = doGet(url, params);
        int statusCode = response.getInt("status_code");
	    String statusMsg = response.getString("status_msg");
        if(statusCode == Status.SUCCESS.getStatusCode()) {
            return gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<List<Discount>>() {}.getType());
        } else {
            throw new Exception(statusMsg);
        }
    }

    @Override
    public List<Discount> getDiscountsByRestaurant(Long restaurantId) throws Exception {
        String url = BasicServiceImpl.HOST + DISCOUNT_URL + "/restaurant";
        Map<String, String> params = new HashMap<String, String> ();
        params.put("restaurantId",restaurantId + "");
        JSONObject response = doGet(url, params);
        int statusCode = response.getInt("status_code");
	    String statusMsg = response.getString("status_msg");
        if(statusCode == Status.SUCCESS.getStatusCode()) {
	        return gson.fromJson(response.getJSONArray("result").toString(), new TypeToken<List<Discount>>() {}.getType());
        } else {
            throw new Exception(statusMsg);
        }
    }
}
