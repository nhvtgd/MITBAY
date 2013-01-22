package com.example.myapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.login.LogInPage;

public class ItemDetail extends MITBAYActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		Bundle bundle = getIntent().getExtras();
		loadPicture(bundle);
		loadTextInformation(bundle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_item_detail, menu);
		return true;
	}
	/**
	 * Load picture from extras of the last axtivity
	 */
	public void loadPicture(Bundle bundle) {
		
	}
	/**
	 * Load Item information from extras of the last activity
	 * @param bundle
	 */
	public void loadTextInformation(Bundle bundle) {
		// Item name
		((TextView) findViewById(R.id.ItemDetail_ItemName))
		.setText(bundle.getString("item_name", "Anonymous"));
		// Category
		((TextView) findViewById(R.id.ItemDetail_Category))
		.setText(bundle.getString("category", "Not defined"));
		// Price
		((TextView) findViewById(R.id.ItemDetail_Price))
		.setText(bundle.getString("price", "No price"));;
		// Description
		((TextView) findViewById(R.id.ItemDetail_Description))
		.setText(bundle.getString("description", ""));
		// Get seller information
		String user_information = String.format("%s %n %s",
				bundle.getString("username", "Anonymous"),
				bundle.getString("email", "No email available"));
		((TextView) findViewById(R.id.ItemDetail_Seller)).
								setText(user_information);
	}
	
	/**
	 * Do action buy item, need to check log in
	 * @param view
	 */
	public void buyOneItem(View view) {
		Toast.makeText(view.getContext(), "will process to buy the item", Toast.LENGTH_SHORT).show();
		// Already log in
		if (isAlreadyLogIn()) {
			Intent i = new Intent(view.getContext(), ConfirmBuyItem.class);
			startActivity(i);
		} else { // Not yet, go back to Log in page 
			Intent i = new Intent(view.getContext(), LogInPage.class);
			startActivity(i);
		}
	}
	/**
	 * Check log in
	 * @return 	true if already log in
	 * 			false, otherwise
	 */
	public boolean isAlreadyLogIn() {
		return true;
	}
}