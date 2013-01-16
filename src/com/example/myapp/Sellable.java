package com.example.myapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Bitmap;

public class Sellable {
	private String name;
	private User seller;
	private String price;
	private User buyer;
	private String date;
	private String description;
	private boolean enabled;
	private int id;
	private SellType type;
	private ArrayList<Bitmap> images;
	private Condition condition;
	private static final String DEFAULT_DESCRIPTION = "Descibe as above";
	private static final Condition DEFAULT_CONDITION = Condition.ACCEPTABLE;

	/**
	 * enum type represents condition
	 * @author trannguyen
	 *
	 */
	public enum Condition {
		NEW, USED, ACCEPTABLE
	};

	/**
	 * enum type represents condition
	 * @author trannguyen
	 *
	 */
	public enum SellType {
		TEXTBOOK, ELECTRONIC, TRANSPORTATION, MISC
	};

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

	public Sellable(User seller, String name, String price, SellType type,
			String description, Condition condition, ArrayList<Bitmap> images) {
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

	}

	/**
	 * 
	 * @param user
	 * @param name
	 * @param price
	 */

	public Sellable(User seller, String name, String price, SellType type) {
		this.setSeller(seller);
		this.name = name;
		this.setPrice(price);
		this.setEnabled(true);
		this.setBuyer(null);
		this.setId(getIDFromServer());
		this.setType(type);
		this.setDate(getCurrentDate());
		this.description = DEFAULT_DESCRIPTION;
		this.setImages(getDefaultImage(type));
		this.setCondition(DEFAULT_CONDITION);

	}

	private ArrayList<Bitmap> getDefaultImage(SellType type2) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIDFromServer() {
		return 0;
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

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public SellType getType() {
		return type;
	}

	public void setType(SellType type) {
		this.type = type;
	}

	public ArrayList<Bitmap> getImages() {
		return images;
	}

	public void setImages(ArrayList<Bitmap> images) {
		this.images = images;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

}
