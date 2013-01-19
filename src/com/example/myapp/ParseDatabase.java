package com.example.myapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.myapp.Sellable.Condition;
import com.example.myapp.Sellable.SellType;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * A wrapper class for transmitting and receiving data from parse.com
 * 
 * @author Fable
 * 
 */
public class ParseDatabase {
	public ParseDatabase(Context context) {
		Parse.initialize(context, "2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");

	}

	/**
	 * 
	 * @param sell
	 *            : sellable
	 * @return a parseobject
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

	/**
	 * saves sellable on parse server
	 * 
	 * @param sell
	 *            : sellable
	 */
	public void sendSellableToServer(Sellable sell) {
		ParseObject sellobj = createSellableParseObj(sell);
		sellobj.saveEventually();
	}

	/**
	 * sends parseobject to server
	 * 
	 * @param obj
	 *            : parseobject
	 */
	public void sendParseObjToServer(ParseObject obj) {
		obj.saveEventually();
	}

	/**
	 * 
	 * @param user
	 *            : instance of User
	 * @param name
	 *            : name of sellable
	 * @return a list of sellable objects with that name belonging to user
	 */
	public List<Sellable> getSellableWithNameAndUser(User user, String name) {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", user.getName());
		query.whereEqualTo("name", name);
		CustomFindCallback c = new CustomFindCallback();
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * 
	 * @param user
	 *            : instance of User
	 * @return a list of sellable objects sold by user
	 */
	public List<Sellable> getSellableOfUser(User user) {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", user.getName());
		CustomFindCallback c = new CustomFindCallback();
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * Gets a specific sellable from Textbook, Electronics, Transportation, Misc
	 * 
	 * @param type
	 *            : Selltype enum
	 * @return a list of sellable objects of that selltype
	 */
	public List<Sellable> getType(SellType type) {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("type", type);
		CustomFindCallback c = new CustomFindCallback();
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * 
	 * @return a list of sellable objects that are enabled
	 */
	public List<Sellable> getEnabled() {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("enabled", true);
		CustomFindCallback c = new CustomFindCallback();
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * 
	 * @return a list of sellable objects that are disabled
	 */
	public List<Sellable> getDisabled() {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("enabled", false);
		CustomFindCallback c = new CustomFindCallback();
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * 
	 * @param cond
	 *            : Condition enum, parameter of sellable
	 * @return a list of sellables that have the specified condition
	 */
	public List<Sellable> getCondition(Condition cond) {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("condition", cond);
		CustomFindCallback c = new CustomFindCallback();
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * 
	 * @param date
	 *            : instance of Date
	 * @return a list of sellable objects created on date
	 */
	public List<Sellable> getDate(Date date) {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("date", date);
		CustomFindCallback c = new CustomFindCallback();
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * 
	 * @return a list of all sellable objects in the parse server
	 */
	public List<Sellable> getAllSellable() {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		CustomFindCallback c = new CustomFindCallback();
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * 
	 * @param skip
	 *            : number of entries to skip
	 * @return a list of all sellable objects that skip the specified number of
	 *         entries
	 */
	public List<Sellable> getAllSellableSetSkip(int skip) {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		CustomFindCallback c = new CustomFindCallback();
		query.setSkip(skip);
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * Sorts the results in ascending order by the parameter field
	 * 
	 * @param parameter
	 *            : parameter eg: name, price
	 * @return: a list of objects sorted by parameter
	 */
	public List<Sellable> returnInOrderByAscending(String parameter) {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		CustomFindCallback c = new CustomFindCallback();
		query.orderByAscending(parameter);
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * Sorts the results in ascending order by the parameter field
	 * 
	 * @param parameter
	 *            : parameter eg: name, price
	 * @return: a list of objects sorted by parameter
	 */
	public List<Sellable> returnInOrderByDescending(String parameter) {
		List<Sellable> sellables = new ArrayList<Sellable>();
		ParseQuery query = new ParseQuery("Sellable");
		CustomFindCallback c = new CustomFindCallback();
		query.orderByDescending(parameter);
		query.findInBackground(c);
		List<ParseObject> list = c.getList();
		for (ParseObject obj : list) {
			Sellable sell = createSellableWithParse(obj);
			sellables.add(sell);
		}
		return sellables;
	}

	/**
	 * Converts a sellable with a parseobject
	 * 
	 * @param obj
	 *            : parseobject
	 * @return a sellable that has the same parameters as obj
	 */
	public static Sellable createSellableWithParse(ParseObject obj) {
		User seller = (User) obj.get("seller");
		String name = (String) obj.get("name");
		String price = (String) obj.get("price");
		SellType type = (SellType) obj.get("type");
		String description = (String) obj.get("description");
		Condition condition = (Condition) obj.get("condition");
		@SuppressWarnings("unchecked")
		ArrayList<Bitmap> images = (ArrayList<Bitmap>) obj.get("images");
		Sellable sell = new Sellable(seller, name, price, type, description,
				condition, images);
		return sell;
	}

}
