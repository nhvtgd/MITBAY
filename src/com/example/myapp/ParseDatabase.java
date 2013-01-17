package com.example.myapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.myapp.Sellable.Condition;
import com.example.myapp.Sellable.SellType;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

/**
 * A wrapper class for transmitting and receiving data from parse.com
 * 
 * @author Fable
 * 
 */
public class ParseDatabase {
	private List<Sellable> sellables;
	public ParseDatabase(Context context) {
		Parse.initialize(context, "2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");

	}

	/**
	 * 
	 * @param sell
	 * @return parseobj
	 */
	public static ParseObject createSellableParseObj(Sellable sell) {
		ParseObject obj = new ParseObject("Sellable");
		obj.put("name", sell.getName());
		obj.put("seller", sell.getSeller());
		obj.put("type", sell.getType());
		obj.put("price", sell.getPrice());
		obj.put("description", sell.getDescription());
		obj.put("enabled", sell.isEnabled());
		obj.put("condition", sell.getCondition());
		obj.put("date", sell.getDate());
		obj.put("images", sell.getImages());
		return obj;
	}

	public void sendSellableToServer(Sellable sell){
		ParseObject sellobj = createSellableParseObj(sell);
		sellobj.saveEventually();
	}
	
	public void sendParseObjToServer(ParseObject obj){
		obj.saveEventually();
	}
	public synchronized List<Sellable> getSellableWithNameAndUser(User user, String name) {
		sellables = new ArrayList<Sellable>(); //DO NOT REMOVE
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", user.getName());
		query.whereEqualTo("name", name);
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> list, ParseException e) {
				if (e == null) {
					for (ParseObject obj: list){
						Sellable sell = createSellableWithParse(obj);
						sellables.add(sell);
					}
				} else {
					System.err.println(e.toString());
				}
			}
		});
		return sellables;
	}
	public synchronized List<Sellable> getSellableOfUser(User user) {
		sellables = new ArrayList<Sellable>(); //DO NOT REMOVE
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", user.getName());
		query.findInBackground(new FindCallback() {
			
			public void done(List<ParseObject> list, ParseException e) {
				if (e == null) {
					for (ParseObject obj: list){
						Sellable sell = createSellableWithParse(obj);
						sellables.add(sell);
					}
				} else {
					System.err.println(e.toString());
				}
			}
		});
		
		return sellables;
	}	

	
	public static Sellable createSellableWithParse(ParseObject obj){
		User seller = (User) obj.get("seller");
		String name = (String) obj.get("name");
		String price = (String) obj.get("price");
		SellType type = (SellType) obj.get("type");
		String description = (String) obj.get("description");
		Condition condition = (Condition) obj.get("condition");
		@SuppressWarnings("unchecked")
		ArrayList<Bitmap> images = (ArrayList<Bitmap>) obj.get("images");
		Sellable sell = new Sellable(seller, name, price, type, description, condition, images);
		return sell;
	}

}
