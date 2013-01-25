package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapp.helper.ItemsAutoCompleteAdapter;

public class ItemSelection extends Activity implements OnClickListener {

	private static final String TEXTBOOK = "text";
	private static final String FURNITURE = "fur";
	private static final String TRANSPORTATION = "trans";
	private static final String MISC = "misc";
	private static final String ALL= "all";
	private AutoCompleteTextView searchQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_selection);

		searchQuery = (AutoCompleteTextView) findViewById(R.id.auto_complete_item);
		searchQuery.setAdapter(new ItemsAutoCompleteAdapter(this,
				android.R.layout.simple_dropdown_item_1line));

		Button textbook = (Button) findViewById(R.id.textbook_button);
		textbook.setOnClickListener(this);
		Button furniture = (Button) findViewById(R.id.furniture_button);
		furniture.setOnClickListener(this);
		Button trans = (Button) findViewById(R.id.bike_button);
		trans.setOnClickListener(this);
		Button misc = (Button) findViewById(R.id.miscellaneous_button);
		misc.setOnClickListener(this);
		ImageButton all = (ImageButton) findViewById(R.id.browse_all_selection);
		all.setOnClickListener(this);
		ImageButton search = (ImageButton) findViewById(R.id.search_icon_32);
		search.setOnClickListener(this);
		searchQuery = (AutoCompleteTextView) findViewById(R.id.auto_complete_item);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_item_selection, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, CustomizedListView.class);
		switch (v.getId()) {
		case (R.id.textbook_button): {
			intent.putExtra("query", TEXTBOOK);
			startActivity(intent);
			break;
		}
		case (R.id.furniture_button): {
			intent.putExtra("query", FURNITURE);
			startActivity(intent);
			break;
		}
		case (R.id.bike_button): {
			intent.putExtra("query", TRANSPORTATION);
			startActivity(intent);
			break;
		}
		case (R.id.miscellaneous_button): {
			intent.putExtra("query", MISC);
			startActivity(intent);
			break;
		}
		case (R.id.browse_all_selection): {
			intent.putExtra("query", ALL);
			startActivity(intent);
			break;
		}
		case (R.id.search_icon_32): {
			String search = searchQuery.getText().toString().trim();
			if (search.length() > 0){
				intent.putExtra("search", search);
				startActivity(intent);
			}
			break;
		}
		}
	}

}
