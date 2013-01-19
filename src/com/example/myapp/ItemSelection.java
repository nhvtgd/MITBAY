package com.example.myapp;

import com.example.myapp.helper.ItemsAutoCompleteAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.AutoCompleteTextView;

public class ItemSelection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_selection);
		
	    
	    AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.auto_complete_item);
	    autoCompView.setAdapter(new ItemsAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_item_selection, menu);
		return true;
	}

}
