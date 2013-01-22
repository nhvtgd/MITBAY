package com.example.myapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RequestingItems extends Activity {

	/**
	 * The listview that holds the images (defined in xml file)
	 */
	ListView list;
	/**
	 * The adapter for the list
	 */
	RequestAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_requesting_items);
		ListView list = (ListView) findViewById(R.id.RequestingItemsList);

		ArrayList<Request> requests = getRequests();
		adapter = new RequestAdapter(this, android.R.layout.simple_list_item_1, requests);
		// bind the adapter with the view
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("custom", "okkkkkkkk");
			}
		});
	}

	public ArrayList<Sellable> getBuyingItems() {
		Sellable item1 = new Sellable(new User("Tran","viettran@mit.edu","abc"),"6.006","$30","TEXTBOOK");
		ArrayList<Sellable> itemList = new ArrayList<Sellable>();
		for (int i=0; i<100; i++)  itemList.add(item1);
		return itemList;
	}
	public class Request {
		private String text;
		public boolean isClicked;
		public long time;

		public Request(String text) {
			this.text = text;
			this.isClicked = false; // need server
			this.time = System.currentTimeMillis();
		}
		public String getText() { return text; }
	}

	public class compareRequests implements Comparator<Request> { 
		public int compare(Request b, Request a) {
			if (a.isClicked==true && b.isClicked==false) return -1;
			if (a.isClicked==false && b.isClicked==true) return 1;
			if (a.time>b.time) return 1;
			else return -1;
		}
	}
	public ArrayList<Request> getRequests() {
		ArrayList<Request> requests = new ArrayList<Request>();
		for (int i=0; i<50; i++) {
			Request r = new Request("request "+i); 
			r.isClicked = (System.currentTimeMillis() % (long)(i+1) != 0);
			requests.add(r);
		}
		sortRequest(requests);
		return requests;
	}
	public void sortRequest(ArrayList<Request> requests) {
		// Sort by isClicked, date
		Collections.sort(requests, new compareRequests());
	}

	public class RequestAdapter extends ArrayAdapter<Request> {
		private Context context;
		private ArrayList<Request> requests;

		public RequestAdapter(Context context, int textViewResourceId, ArrayList<Request> requests) {
			super(context, textViewResourceId, requests);
			this.requests= requests;
			this.context = context;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.context.
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.request_layout, null);
			}

			Request request = this.requests.get(position);
			if (request != null) {
				TextView requestField = (TextView) v.findViewById(R.id.RequestText);
				if (request != null) {
					requestField.setText(request.getText()+", "+request.isClicked+", "+request.time);
					if (!request.isClicked) requestField.setTextColor(getResources().getColor(R.color.red));
					else requestField.setTextColor(getResources().getColor(R.color.black));
				}
			}
			return v;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_requesting_items, menu);
		return true;
	}

}
