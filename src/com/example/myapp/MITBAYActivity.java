package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
/**
 * This class is the base class that will keep all of the preferences 
 * for the project
 * @author trannguyen
 *
 */
public class MITBAYActivity extends Activity {

	public static final String TEXTBOOK = "TEXTBOOK";
	public static final String FURNITURE= "FURNITURE";
	public static final String TRANSPORTATION = "FURNITURE";
	public static final String MISC= "MISC";
	public static final String USERNAME="username";
	public static final String EMAIL="email";
	public static final String DATE="date";
	public static final String TYPE="type";
	public static final String DESCRIPTION="description";
	public static final String CONDITION="condition";
	public static final String PRICE="price";
	public static final String ITEM="item";
	public static final String ID="id";
	public static final String IMAGE="image";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitbay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mitbay, menu);
		return true;
	}

}
