package com.example.myapp;


import com.login.LogInPage;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetail extends MITBAYActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_item_detail, menu);
		return true;
	}
	/**
	 * Load information given by the Sellable object
	 */
	public void loadItemInformation() {}
	public void loadItemDescription() {}
	public void loadSellerInformation() {}
	
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