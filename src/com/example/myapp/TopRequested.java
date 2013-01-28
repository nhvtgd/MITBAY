package com.example.myapp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TopRequested extends Activity {

	Hashtable<String, Integer> frequencyTable;

	ListView listView;

	CustomizedListFrequency adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_requested);

		ParseDatabase data = new ParseDatabase(this);
		ParseQuery query = ParseUser.getQuery();
		Log.d("get All user", "Okay");
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if (arg1 == null) {
					Log.d("inside", "get Parse Object");
					createPrequencyTable(arg0);
				}

			}
		});

	}

	protected void createPrequencyTable(List<ParseObject> arg0) {
		Log.d("done loading frequency", "Okay");
		frequencyTable = new Hashtable<String, Integer>();
		for (ParseObject obj : arg0) {
			ParseUser user = (ParseUser) obj;
			JSONArray arrayOfItem = user.getJSONArray("requesteditems");
			Log.d("Array", "get Json array");
			if (arrayOfItem != null && arrayOfItem.length() > 0) {
				for (int i = 0; i < arrayOfItem.length(); i++) {
					try {
						String key = arrayOfItem.get(i).toString()
								.toLowerCase();
						if (frequencyTable.contains(key)) {
							frequencyTable
									.put(key, frequencyTable.get(key) + 1);
						} else {
							frequencyTable.put(key, 1);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		if (frequencyTable.size() > 0) {
			ArrayList<FrequencyPair> pair = new ArrayList<FrequencyPair>();
			for (String item : frequencyTable.keySet()) {
				pair.add(new FrequencyPair(item, frequencyTable.get(item)
						.toString()));
			}
			listView = (ListView) findViewById(R.id.frequency_listView);
			Log.d("listView", "create listView");
			adapter = new CustomizedListFrequency(this, pair);
			Log.d("listView2", "create listView");
			listView.setAdapter(adapter);
			Log.d("listView3", "create listView");
		}

		// if (frequencyTable.size() > 0) {
		// TableLayout tbl = (TableLayout) findViewById(R.id.Frequency_Table);
		// for (String item : frequencyTable.keySet()) {
		// // delcare a new row
		//
		// TableRow newRow = new TableRow(this);
		// // add views to the row
		// TextView itemView = new TextView(this);
		// Log.d("item", item);
		// itemView.setText(item);
		// newRow.addView(new TextView(this));
		// TextView itemFrequency = new TextView(this);
		// Log.d("item", frequencyTable.get(item).toString());
		// itemFrequency.setText(frequencyTable.get(item).toString());
		// Log.d("item2", frequencyTable.get(item).toString());
		// newRow.addView(itemFrequency);
		// Log.d("item3", frequencyTable.get(item).toString());
		// tbl.addView(newRow, new TableLayout.LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		// Log.d("finish", "yay");
		// }
		// }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_top_requested, menu);
		return true;
	}

	public class FrequencyPair {
		String item;
		String frequency;

		public FrequencyPair(String item, String frequency) {
			this.item = item;
			this.frequency = frequency;
		}

	}

	public class CustomizedListFrequency extends BaseAdapter {
		private Activity activity;
		/** The collections of sellable objects */
		private ArrayList<FrequencyPair> data;
		/** To inflate the view from an xml file */
		private LayoutInflater inflater = null;

		public CustomizedListFrequency(Activity a, ArrayList<FrequencyPair> pair) {
			activity = a;
			data = pair;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.frequency_table, null);
			Log.d("getView", "start GetView");
			TextView item = (TextView) vi.findViewById(R.id.top_item);
			TextView frequency = (TextView) vi
					.findViewById(R.id.frequency_item);
			FrequencyPair pair = data.get(position);

			item.setText(pair.item);

			frequency.setText(pair.frequency);

			return vi;
		}

	}

}
