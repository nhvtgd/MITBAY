package com.example.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ConfirmBuyItem extends MITBAYActivity {

	private String item, date, condition, price, description, username, email, type, id;
	private Bitmap image;
	SharedPreferences settings;
	private Activity act;
	private int AFTER_SEND_EMAIL = 123456;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_buy_item);
		// Make start animation
		makeStartAnimation();
		act = this;
		// Loading information
		Bundle bundle = getIntent().getExtras();
		loadTextInformation(bundle);
		loadPicture(bundle);
	}

	public void loadPicture(Bundle bundle) {
		ImageView picView = (ImageView) findViewById(R.id.cbi_ItemPicture);
		image = getIntent().getParcelableExtra(IMAGE);
		//		String imgPath = bundle.getString("imgPath").toString();
		//		Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
		if (image == null) picView.setImageDrawable(getResources().getDrawable(R.drawable.ic_green_cart));
		else picView.setImageBitmap(image);
	}

	/**
	 * Load Item information from extras of the last activity
	 * @param bundle
	 */
	private void loadTextInformation(Bundle bundle) {
		// Load item, date, condition, price, description, username, email, type, id;
		item = bundle.getString(ITEM, "No named").toString();
		date = bundle.getString(DATE, "").toString();
		condition = bundle.getString(CONDITION, "").toString();
		price = bundle.getString(PRICE).toString();
		description = bundle.getString(DESCRIPTION, "").toString();
		username = bundle.getString(USERNAME, "Anonymous").toString();
		email = bundle.getString(EMAIL, "").toString();
		type = bundle.getString(TYPE, MISC).toString();
		id = bundle.getString(ID, null);
		// Set item name
		((TextView) findViewById(R.id.cbi_ItemName))
		.setText(String.format("%s %n%s",item, date));
		// Set condition
		((TextView) findViewById(R.id.cbi_Condition)).setText(condition);
		// Set price
		((TextView) findViewById(R.id.cbi_Price)).setText("$"+price);
		// Buyer information
		settings = getSharedPreferences(SETTING, 0);
		((TextView) findViewById(R.id.cbi_Buyer)).setText(String.format("%s %n%s %n%s",
				settings.getString(USERNAME, "").toString(),
				settings.getString(EMAIL, "").toString(), 
				settings.getString(ADDRESS, "").toString()));
		// Get seller information
		String seller_information = String.format("%s %n%s", username, email);
		((TextView) findViewById(R.id.cbi_Seller)).setText(seller_information);
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
		ParseQuery query = new ParseQuery("Sellable");
		query.getInBackground(id, new GetCallback() {
			@Override
			public void done(ParseObject obj, ParseException e) {
				if (e == null) {
					obj.put(ENABLE, false);
					obj.put(BUYER, username);
					obj.saveEventually();
				} else {
					Toast.makeText(getApplicationContext(), "Unfortunately, there are some error in server", Toast.LENGTH_SHORT).show();
				}
			}
		});
		// Visible progress
		((View) findViewById(R.id.confirm_buy_item_ProgressBar)).setVisibility(View.VISIBLE);
		// Remind sending email
		new CountDownTimer(1000, 1000) {
			public void onTick(long millisUntilFinished) {
				Toast.makeText(getApplicationContext(), "Sending to server", Toast.LENGTH_SHORT).show();
			}

			public void onFinish() {
				AlertDialog.Builder builder = new AlertDialog.Builder(act);
				builder.setTitle("Excellent! That would be great if you send email to seller!");
				builder.setPositiveButton("Ok", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Need to confirm cancel to buy this item from server
						sendEmail();
					}
				});
				builder.setNegativeButton("No. Thanks", null);
				builder.create().show();
			}
		}.start();

	}
	/**
	 * Send Email to the seller
	 */
	public void sendEmail() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		// Put extra seller email
		String aEmailList[] = { email }; 
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
		// Put extra subject
		String subject = "[Buying "+ item +" from you via MITBAY]";
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		// Put extra content
		String content = String.format("%s %n%s",
				"Hi "+settings.getString(NAME,  "") +",",
				"Thank you for selling this item. I would like to buy "
						+ item +" from you");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		// Start activity
		startActivityForResult(emailIntent, AFTER_SEND_EMAIL);
	}
	
	/**
	 * Come back the the main screen
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AFTER_SEND_EMAIL) {
			Toast.makeText(getApplicationContext(), "you just bought item "+item, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), CustomizedListView.class);
			intent.putExtra("query", ItemSelection.ALL);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
	/**
	 * Cancel to buy, come back to the home screen (ItemSelection)
	 * @param view
	 */
	public void cancelBuyItem(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Do you want to cancel to buy this item");
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Need to confirm cancel to buy this item from server
				Intent i = new Intent(getApplicationContext(), ItemSelection.class);
				startActivity(i);
				act.finish();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { } // do nothing
		});
		builder.create().show();

	}

	private void makeStartAnimation() {
		ViewGroup frame = (ViewGroup) findViewById(R.id.confirm_buy_item_Frame);
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_animation_item_detail);
		for (int i=0; i<frame.getChildCount(); i++) {
			View child = frame.getChildAt(i);
			child.setAnimation(animation);
		}
		animation.start();
	}
}


// Make Theme
// Make Dialog button more attractive
// Send them back the main screen
