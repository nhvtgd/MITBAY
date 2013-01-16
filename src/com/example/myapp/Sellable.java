package com.example.myapp;

import java.util.Date;

public class Sellable {
	private String name;
	private User seller;
	private String price;
	private User buyer;
	private Date date;
	private String description;
	private boolean enabled;
	private int id;
	
	public enum Condition {NEW,	USED, ACCEPTABLE};
	
	/**
	 * 
	 * @param user
	 * @param name
	 * @param price
	 */
	
	public Sellable(User seller,String name,String price){
		this.seller = seller;
		this.name = name;
		this.price = price;	
		this.enabled = true;
		this.buyer = null;
		this.id = getIDFromServer();
	}
	
	public int getIDFromServer(){
		return 0;
	}
	
	public Double convertPriceToDouble(String price){
		return null;
	}
	
	public void disableSellable(User buyer){
		this.buyer = buyer;
		enabled = false;
	}
	
	public void enableSellable(){
		buyer = null;
		enabled = true;
	}
	
	

}
