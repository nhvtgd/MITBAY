package com.example.myapp;


import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.example.myapp.R;

public class ConfirmSellItem extends SellOneItem {

	private Activity activity;
	private String item, condition, price, description, type, username, email, address, password;
	Bitmap image;
	User user;
	Button confirm_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load data
		setContentView(R.layout.activity_confirm_sell_item);
		activity = this; 
		confirm_button = (Button) findViewById(R.id.Confirm);
		Bundle bundle = getIntent().getExtras();
		// Load information
		item = bundle.getString(ITEM).toString();
		type = bundle.getString(TYPE).toString();
		condition = bundle.getString(CONDITION).toString();
		price = bundle.getString(PRICE).toString();
		description = bundle.getString(DESCRIPTION).toString();
		// Set Name item, category, condition
		((TextView) findViewById(R.id.ItemName)).setText(item);
		((TextView) findViewById(R.id.Category)).setText(type);
		((TextView) findViewById(R.id.Quality)).setText(condition);
		((TextView) findViewById(R.id.Price)).setText("$"+price);
		((TextView) findViewById(R.id.Description)).setText(description);
		
		// Set Seller
		SharedPreferences settings = getSharedPreferences(SETTING, 0);
		username = settings.getString(USERNAME, "Anonymous");
		email = settings.getString(EMAIL, "No email available");
		address = settings.getString(ADDRESS, "");
		password = settings.getString(PASSWORD, "");
		user = new User(username, email, password);
		((TextView) findViewById(R.id.Seller)).setText(
				String.format("%s %n%s %n%s", username, email ,address));
		Log.d("Loading", "0");
		
		// Set Images
		image = null;
		String status = "No picture available";
		ImageView picView = (ImageView)findViewById(R.id.Picture);
		String imgPath = bundle.getString("imgPath").toString();
		image = loadingBitmapEfficiently(imgPath, WIDTH, HEIGHT);
		// Check size of image
		Log.d("images size",image.getByteCount()+", "+image.getWidth()+", "+image.getHeight());
		// Set Image
		if (image != null) status = "";
		else picView.setMinimumHeight(picView.getWidth());
		picView.setImageBitmap(image);
		((TextView) findViewById(R.id.Status)).setText(status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_confirm_sell_item, menu);
		return true;
	}
	
	/**
	 * Handle cancel button 
	 * @param view
	 * go to home page if user click Ok
	 */
	public void cancelSellItem(View view) {
		// Go back to the main screen
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Do you want to cancel to Sell this item");
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Need to confirm cancel to buy this item from server
				activity.finish();
				Intent i = new Intent(getApplicationContext(), ItemSelection.class);
				startActivity(i);
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { } // do nothing
		});
		builder.create().show();
	}
	/**
	 * Handle Edit button
	 * @param view
	 * go back if user click Ok
	 */
	public void editSellItem(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Do you want to cancel to buy this item");
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish(); 
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { } // do nothing
		});
		builder.create().show();
	}
	/**
	 * Handle confirm button
	 * @param view
	 * send to the server if user click Ok
	 */
	public void confirmSellItem(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Do you want to confirm to sell this item");
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "you just confirmed to sell", Toast.LENGTH_SHORT).show();
				confirm_button.setEnabled(false);
				// Create a Sellable object
				Sellable obj = new Sellable(user, item, price, type, description, condition, image);
				Log.d("Image == null", image.getByteCount()+"");
				// Send to server
				start = System.currentTimeMillis();
				Log.d("Sending server", ""+System.currentTimeMillis());
				ParseDatabase parse = new ParseDatabase(getApplicationContext());
				Log.d("create Parse","Ok");
				parse.sendSellableToServer(obj);
				end = System.currentTimeMillis();
				Log.d("Sent server", ""+System.currentTimeMillis());
				Log.d("Running time", ""+(end - start));
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { } // do nothing
		});
		builder.create().show();
	}
	
	/**
	 * This construct a Sellable object based on information that the seller fills out
	 * @return Sellable
	 */
	
}
