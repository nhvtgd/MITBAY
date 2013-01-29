package com.example.myapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class NotificationHistory extends ListActivity implements
		OnClickListener {

	ListView listView;
	// String[] requests;
	ArrayList<String> requests = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(getApplicationContext(),
				"2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");
		settingUpData();
	}

	private void settingUpData() {
		ParseQuery query = ParseUser.getQuery();
		String id = loadSettingData();
		query.getInBackground(id, new GetCallback() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					updatingAdapter(object);

				} else {
				}
			}
		});

	}

	protected void updatingAdapter(ParseObject object) {
		JSONArray jsonArray = object.getJSONArray("requesteditems");
		if (jsonArray != null) {
			int len = jsonArray.length();
			for (int i = 0; i < len; i++) {
				try {
					requests.add(jsonArray.get(i).toString());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		}

		listView = (ListView) findViewById(R.id.list_notification_history);
		ArrayAdapter<String> adapter = new MySimpleArrayAdapter(this,
				requests);
		setListAdapter(adapter);

	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> values;

		public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
			super(context, R.layout.request_layout, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.request_layout, parent,
					false);
			EditText textView = (EditText) rowView
					.findViewById(R.id.edit_text_notification_history);
			textView.setText(values.get(position));
			// Change the icon for Windows and iPhonex
			return rowView;
		}
	}

	/**
	 * Loading setting data
	 */
	public String loadSettingData() {
		// GetSharedPreferences
		settings = getSharedPreferences(MITBAYActivity.SETTING, 0);
		String id = settings.getString(MITBAYActivity.USERID, "No Id found");

		return id;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_notification_history, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
