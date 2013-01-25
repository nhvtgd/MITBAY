package com.example.myapp.helper;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParserTest {
	public static void main(String[] args) throws JSONException {
		JSONParser parse = new JSONParser();
		JSONObject obj = parse.getJSONFromUrl("http://coursews.mit.edu/coursews/?term=2013SP");
		System.out.println(obj.getJSONObject("id").toString());
	}
}
