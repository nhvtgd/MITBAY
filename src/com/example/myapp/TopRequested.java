package com.example.myapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapp.helper.AlertDialogManager;
import com.example.myapp.helper.ConnectionDetector;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * This class display the top requested item from the user It supports showing
 * the top 5, 10, 20, etc number of item
 * 
 * @author trannguyen
 * 
 */
public class TopRequested extends MITBAYActivity implements OnClickListener {

	/**
	 * The default number of items display
	 */
	private static final int DEFAULT_DISPLAY = 5;

	/**
	 * frequency table constructed from String of request
	 */
	Map<String, Integer> frequencyTable;

	ListView listView;

	/**
	 * Adapter to bind with listView
	 */
	CustomizedListFrequency adapter;

	/**
	 * Display dialog while dowing the data
	 */
	ProgressDialog progressDialog;

	/**
	 * the frequency list Pair to construct the internal data
	 */
	List<FrequencyPair> pair;

	ConnectionDetector connection;

	AlertDialogManager alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_requested);
		
		connection = new ConnectionDetector(this);
		if (connection.isConnectingToInternet()) {

			ParseDatabase data = new ParseDatabase(this);
			ParseQuery query = ParseUser.getQuery();
			Log.d("get All user", "Okay");
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Downloadig data...");
			progressDialog.show();
			query.findInBackground(new FindCallback() {
				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {
					if (arg1 == null) {

						Log.d("inside", "get Parse Object");
						createPrequencyTable(arg0);
					}

				}
			});
		} else {
			alert = new AlertDialogManager();
			alert.showAlertDialog(this, NETWORK_ERROR_TITLE,
					NETWORK_ERROR_MESSAGE, false);
		}
		Button topRequest = (Button) findViewById(R.id.show_top_requested);
		registerForContextMenu(topRequest);
		topRequest.setOnClickListener(this);

	}

	protected void createPrequencyTable(List<ParseObject> arg0) {
		Log.d("done loading frequency", "Okay");
		frequencyTable = Collections
				.synchronizedMap(new LinkedHashMap<String, Integer>());
		for (ParseObject obj : arg0) {
			ParseUser user = (ParseUser) obj;
			JSONArray arrayOfItem = user.getJSONArray("requesteditems");
			Log.d("Array", "get Json array");
			if (arrayOfItem != null && arrayOfItem.length() > 0) {
				for (int i = 0; i < arrayOfItem.length(); i++) {
					try {
						String key = arrayOfItem.get(i).toString()
								.toLowerCase();
						if (frequencyTable.containsKey(key)) {
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
			sortList(frequencyTable);
			displayTable(DEFAULT_DISPLAY);
		}

	}

	private void sortList(Map<String, Integer> frequencyTable2) {
		if (progressDialog.isShowing())
			progressDialog.cancel();
		pair = new ArrayList<FrequencyPair>();
		for (String item : frequencyTable.keySet()) {
			pair.add(new FrequencyPair(item, frequencyTable.get(item)
					.toString()));
		}
		Collections.sort(pair, frequencyComparator);

	}

	private void displayTable(int i) {
		listView = (ListView) findViewById(R.id.frequency_listView);
		Log.d("listView", "create listView");
		adapter = new CustomizedListFrequency(this, pair.subList(0, i));
		Log.d("listView2", "create listView");
		listView.setAdapter(adapter);
		Log.d("listView3", "create listView");
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

	static Comparator<FrequencyPair> frequencyComparator = new Comparator<FrequencyPair>() {

		@Override
		public int compare(FrequencyPair lhs, FrequencyPair rhs) {
			if (Integer.valueOf(lhs.frequency) < Integer.valueOf(rhs.frequency))
				return 1;
			else if (Integer.valueOf(lhs.frequency) > Integer
					.valueOf(rhs.frequency))
				return -1;
			else
				return lhs.frequency.compareTo(rhs.frequency);
		}
	};

	public class CustomizedListFrequency extends BaseAdapter {
		private Activity activity;
		/** The collections of sellable objects */
		private List<FrequencyPair> data;
		/** To inflate the view from an xml file */
		private LayoutInflater inflater = null;

		public CustomizedListFrequency(Activity a, List<FrequencyPair> pair) {
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
			item.setBackgroundResource(R.drawable.border_right);

			frequency.setText(pair.frequency);
			frequency.setBackgroundColor(R.drawable.border_right);

			return vi;
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.requested_menu, menu);
	}

	@Override
	public void onClick(View v) {
		this.openContextMenu(v);

	}

	private int returnSmaller(int request, int capacity) {
		return (request < capacity) ? request : capacity;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (frequencyTable != null) {
			switch (item.getItemId()) {
			case (R.id.show_5): {
				displayTable(returnSmaller(5, frequencyTable.size()));
				return true;

			}
			case (R.id.show_10): {
				displayTable(returnSmaller(10, frequencyTable.size()));
				return true;

			}
			case (R.id.show_20): {
				displayTable(returnSmaller(20, frequencyTable.size()));
				return true;
			}
			case (R.id.show_everything): {
				displayTable(frequencyTable.size());
				return true;
			}
			default:
				return false;
			}

		}
		else
			return false;
	}
}
