package com.example.myapp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

public class CustomFindCallback extends FindCallback{
	List<ParseObject> listOfParse;
	@Override
	public void done(List<ParseObject> list, ParseException e) {
		if (e == null) {
			listOfParse = list;
		} else {
			System.err.println(e.toString());
		}
		
	}
	
	public List<ParseObject> getList(){
		return listOfParse;
	}

}
