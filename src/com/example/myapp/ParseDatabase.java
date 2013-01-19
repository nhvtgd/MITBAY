package com.example.myapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
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
		obj.put("price", sell.getPrice());
		obj.put("type", sell.getType());
		obj.put("condition", sell.getCondition());
		obj.put("seller", sell.getSeller().getName());
		obj.put("enabled", sell.isEnabled());
		return obj;
	}

	/**
	 * saves sellable on parse server
	 * 
	 * @param sell
	 *            : sellable
	 */
	public String sendSellableToServer(Sellable sell) {
		ParseObject sellobj = createSellableParseObj(sell);
		sellobj.saveInBackground();
		return sellobj.getObjectId();
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
	 * @return parsequery you can use to query in parse server
	 */
	public ParseQuery getSellableWithNameAndUser(User user, String name) {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", user.getName());
		query.whereEqualTo("name", name);
		return query;
	}

	/**
	 * 
	 * @param user
	 *            : instance of User
	 * @return a list of sellable objects sold by user
	 */
	public ParseQuery getSellableOfUser(User user) {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", user.getName());
		return query;
	}

	/**
	 * Gets a specific sellable from Textbook, Electronics, Transportation, Misc
	 * 
	 * @param type
	 *            : Selltype enum
	 * @return a list of sellable objects of that selltype
	 */
	public ParseQuery getType(String type) {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("type", type);
		return query;
	}

	/**
	 * 
	 * @return a list of sellable objects that are enabled
	 */
	public ParseQuery getEnabled() {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("enabled", true);
		return query;
	}

	/**
	 * 
	 * @return a list of sellable objects that are disabled
	 */
	public ParseQuery getDisabled() {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("enabled", false);
		return query;
	}

	/**
	 * 
	 * @param cond
	 *            : Condition enum, parameter of sellable
	 * @return a list of sellables that have the specified condition
	 */
	public ParseQuery getCondition(String cond) {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("condition", cond);
		return query;
	}

	/**
	 * 
	 * @param date
	 *            : instance of Date
	 * @return a list of sellable objects created on date
	 */
	public ParseQuery getDate(Date date) {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("date", date);
		return query;
	}

	/**
	 * 
	 * @return a list of all sellable objects in the parse server
	 * @throws ParseException
	 */
	public ParseQuery getAllSellable() throws ParseException {
		ParseQuery query = new ParseQuery("Sellable");
		return query;
	}

	/**
	 * 
	 * @param skip
	 *            : number of entries to skip
	 * @return a list of all sellable objects that skip the specified number of
	 *         entries
	 */
	public ParseQuery getAllSellableSetSkip(int skip) {
		ParseQuery query = new ParseQuery("Sellable");
		query.setSkip(skip);
		return query;
	}

	/**
	 * Sorts the results in ascending order by the parameter field
	 * 
	 * @param parameter
	 *            : parameter eg: name, price
	 * @return: a list of objects sorted by parameter
	 */
	public ParseQuery returnInOrderByAscending(String parameter) {
		ParseQuery query = new ParseQuery("Sellable");
		query.orderByAscending(parameter);
		return query;
	}

	/**
	 * Sorts the results in ascending order by the parameter field
	 * 
	 * @param parameter
	 *            : parameter eg: name, price
	 * @return: a list of objects sorted by parameter
	 */
	public ParseQuery returnInOrderByDescending(String parameter) {
		ParseQuery query = new ParseQuery("Sellable");
		query.orderByDescending(parameter);
		return query;
	}

	/**
	 * Converts a sellable with a parseobject
	 * 
	 * @param obj
	 *            : parseobject
	 * @return a sellable that has the same parameters as obj
	 */
	public static Sellable createSellableWithParse(ParseObject obj) {

		String name = (String) obj.get("name");
		String price = (String) obj.get("price");

		String description = (String) obj.get("description");
		String type = (String) obj.get("type");
		String condition = (String) obj.get("condition");
		String seller = (String) obj.get("seller");
		User user = new User(seller, seller);
		Sellable sell = new Sellable(user, name, price, type, description,
				condition, null);
		return sell;
	}

}
