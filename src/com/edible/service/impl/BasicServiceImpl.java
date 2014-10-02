package com.edible.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.edible.other.MyResponseHandler;
import com.edible.other.DateAdapter;
import com.edible.service.BasicService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BasicServiceImpl implements BasicService {

	public static final String HOST = "http://ediblebluecheese.elasticbeanstalk.com";
	protected MyResponseHandler responseHandler;
	protected Gson gson;
	
	public BasicServiceImpl() {
		this.responseHandler = new MyResponseHandler();
		gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateAdapter()).create();
	}
	public JSONObject doGet(String url, Map<String, String> params) throws IOException, URISyntaxException, JSONException {
		URIBuilder uriBuilder = new URIBuilder(url);
		CloseableHttpClient httpclient =  HttpClients.createDefault();
		
	    try {
	    	Iterator<String> it = params.keySet().iterator();
	    	while(it.hasNext()) {
	    		String key = it.next();
	    		String value = (String) params.get(key);
	    		uriBuilder.setParameter(key, value);
	    	}
	    	URI uri = uriBuilder.build();
	    	HttpGet httpget = new HttpGet(uri);
	        return new JSONObject(httpclient.execute(httpget, responseHandler));
	    } finally {
	        httpclient.close();
	    }
	}
	
	public JSONObject doPost(String url, Map<String, String> params) throws IOException, URISyntaxException, JSONException {
		URIBuilder uriBuilder = new URIBuilder(url);
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		@SuppressWarnings({ "deprecation", "resource" })
		HttpClient httpclient = new DefaultHttpClient();
		try {
			URI uri = uriBuilder.build();
			HttpPost httppost = new HttpPost(uri);
			String entity = gson.toJson(params);
			System.out.println(entity);
			httppost.setHeader("Content-Type", "application/json;charset=utf-8");
			httppost.setEntity(new StringEntity(entity, "UTF-8"));
			return new JSONObject(httpclient.execute(httppost, responseHandler));
		}
		finally {
			//httpclient.close();
		}
	}
}
