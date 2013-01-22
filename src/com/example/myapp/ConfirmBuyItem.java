package com.example.myapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmBuyItem extends MITBAYActivity {
	
	private String item, date, condition, price, description, username, email, type;
	private int id;
	private Bitmap image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_buy_item);
		// Loading information
		Bundle bundle = getIntent().getExtras();
		loadTextInformation(bundle);
	}
	
	public void loadPicture(Bundle bundle) {
		image = getIntent().getParcelableExtra(IMAGE);
//		String imgPath = bundle.getString("imgPath").toString();
//		Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
		Log.d("Image", (image==null) +", ");
		((ImageView) findViewById(R.id.ItemDetail_Piture)).setImageBitmap(image);
	}
	
	/**
	 * Load Item information from extras of the last activity
	 * @param bundle
	 */
	private void loadTextInformation(Bundle bundle) {
		// Load text
		item = bundle.getString(ITEM, "No named").toString();
		date = bundle.getString(DATE, "").toString();
		condition = bundle.getString(CONDITION, "").toString();
		price = bundle.getString(PRICE).toString();
		description = bundle.getString(DESCRIPTION, "").toString();
		username = bundle.getString(USERNAME, "Anonymous").toString();
		email = bundle.getString(EMAIL, "").toString();
		type = bundle.getString(TYPE, "Misc").toString();
		id = bundle.getInt(ID);
		// Set item name
		((TextView) findViewById(R.id.cbi_ItemName))
		.setText(String.format("%s %n%s",item, date));
		// Set condition
		((TextView) findViewById(R.id.cbi_Condition)).setText(condition);
		// Set price
		((TextView) findViewById(R.id.cbi_Price))
		.setText(price);;
		// Get seller information
		String user_information = String.format("%s %n %s", username, email);
		((TextView) findViewById(R.id.cbi_Seller)).
								setText(user_information);
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
		new CountDownTimer(2000, 2000) {
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
