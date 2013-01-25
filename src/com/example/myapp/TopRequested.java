package com.example.myapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TopRequested extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_requested);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_top_requested, menu);
		return true;
	}

}
