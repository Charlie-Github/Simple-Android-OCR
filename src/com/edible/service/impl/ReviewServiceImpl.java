package com.edible.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.edible.entity.Review;
import com.edible.entity.User;
import com.edible.other.Status;
import com.edible.service.ReviewService;
import com.google.gson.JsonSyntaxException;

public class ReviewServiceImpl extends BasicServiceImpl implements ReviewService {

	public static final String REVIEW_URL = "/review";
	@Override
	public Review writeReview(Long fid, String comment, int rate, User user) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + REVIEW_URL + "/write";
		Review review = new Review();
		review.setComment(comment);
		review.setRate(rate);
		review.setReviewer(user);
		Map<String, String> params = new HashMap<String, String> ();
		params.put("fid", fid + "");
		params.put("review", gson.toJson(review));
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode == Status.SUCCESS.getStatusCode()) {
			return gson.fromJson(response.getJSONObject("result").toString(), Review.class);
		} else {
			throw new Exception(statusMsg);
		}
	}
	@Override
	public void likeReview(Long rid) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + REVIEW_URL + "/like";
		Map<String, String> params = new HashMap<String, String> ();
		params.put("rid", rid + "");
		JSONObject response = doGet(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode != Status.SUCCESS.getStatusCode()) {
			throw new Exception(statusMsg);
		}
	}
	
}
