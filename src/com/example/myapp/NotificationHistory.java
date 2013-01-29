package com.example.myapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
		setContentView(R.layout.activity_notification_history);
		Parse.initialize(getApplicationContext(),
				"2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");
		settingUpData();

		EditText newRequest = (EditText) findViewById(R.id.new_request_notification_history);
		Button addRequest = (Button) findViewById(R.id.request_button_notification_history);
		addRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

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

	private void deleteOnServer(final ArrayList<String> request) {
		ParseQuery query = ParseUser.getQuery();
		String id = loadSettingData();
		query.getInBackground(id, new GetCallback() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {

					object.put("requesteditems", request);
					object.saveInBackground();
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
		adapter = new MySimpleArrayAdapter(this, requests);
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.request_layout, parent,false);
			TextView textView = (TextView) rowView
					.findViewById(R.id.text_view_notification_history);
			Button delete = (Button) rowView.findViewById(R.id.delete_button);
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					requests.remove(position);
					deleteOnServer(requests);
					adapter.notifyDataSetChanged();

				}
			});
			textView.setText(values.get(position));

			return rowView;
		}
	}

	private void makeEditable(boolean isEditable, EditText et) {
		if (isEditable) {

			et.setFocusable(true);
			et.setEnabled(true);
			et.setClickable(true);
			et.setFocusableInTouchMode(true);
		} else {
			et.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
			et.setFocusable(false);
			et.setClickable(false);
			et.setFocusableInTouchMode(false);
			et.setEnabled(false);
			et.setKeyListener(null);
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
