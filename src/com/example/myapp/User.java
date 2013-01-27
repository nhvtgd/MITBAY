package com.example.myapp;

import java.util.ArrayList;

public class User {
	private String name;
	private String email;
	private String password;
	private ArrayList<String> requests;
	private String location;

	public User(String name, String email){
		this.name = name;
		this.email = email;
		this.requests = new ArrayList<String>();
	}
	public User(String name, String email, String password){
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void addItemToRequests(String item){
		this.requests.add(item);
	}
	
	public void addMultipleItemsToRequests(ArrayList<String> items){
		for(String i: items){
			this.requests.add(i);
		}
	}
	
	public void changeLocation(String location){
		this.location = location;
	}

}
