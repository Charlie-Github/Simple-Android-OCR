package com.edible.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.edible.entity.Image;
import com.edible.entity.User;
import com.edible.other.Status;
import com.edible.service.ImageService;
import com.google.gson.JsonSyntaxException;

public class ImageServiceImpl extends BasicServiceImpl implements ImageService {

	private static final String IMAGE_URL = "/image";
	@Override
	public Image uploadImage(Long fid, String name, String message, User user) throws Exception {
		// TODO Auto-generated method stub
		String url = BasicServiceImpl.HOST + IMAGE_URL + "/upload";
		Image image = new Image();
		image.setName(name);
		image.setMessage(message);
		image.setUploader(user);
		Map<String, String> params = new HashMap<String, String> ();
		params.put("fid", fid + "");
		params.put("image", gson.toJson(image));
		JSONObject response = doPost(url, params);
		int statusCode = response.getInt("status_code");
		String statusMsg = response.getString("status_msg");
		if(statusCode == Status.SUCCESS.getStatusCode()) {
			return gson.fromJson(response.getJSONObject("result").toString(), Image.class);
		} else {
			throw new Exception(statusMsg);
		}
	}
}
