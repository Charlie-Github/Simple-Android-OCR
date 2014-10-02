package com.edible.other;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>{

	@Override
	public Date deserialize(JsonElement jsonElement, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		// TODO Auto-generated method stub
		if(jsonElement == null) {
			return null;
		} else {
			if(jsonElement.toString().contains("-")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					return df.parse(jsonElement.getAsString());
				} catch (ParseException e) {
					return null;
				}
			} else {
				return new Date(jsonElement.getAsLong());
			}
		}
	}

	@Override
	public JsonElement serialize(Date date, Type type,
			JsonSerializationContext context) {
		// TODO Auto-generated method stub
		if(date == null) {
			return null;
		} else {
			return new JsonPrimitive(date.getTime());
		}
	}

}
