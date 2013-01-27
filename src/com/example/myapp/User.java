package com.example.myapp;

import java.util.ArrayList;

public class User {
	private String name;
	private String email;
	private String password;
	private ArrayList<String> requests;
	private ArrayList<Sellable> buyingItems;
	private ArrayList<Sellable> sellingItems;
	private String location;
	private String DEFAULT_LOCATION = "Not set yet";

	public User(String name, String email) {
		this.name = name;
		this.email = email;
		this.requests = new ArrayList<String>();
		this.buyingItems = new ArrayList<Sellable>();
		this.sellingItems = new ArrayList<Sellable>();
		this.location = DEFAULT_LOCATION;

	}

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.requests = new ArrayList<String>();
		this.buyingItems = new ArrayList<Sellable>();
		this.sellingItems = new ArrayList<Sellable>();
		this.location = DEFAULT_LOCATION;

	}
	
	public User(String name, String email, String password,String location) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.requests = new ArrayList<String>();
		this.buyingItems = new ArrayList<Sellable>();
		this.sellingItems = new ArrayList<Sellable>();
		this.location = location;

	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}

	public void addItemToRequests(String item) {
		this.requests.add(item);
	}

	public void addItemToBuyingLists(Sellable item) {
		this.buyingItems.add(item);
	}

	public void addItemToSellingLists(Sellable item) {
		this.sellingItems.add(item);
	}

	public void addMultipleToBuyingLists(ArrayList<Sellable> items) {
		for (Sellable item : items)
			this.buyingItems.add(item);
	}

	public void addMultipleToSellingLists(ArrayList<Sellable> items) {
		for (Sellable item : items)
			this.sellingItems.add(item);
	}

	public void addMultipleItemsToRequests(ArrayList<String> items) {
		for (String i : items) {
			this.requests.add(i);
		}
	}

	public void changeLocation(String location) {
		this.location = location;
	}

}
