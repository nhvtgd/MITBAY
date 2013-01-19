package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
/**
 * This class is the base class that will keep all of the preferences 
 * for the project
 * @author trannguyen
 *
 */
public class MITBAYActivity extends Activity {

	
	
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
