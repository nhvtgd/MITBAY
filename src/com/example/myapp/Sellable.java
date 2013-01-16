package com.example.myapp;

import java.util.Date;

public class Sellable {
	private String name;
	private String price;
	private User user;
	private Date date;
	private String description;
	private boolean enabled;
	
	public enum Condition {NEW,	USED, ACCEPTABLE};
	
	/**
	 * 
	 * @param user
	 * @param name
	 * @param price
	 */
	
	public Sellable(User user,String name,String price){
		this.user = user;
		this.name = name;
		this.price = price;	
		this.enabled = true;
	}
	
	public Double convertPriceToDouble(String price){
		return null;
	}
	
	public void disableSellable(){
		enabled = false;
	}
	
	public void enableSellable(){
		enabled = true;
	}
	
	

}
