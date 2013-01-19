package com.example.myapp;

import java.util.ArrayList;

public class User {
	private String name;
	private String email;
	private String password;
	private int id;
	private ArrayList<Sellable> itemHistory;
	
	public User(String name, String email, String password){
		this.name = name;
		this.email = email;
		this.password = password;
		this.setItemHistory(new ArrayList<Sellable>());
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getEmail(){
		return this.email;
	}

	public ArrayList<Sellable> getItemHistory() {
		return itemHistory;
	}

	public void setItemHistory(ArrayList<Sellable> itemHistory) {
		this.itemHistory = itemHistory;
	}
	
	
}
