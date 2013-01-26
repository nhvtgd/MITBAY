
package com.example.myapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
	/**
	 * Handle click confirm to buy
	 * 	send email
	 *  send to server
	 * @param view
	 */
	public void confirmBuyItem(View view) {
		// disable button
		((Button) findViewById(R.id.cbi_ConfirmBuy)).setEnabled(false);
		// Send to server
		
		// Visible progress
		((LinearLayout) findViewById(R.id.cbi_Progress)).setVisibility(LinearLayout.VISIBLE);
		// Remind sending email
		new CountDownTimer(3000, 3000) {
		     public void onTick(long millisUntilFinished) {
		         Toast.makeText(getApplicationContext(), "Sending to server", Toast.LENGTH_SHORT).show();
		     }

		     public void onFinish() {
		    	 sendEmail();
		     }
		  }.start();
		 
	}
	/**
	 * Send Email to the seller
	 */
	public void sendEmail() {
		((LinearLayout) findViewById(R.id.cbi_Progress)).setVisibility(LinearLayout.INVISIBLE);
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
	/**
	 * Cancel to buy, come back to the home screen
	 * @param view
	 */
	public void cancelBuyItem(View view) {
		Intent i = new Intent(view.getContext(), ItemDetail.class);
		startActivity(i);
	}
}
