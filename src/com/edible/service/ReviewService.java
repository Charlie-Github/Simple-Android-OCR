package com.edible.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

import com.edible.entity.Review;
import com.edible.entity.User;
import com.google.gson.JsonSyntaxException;

public interface ReviewService extends BasicService {

	public Review writeReview(Long fid, String comment, int rate, User user) throws Exception;
	public void likeReview(Long rid) throws Exception;
}
