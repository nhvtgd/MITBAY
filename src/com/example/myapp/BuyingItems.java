package com.example.myapp;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.myapp.helper.ListViewAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BuyingItems extends Activity {

	/**
	 * The listview that holds the images (defined in xml file)
	 */
	ListView list;
	/**
	 * The adapter for the list
	 */
	ListViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buying_items);
		ListView list = (ListView) findViewById(R.id.BuyingItemsList);
		
		ArrayList<Sellable> itemList = getBuyingItems();
		adapter = new ListViewAdapter(this, (ArrayList<Sellable>) itemList);
		// bind the adapter with the view
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Sellable item = (Sellable) adapter.getItem(position);
				Log.d("custom", item.getName());
				Log.d("custom", item.getType().toString());
				Log.d("custom", item.getPrice());
				Log.d("custom", item.getDate());
			}
		});
	}
	
	public ArrayList<Sellable> getBuyingItems() {
		Sellable item1 = new Sellable(new User("Tran","viettran@mit.edu","abc"),"6.006","$30","TEXTBOOK");
		ArrayList<Sellable> itemList = new ArrayList<Sellable>();
		for (int i=0; i<100; i++)  itemList.add(item1);
		return itemList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_buying_items, menu);
		return true;
	}

}
