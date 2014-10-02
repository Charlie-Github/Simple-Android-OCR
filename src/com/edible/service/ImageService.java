package com.edible.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

import com.edible.entity.Image;
import com.edible.entity.User;
import com.google.gson.JsonSyntaxException;

public interface ImageService extends BasicService {

	public Image uploadImage(Long fid, String name, String message, User user) throws Exception;
}
