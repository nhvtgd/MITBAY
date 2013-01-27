package com.example.myapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapp.helper.ListViewAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class BuyingItems extends MITBAYActivity {

	/**
	 * The listview that holds the images (defined in xml file)
	 */
	ListView list;
	/**
	 * The adapter for the list
	 */
	ArrayList<Sellable> sample;
	private ListViewAdapter adapter;
	private String username, email;
	private SharedPreferences settings;
	private ArrayList<Sellable> itemsList;
	private ArrayList<String> IDs;
	private Bitmap image;
	private Sellable sell;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_buying_items);
		// initial values
		settings = getSharedPreferences(SETTING, 0);
		username = settings.getString(USERNAME, "");
		email = settings.getString(EMAIL, "");
		IDs = new ArrayList<String>();
		// Loading data
		list = (ListView) findViewById(R.id.BuyingItemsList);
		// Get ID items
		Log.d("username", username);
		itemsList = new ArrayList<Sellable>();
		// Display on UI thread
		sample = getBuyingItems();
		adapter = new ListViewAdapter(this, sample);
		Log.d("adapter", "adapter running");
		list.setAdapter(adapter);
		Log.d("list", "list running");
		getIDBuyingItems(username);
		Log.d("running", "...");
//		long t0 = System.currentTimeMillis();
//		while (true) {
//			long t = System.currentTimeMillis();
//			if (t-t0 > 10000) {
//				sample.add(new Sellable(new User("Tran","viettran@mit.edu","abc"),"6.006","$30","TEXTBOOK"));
//			} else t0 = t;
//		}
	}

	public ArrayList<Sellable> getBuyingItems() {
		Sellable item1 = new Sellable(new User("Tran","viettran@mit.edu","abc"),"6.006","$30","TEXTBOOK");
		ArrayList<Sellable> itemList = new ArrayList<Sellable>();
		for (int i=0; i<5; i++)  itemList.add(item1);
		return itemList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_buying_items, menu);
		return true;
	}




	/**
	 * Get all IDs of the items
	 * @param username
	 */
	public void getIDBuyingItems(String username) {
		IDs.clear();
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", username);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if (arg1 == null) {
					int i = 0;
					for (ParseObject obj: arg0) {
						Log.d("ID"+(i++), obj.getObjectId().toString());
						IDs.add(obj.getObjectId());
					}
				} else {}
			}
		});
	}

	/**
	 * Get Sellable object from id
	 * @param id
	 * @return
	 */
	public Sellable getSellableFromID(String id) {
		// Parse object load small image
		ParseQuery query = new ParseQuery("Sellable");
		query.getInBackground(id, new GetCallback() {
			@Override
			public void done(ParseObject obj, ParseException arg1) {
				if (arg1 == null){
					// Load picture first
					//					ParseFile file = (ParseFile) obj.get("pic");
					//					file.getDataInBackground(new GetDataCallback(){
					//						public void done(byte[] data, ParseException e){
					//							image.recycle();
					//							if (e == null) image = BitmapFactory.decodeByteArray(data, 0, data.length);
					//							else image = null;
					//						}
					//					});
					image = BitmapFactory.decodeResource(getResources(), R.id.MITGreatDome);
					sell = new Sellable(new User((String) obj.get("seller"),"mylife", "cuocsong"),
							(String) obj.get("name"), 
							(String) obj.get("price"), 
							(String) obj.get("type"),
							(String) obj.get("description"),
							(String) obj.get("condition"),
							image);
					itemsList.add(sell);
				} else sell = null;
			}
		});
		return sell;
	}

}
