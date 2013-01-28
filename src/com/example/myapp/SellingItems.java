package com.example.myapp;

import java.util.ArrayList;
import java.util.List;

import com.example.myapp.BuyingItems.Item;
import com.example.myapp.BuyingItems.ItemsAdapter;
import com.example.myapp.helper.ListViewAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;
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
	private int number_query;
	private Intent intent;
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
		getBuyingItems(username);
		getImageBuyingItems(username);
		Log.d("Running IDs", "Ok");
		// Set listener
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Item item = (Item) adapter.getItem(position);
				intent = new Intent(v.getContext(), ConfirmBuyItem.class);
				intent.putExtra("isEdit", true);
				ParseQuery query = new ParseQuery("Sellable");
				query.getInBackground(item.id, new GetCallback() {
					@Override
					public void done(ParseObject obj, ParseException e) {
						if (e == null) {
							Sellable item = ParseDatabase.createSellableWithParse(obj);
							// Put extras item, date, condition, price, description, username, email, type, id;
							intent.putExtra(ITEM, item.getName());
							intent.putExtra(DATE, item.getDate());
							intent.putExtra(CONDITION, item.getCondition());
							intent.putExtra(PRICE, item.getPrice());
							intent.putExtra(DESCRIPTION, item.getDescription());
							intent.putExtra(USERNAME, item.getSeller().getName());
							intent.putExtra(EMAIL, item.getSeller().getEmail());
							intent.putExtra(TYPE, item.getType());
							intent.putExtra(ID, item.getId());
							startActivity(intent);
						} else {
							Toast.makeText(getApplicationContext(), "There is a problem with server", Toast.LENGTH_SHORT).show();
							startActivity(intent);
						}
					}
				});
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
	public void getBuyingItems(String username) {
		Log.d("find buying item", "ok");
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("seller", username);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if (arg1 == null) {
					for (ParseObject obj: arg0) {
						Log.d("create object", "Ok");
						Item item = new Item((String) obj.get("name"), 
								(String) obj.getCreatedAt().toString(), 
								(String) obj.get("type"),
								(String) obj.get("condition"), 
								(String) obj.get("price"),
								obj.getObjectId().toString(), 
								null);
						Log.d("finish","");
						Items.add(item);
						Log.d("added", "ok");
						adapter = new ItemsAdapter(act, android.R.layout.simple_list_item_1, Items);
						listView.setAdapter(adapter);
						Log.d("UI thread", "ok");

					} 
				} else {}
			}
		});
	}

	/**
	 * Get all images of the items
	 * @param username
	 */
	public void getImageBuyingItems(String username) {
		ParseQuery query = new ParseQuery("Sellable");
		query.whereEqualTo("buyer", username);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if (arg1 == null) {
					for (ParseObject obj: arg0) {
						Log.d("load file", "Ok");
						ParseFile file = (ParseFile) obj.get("pic");
						file.getDataInBackground(new GetDataCallback(){
							public void done(byte[] data, ParseException e){
								Bitmap image = null;
								if (e == null) {
									Log.d("create bitmap", "Ok");
									image = BitmapFactory.decodeByteArray(data, 0, data.length);
								} else {
									image = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);
								}
								Log.d("update picture", "ok");
								Log.d("picture == null", ""+(image==null));
								for (Item item:Items) {
									if (item.bitmap == null) {
										item.bitmap = Bitmap.createBitmap(image);
										break;
									}
								}
								Log.d("update UI", "ok");
								adapter = new ItemsAdapter(act, android.R.layout.simple_list_item_1, Items);
								listView.setAdapter(adapter);
							}
						});
						
					}
				}else {}
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
		public Bitmap bitmap;

		// Construction
		public Item(String item, String date, String type, String condition, String price, String id, Bitmap bitmap) {
			this.item = new String(item);
			this.date = new String(date);
			this.type = new String(type);
			this.condition = new String(condition);
			this.price = new String(price);
			this.id = new String(id);
			if (bitmap != null) this.bitmap = Bitmap.createBitmap(bitmap);
			else this.bitmap = null;
		}
		// Copy
		public Item getCopy() {
			return new Item(this.item, this.date, this.type, this.condition, this.price, this.id, this.bitmap);
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
				image.setImageBitmap(item.bitmap);
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