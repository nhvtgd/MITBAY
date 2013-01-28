package com.example.myapp;

import java.util.ArrayList;
import java.util.List;

import com.example.myapp.BuyingItems.Item;
import com.example.myapp.BuyingItems.ItemsAdapter;
import com.example.myapp.helper.ListViewAdapter;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SellingItems extends MITBAYActivity {

	/**
	 * The listview that holds the images (defined in xml file)
	 */
	ListView list;
	/**
	 * The adapter for the list
	 */
	private String username;
	private SharedPreferences settings;
	private ArrayList<Item> Items;
	private ListView listView;
	private Activity act;
	private ItemsAdapter adapter;
	private ParseObject object;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selling_items);
		// initial values
		settings = getSharedPreferences(SETTING, 0);
		username = settings.getString(USERNAME, "");
		listView = (ListView) findViewById(R.id.SellingItemsList);
		act = this;
		// Loading data
		Items = new ArrayList<Item>();
		Log.d("create Items", "OK");
		adapter = new ItemsAdapter(act, android.R.layout.simple_list_item_1, Items);
		Log.d("initiate adapter", "ok");
		listView.setAdapter(adapter);
		Log.d("running", "ok");
		getIDBuyingItems(username);
		Log.d("Running IDs", "Ok");
		// Set listener
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Item item = (Item) adapter.getItem(position);
				
			}
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_selling_items, menu);
		return true;
	}
	
	/**
	 * Get all IDs of the items
	 * @param username
	 */
	public void getIDBuyingItems(String username) {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", username);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if (arg1 == null) {
					for (ParseObject obj: arg0) {
						// Get object Item
						object = obj;
						// synchronized it
						addItemToList();
					}
				} else {}
			}
		});
	}
	
	public synchronized void addItemToList() {
		ParseFile file = (ParseFile) object.get("pic");
		file.getDataInBackground(new GetDataCallback(){
			public void done(byte[] data, ParseException e){
				if (e == null){ 
					Item item = new Item((String) object.get("name"), 
							(String) object.getCreatedAt().toString(), 
							(String) object.get("type"),
							(String) object.get("condition"), 
							(String) object.get("price"),
							object.getObjectId().toString(), 
							data);
					Items.add(item);
					adapter = new ItemsAdapter(act, android.R.layout.simple_list_item_1, Items);
					listView.setAdapter(adapter);
				} else {}
			}
		});
	}
	
	
	/**
	 * Item class
	 * @author Duy
	 *
	 */
	public class Item {
		public String item, date, type, condition, price,id;
		public byte[] file;

		// Construction
		public Item(String item, String date, String type, String condition, String price, String id, byte[] fileBitmap) {
			this.item = new String(item);
			this.date = new String(date);
			this.type = new String(type);
			this.condition = new String(condition);
			this.price = new String(price);
			this.id = new String(id);
			if (fileBitmap == null) this.file = null;
			else this.file = fileBitmap.clone();
		}
		// Copy
		public Item getCopy() {
			return new Item(this.item, this.date, this.type, this.condition, this.price, this.id, this.file);
		}
	}

	public class ItemsAdapter extends ArrayAdapter<Item> {
		public ArrayList<Item> items;
		public Context context;
		public ItemsAdapter(Context context, int textViewResourceId, ArrayList<Item> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.context.
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			Item item = this.items.get(position);
			if (item != null) {
				ImageView image = (ImageView) v.findViewById(R.id.list_item_picture);
				TextView information = (TextView) v.findViewById(R.id.list_item_information);
				TextView price  = (TextView) v.findViewById(R.id.list_item_price);
				// Set image
				Bitmap bitmap = null;
				if (item.file != null)  bitmap = BitmapFactory.decodeByteArray(item.file, 0, (item.file).length);
				image.setImageBitmap(bitmap);
				// Set text
				String text = String.format("%s %n%s %n%s", item.item,
						item.condition,item.date);
				information.setText(text);
				// Set price
				price.setText("$"+item.price);
			}
			return v;
		}
	}
	
}