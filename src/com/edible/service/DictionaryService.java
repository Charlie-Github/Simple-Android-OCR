package com.edible.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONException;

import com.edible.entity.Dictionary;
import com.google.gson.JsonSyntaxException;

public interface DictionaryService extends BasicService {

	public List<String> loopUpDictionary(String query, String lang) throws Exception;
	public Dictionary retrieveDictionary(String title, String lang) throws Exception;
}
