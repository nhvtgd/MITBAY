package com.example.myapp;

public class User {
	private String name;
	private String email;
	private String password;
	private int id;
	
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
}
