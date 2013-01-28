package com.example.myapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.myapp.helper.ConnectionDetector;
import com.example.myapp.helper.ListViewAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListActivity;

public class NotificationHistory extends ListActivity implements
		OnClickListener {

	ListView listView;
	//String[] requests;
	ArrayList<String> requests = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(getApplicationContext(), "2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");
		ParseQuery query = ParseUser.getQuery();
		String id = loadSettingData();
		query.getInBackground(id, new GetCallback() {
			  public void done(ParseObject object, ParseException e) {
			    if (e == null) {
			    	JSONArray jsonArray = object.getJSONArray("requesteditems");
			    	ArrayList<String> list = new ArrayList<String>();     
			    	if (jsonArray != null) { 
			    	   int len = jsonArray.length();
			    	   for (int i=0;i<len;i++){ 
			    	    try {
							requests.add(jsonArray.get(i).toString());
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			    	   } 
			    	} 
			    	
			    } else {
			    }
			  }
			});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, requests);
		setListAdapter(adapter);
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
