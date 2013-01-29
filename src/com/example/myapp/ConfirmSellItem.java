package com.example.myapp;


import android.app.ActionBar;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ConfirmSellItem extends SellOneItem {

	private Activity activity;
	private String item, condition, price, description, type, username, email, address, password, id;
	private Bitmap image;
	private User user;
	private Button confirm_button;
	private SharedPreferences settings;
	private boolean isEdit = false, isDoneEdit =false; 
	private ImageView picView;
	private TextView Status;
	private Intent intent;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case (android.R.id.home):
			Intent intent = new Intent(this, ItemSelection.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load data
		setContentView(R.layout.activity_confirm_sell_item);
		// set navigating icon
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		activity = this; 
		confirm_button = (Button) findViewById(R.id.Confirm);
		// Make animation
		makeStartAnimation();
		// loading data
		Bundle bundle = getIntent().getExtras();
		// Load information
		if (getIntent().hasExtra(EDIT)) isEdit = bundle.getBoolean(EDIT);
		else isEdit = false;
		if (getIntent().hasExtra(DONE_EDIT)) isDoneEdit = bundle.getBoolean(DONE_EDIT);
		else isDoneEdit = false;
		id = bundle.getString(ID, "");
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
		settings = getSharedPreferences(SETTING, 0);
		username = settings.getString(USERNAME, "Anonymous");
		email = settings.getString(EMAIL, "No email available");
		address = settings.getString(LOCATION, "");
		password = settings.getString(PASSWORD, "");
		user = new User(username, email, password);
		((TextView) findViewById(R.id.Seller)).setText(
				String.format("%s %n%s %n%s", username, email ,address));
		// Set Images
		Status = (TextView) findViewById(R.id.Status);
		picView = (ImageView)findViewById(R.id.Picture);
		Log.d("is edit?", ""+(isEdit));
		Log.d("is done edit?", ""+isDoneEdit);
		if (!isEdit || isDoneEdit) {
			Log.d("loading picture from bundle", "ok");
			loadImage(bundle);
		}
		else  {
			Log.d("Doing crazy with editing", "");
			loadImageFromServer(bundle); 
			TextView greeting  = (TextView) findViewById(R.id.confirm_sell_item_greeting);
			greeting.setText("Hi "+ username +", You are selling the following item");
			Button confirm = (Button) findViewById(R.id.Confirm);
			if (!isDoneEdit) {
				confirm.setEnabled(false);
				confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_disabled_holo_dark));
			}
		}
	}

	/**
	 * Load image, if image is null, assign it as one default image
	 * @param bundle
	 */
	private void loadImage(Bundle bundle) {
		image = null;
		String status = "Default picture";
		String imgPath = bundle.getString(IMAGE_PATH);
		image = loadingBitmapEfficiently(imgPath, WIDTH, HEIGHT);
		// Set Image
		type = bundle.getString(TYPE).toString();
		if (image != null) status = "";
		else image = setDefaultImage();
		picView.setMinimumHeight(picView.getWidth());
		picView.setImageBitmap(image);
		Status.setText(status);
	}

	/**
	 *  Load image from server
	 */
	public void loadImageFromServer(Bundle bundle) {
		image = null;
		id = bundle.getString(ID, "");
		// Parse object load small image
		Log.d("create query", "ok");
		ParseQuery query = new ParseQuery("Sellable");
		query.getInBackground(id, new GetCallback() {
			@Override
			public void done(ParseObject arg0, ParseException arg1) {
				if (arg1 == null){
					Log.d("arg1", "ok, not null");
					ParseFile file = (ParseFile) arg0.get("pic");
					file.getDataInBackground(new GetDataCallback(){
						public void done(byte[] data, ParseException e){
							if (e == null){
								loadPictureFromByteArray(data);
							} else loadPictureFromByteArray(null);	
						}
					});
				}
				Log.d("create big pic", "ok");
				ParseObject bigpicObj = (ParseObject) arg0.get("bigpic");

				bigpicObj.fetchIfNeededInBackground(new GetCallback() {
					public void done(ParseObject obj, ParseException e){
						ParseFile file = (ParseFile) obj.get("pic");
						file.getDataInBackground(new GetDataCallback(){
							public void done(byte[] data, ParseException e){
								if (e == null){
									loadPictureFromByteArray(data);
								} else loadPictureFromByteArray(null);
							}
						});
					}
				});
			}
		});
		Log.d("finish", "ok");
	}

	public void loadPictureFromByteArray(byte [] data) {
		if (data == null) {
			image = null;
			return; }
		image = BitmapFactory.decodeByteArray(data, 0, data.length);
		picView.setImageBitmap(image);
		if (image == null) Status.setText("No picture available");
		else Status.setText("");
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
				if (!isEdit) {
					Intent i = new Intent(getApplicationContext(), ItemSelection.class);
					startActivity(i);
					activity.finish();

				} else {
					ParseQuery query = new ParseQuery("Sellable");
					query.getInBackground(id, new GetCallback() {
						@Override
						public void done(ParseObject obj, ParseException e) {
							// Cancel object
							obj.deleteInBackground();
							Toast.makeText(getApplicationContext(), "You have deleted the item", Toast.LENGTH_LONG).show();
							intent = new Intent(getApplicationContext(), SellingItems.class);
							startActivity(intent);
							activity.finish();
						}
					});
				}
			}
		});
		builder.setNegativeButton("No", null);
		builder.create().show();
	}

	/**
	 * Handle Edit button
	 * @param view
	 * go back if user click Ok
	 */
	public void editSellItem(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Do you want to edit this item?");
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (isEdit) {
					intent = new Intent(getApplicationContext(), SellOneItem.class);
					// Put EDIT and ID for the next screen
					intent.putExtra(EDIT, true);
					intent.putExtra(ID, id);
					startActivity(intent);
					activity.finish();
				} else activity.finish();
			}
		});
		builder.setNegativeButton("No", null);
		builder.create().show();
	}
	/**
	 * Handle confirm button
	 * @param view
	 * send to the server if user click Ok
	 */
	public void confirmSellItem(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Do you want to confirm to sell this item?");
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "you just confirmed to sell", Toast.LENGTH_SHORT).show();
				confirm_button.setEnabled(false);
				// Create a Sellable object
				Sellable obj = new Sellable(user, item, price, type, description, condition, image);
				SharedPreferences settings = getSharedPreferences(SETTING, 0);
				String location = settings.getString(LOCATION, CONTACT_SELLER);
				obj.setLocation(location);
				Log.d("Image == null", image.getByteCount()+"");
				// If is editing, delete object
				if  (isDoneEdit) {
					ParseQuery query = new ParseQuery("Sellable");
					query.getInBackground(id, new GetCallback() {
						@Override
						public void done(ParseObject obj, ParseException e) {
							// Cancel object
							obj.deleteInBackground();
							Toast.makeText(getApplicationContext(), "You have deleted the item", Toast.LENGTH_LONG).show();
						}
					});
				}
				// Send to server
				ParseDatabase parse = new ParseDatabase(getApplicationContext());
				parse.sendSellableToServer(obj);
				Log.d("Sent server", ""+System.currentTimeMillis());
				Log.d("Running time", ""+(end - start));
				intent = new Intent(getApplicationContext(), CustomizedListView.class);
				intent.putExtra("query", ItemSelection.ALL);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("No", null);
		builder.create().show();
	}


	/**
	 * Load default image by type
	 * @param type
	 * @return
	 */
	private Bitmap setDefaultImage() {
		if (type.equals(TEXTBOOK)) {
			return BitmapFactory.decodeResource(getResources(), R.drawable.text_book);
		} else if (type.equals(FURNITURE)) {
			return BitmapFactory.decodeResource(getResources(), R.drawable.furniture_icon);
		} else if (type.equals(TRANSPORTATION)) {
			return BitmapFactory.decodeResource(getResources(), R.drawable.bike);
		} else {
			return BitmapFactory.decodeResource(getResources(), R.drawable.misc);
		}
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
