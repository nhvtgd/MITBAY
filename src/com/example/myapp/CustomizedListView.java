package com.example.myapp;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.myapp.Sellable.SellType;
import com.example.myapp.helper.ListViewAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * This class creates the list selection screen when the user click on a
 * paticular category, will need to work out how to push image to server
 * 
 * @author trannguyen
 * 
 */
public class CustomizedListView extends Activity {
	/**
	 * The listview that holds the images (defined in xml file)
	 */
	ListView list;
	/**
	 * The adapter for the list
	 */
	ListViewAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customized_list_view);
		ParseDatabase database = new ParseDatabase(this);
		// the item list will store all of the sellable objects and ignite when
		// the user clicks on a particular item
		Sellable group1 = new Sellable(new User("Tran","viettran@mit.edu","abc"),"6.006","$30",SellType.TEXTBOOK);
		Sellable group2 = new Sellable(new User("Duy","duyha@mit.edu","abc"),"6.005","$20",SellType.TEXTBOOK);
		
		ArrayList<Sellable> itemList = new ArrayList<Sellable>(Arrays.asList(
				group1, group2));

		list = (ListView) findViewById(R.id.my_list);

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
}
