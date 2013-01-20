package com.example.myapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ConfirmBuyItem extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_buy_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_confirm_buy_item, menu);
		return true;
	}
	public void confirmBuyItem(View view) {
		// Confirm to sell
		// Send email
		sendEmail(view);
		// Push notification (almost will be done by server)
	}
	public void sendEmail(View view) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		// Put extra seller email
		String aEmailList[] = { "duyha@mit.edu" }; 
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
		// Put extra subject
		String subject = "I want to buy text book from you";
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		// Put extra content
		String content = "Hi, Thank you for selling this item";
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		// Start activity
		startActivity(emailIntent);
	}
	public void cancelBuyItem(View view) {
		Intent i = new Intent(view.getContext(), ItemDetail.class);
		startActivity(i);
	}
}
