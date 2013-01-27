package com.example.myapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Sellable {
	private String name;
	private User seller;
	private String price;
	private User buyer;
	private String date;
	private String description;
	private boolean enabled;
	private String id;
	private String type;
	private Bitmap images;
	private String condition;
	private String location;

	private final String DEFAULT_LOCATION = "Please contact Seller";

	private final String DEFAULT_CONDITION = "NEW";

	private final String DEFAULT_DESCIPTION = "No Description Provided";

	private final String DEFAULT_ID = "Default";

	/**
	 * This is a constructor to initialize the Sellable object after the seller
	 * fill out the information for items
	 * 
	 * @param seller
	 *            the User represents seller
	 * @param name
	 *            String the name of the item
	 * @param price
	 *            String the price of the item
	 * @param type
	 *            enum SellType (4 categories)
	 * @param description
	 *            String the text describe the item
	 * @param condition
	 *            enum Condition (3 categories)
	 * @param images
	 *            ArrayList of Bitmap represents the picture of items
	 * 
	 */

	public Sellable(User seller, String name, String price, String type,
			String description, String condition, Bitmap images) {
		this.setSeller(seller);
		this.name = name;
		this.setPrice(price);
		// use for quick buy and reactivation
		this.setEnabled(true);
		this.setBuyer(null);
		this.setId(getIDFromServer());
		this.setType(type);
		this.setDate(getCurrentDate());
		this.description = description;
		this.setImages(images);
		this.setCondition(condition);
		this.setDate(getCurrentDate());
		this.setLocation(getDefaultLocation());

	}

	public void setLocation(String defaultLocation) {
		this.location = defaultLocation;

	}

	public String getLocation() {
		return this.location;
	}

	public String getDefaultLocation() {
		return DEFAULT_LOCATION;
	}

	/**
	 * 
	 * @param user
	 * @param name
	 * @param price
	 */

	public Sellable(User seller, String name, String price, String type) {
		this.setSeller(seller);
		this.name = name;
		this.setPrice(price);
		this.setEnabled(true);
		this.setBuyer(null);
		this.setId(getIDFromServer());
		this.setType(type);
		this.setDate(getCurrentDate());
		this.description = DEFAULT_DESCIPTION;
		this.setImages(getDefaultImage(type));
		this.setCondition(DEFAULT_LOCATION);
		this.setLocation(getDefaultLocation());

	}

	public Bitmap getDefaultImage(String type) {
		return null;

	}

	public String getIDFromServer() {
		return DEFAULT_ID;
	}

	public Double convertPriceToDouble(String price) {
		return null;
	}

	public void disableSellable(User buyer) {
		this.setBuyer(buyer);
		setEnabled(false);
	}

	public void enableSellable() {
		setBuyer(null);
		setEnabled(true);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrentDate() {

		Calendar c = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		return formattedDate;

	}

	public String getName() {
		return name;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getType() {
		return type;
	}

	public void setType(String type2) {
		this.type = type2;
	}

	public Bitmap getImages() {
		return images;
	}

	public void setImages(Bitmap images) {
		this.images = images;
	}
	
	public void setDefaultImage(Bitmap images){
		this.images = images;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
