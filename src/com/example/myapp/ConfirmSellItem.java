package com.example.myapp;


import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
		// Make animation
		makeStartAnimation();
		// loading data
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
		address = settings.getString(LOCATION, "");
		password = settings.getString(PASSWORD, "");
		user = new User(username, email, password);
		((TextView) findViewById(R.id.Seller)).setText(
				String.format("%s %n%s %n%s", username, email ,address));
		// Set Images
		loadImage(bundle);
	}
	
	/**
	 * Load image, if image is null, assign it as one default image
	 * @param bundle
	 */
	private void loadImage(Bundle bundle) {
		image = null;
		String status = "Default picture";
		ImageView picView = (ImageView)findViewById(R.id.Picture);
		String imgPath = bundle.getString("imgPath").toString();
		image = loadingBitmapEfficiently(imgPath, WIDTH, HEIGHT);
		// Set Image
		if (image != null) status = "";
		else image = setDefaultImage(type);
		picView.setMinimumHeight(picView.getWidth());
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
	 * Load default image by type
	 * @param type
	 * @return
	 */
	private Bitmap setDefaultImage(String type) {
		if (type.equals(R.string.textbook_string)) {
			return BitmapFactory.decodeResource(getResources(), R.drawable.text_book);
		} else if (type.equals(R.string.furniture_string)) {
			return BitmapFactory.decodeResource(getResources(), R.drawable.furniture);
		} else if (type.equals(R.string.transportation_string)) {
			return BitmapFactory.decodeResource(getResources(), R.drawable.bike);
		} else return BitmapFactory.decodeResource(getResources(), R.drawable.miscellaneous);
	}
	
	/** 
	 * Make a start animation slide both left and right
	 */
	private void makeStartAnimation() {
		ViewGroup frame = (ViewGroup) findViewById(R.id.confirm_sell_item_Frame);
		Animation right_to_mid = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_animation_translate_right_to_mid);
		Animation left_to_mid = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_animation_translate_left_to_mid);
		for (int i=0; i<frame.getChildCount()/2; i++) {
			View child = frame.getChildAt(i);
			child.setAnimation(left_to_mid); }
		for (int i=frame.getChildCount()/2; i<frame.getChildCount(); i++) {
			View child = frame.getChildAt(i);
			child.setAnimation(right_to_mid); }
		right_to_mid.start();
		left_to_mid.start();
	}
	
}
