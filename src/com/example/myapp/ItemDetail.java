package com.example.myapp;


import android.app.ActionBar;
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

import com.example.myapp.helper.AlertDialogManager;
import com.example.myapp.helper.ConnectionDetector;
import com.login.LogInPage;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ItemDetail extends MITBAYActivity {

	private String item, date, type, condition, price, description, username, email, address, id;
	private Bitmap image;
	private ImageView picView;
	private TextView status;
	private boolean isEdit;
	private Intent intent;
	private ConnectionDetector connection = new ConnectionDetector(this);
	
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
		setContentView(R.layout.activity_item_detail);
		// set navigating icon
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		// make start animation
		makeStartAnimation();
		// Loading information
		picView = (ImageView) findViewById(R.id.ItemDetail_Piture);
		status = (TextView) findViewById(R.id.ItemDetail_status);
		Bundle bundle = getIntent().getExtras();
		loadTextInformation(bundle);
		loadPicture(bundle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_item_detail, menu);
		return true;
	}

	/**
	 * Load Item information from extras of the last activity
	 * @param bundle
	 */
	private void loadTextInformation(Bundle bundle) {
		// is that edit?
		isEdit = bundle.getBoolean(EDIT, false);
		if (isEdit) {
			// change the name of the button
			Button confirm_buy = (Button) findViewById(R.id.ItemDetail_Buy);
			confirm_buy.setText("Cancel to buy");
		}
		// Load text
		item = bundle.getString(ITEM, "No named").toString();
		date = bundle.getString(DATE, "").toString();
		condition = bundle.getString(CONDITION, "").toString();
		price = bundle.getString(PRICE).toString();
		description = bundle.getString(DESCRIPTION, "").toString();
		username = bundle.getString(USERNAME, "Anonymous").toString();
		email = bundle.getString(EMAIL, "").toString();
		type = bundle.getString(TYPE, "Misc").toString();
		id = bundle.getString(ID, "");
		// Set item name
		((TextView) findViewById(R.id.ItemDetail_ItemName))
		.setText(String.format("%s %n%s",item, date));
		// Set condition
		((TextView) findViewById(R.id.ItemDetail_Condition)).setText(condition);
		// Set price
		((TextView) findViewById(R.id.ItemDetail_Price))
		.setText("$"+price);;
		// Set description
		((TextView) findViewById(R.id.ItemDetail_Description)).setText(description);
		// Get seller information
		String user_information = String.format("%s %n %s", username, email);
		((TextView) findViewById(R.id.ItemDetail_Seller)).
		setText(user_information);
		// Load category picture
		ImageView pic = (ImageView) findViewById(R.id.ItemDetail_ImageForItem);
		if (type.equals(TEXTBOOK)) pic.setImageResource(R.drawable.text_book);
		else if (type.equals(FURNITURE)) pic.setImageResource(R.drawable.furniture_icon);
		else if (type.equals(TRANSPORTATION)) pic.setImageResource(R.drawable.bike);
		else pic.setImageResource(R.drawable.misc);
	}

	/**
	 * Load picture from extras of the last activity
	 * Up date status
	 */
	public void loadPicture(Bundle bundle) {
		if (!connection.isConnectingToInternet()) {
			new AlertDialogManager().showAlertDialog(this, NETWORK_ERROR_TITLE,
					NETWORK_ERROR_MESSAGE, false);
			return ;
		}
		image = null;
		id = bundle.getString(ID, "");
		// Parse object load small image
		ParseQuery query = new ParseQuery("Sellable");
		query.getInBackground(id, new GetCallback() {
			@Override
			public void done(ParseObject arg0, ParseException arg1) {
				if (arg1 == null){
					ParseFile file = (ParseFile) arg0.get("pic");
					file.getDataInBackground(new GetDataCallback(){
						public void done(byte[] data, ParseException e){
							if (e == null){
								loadPictureFromByteArray(data);
							} else loadPictureFromByteArray(null);	
						}
					});
				}
				ParseObject bigpicObj = (ParseObject) arg0.get("bigpic");

				bigpicObj.fetchIfNeededInBackground(new GetCallback() {
					public void done(ParseObject obj, ParseException e){
						Toast.makeText(getApplicationContext(), ""+obj.isDataAvailable(), Toast.LENGTH_SHORT).show();
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
				//				ParseFile file = (ParseFile)bigpicObj.get("pic");
				//				file.getDataInBackground(new GetDataCallback(){
				//					public void done(byte[] data, ParseException e){
				//						if (e == null){
				//							loadPictureFromByteArray(data);
				//						} else loadPictureFromByteArray(null);
				//					}
				//				});
			}
		});
		// Load big image

	}
	public void loadPictureFromByteArray(byte [] data) {
		if (data == null) {
			image = null;
			return; }
		image = BitmapFactory.decodeByteArray(data, 0, data.length);
		picView.setImageBitmap(image);
		if (image == null) status.setText("No picture available");
		else status.setText("");
	}

	/**
	 * Put extra information for the next activity
	 * @param intent
	 */
	private void putExtras(Intent intent) {
		// Put extras item, date, condition, price, description, username, email, type, id;
		intent.putExtra(ITEM, item);
		intent.putExtra(DATE, date);
		intent.putExtra(CONDITION, condition);
		intent.putExtra(PRICE, price);
		intent.putExtra(DESCRIPTION, description);
		intent.putExtra(USERNAME, username);
		intent.putExtra(EMAIL, email);
		intent.putExtra(TYPE, type);
		intent.putExtra(ID, id);
	}
	/**
	 * Do action buy item, need to check log in
	 * @param view
	 */
	public void buyOneItem(View view) {
		if (isEdit) {
			cancelToBuy();
			return ; }
		// Already log in
		if (isAlreadyLogIn()) {
			intent = new Intent(view.getContext(), ConfirmBuyItem.class);
			putExtras(intent);
			startActivity(intent);
		} else { 
			// Not yet, go back to Log in page
			// Build a dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Dear user");
			builder.setMessage("Do you want to log in right now?");
			builder.setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					intent = new Intent(getApplicationContext(), LogInPage.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} 
			});
			builder.setNegativeButton("Cancel", null);
			builder.create().show();
			Toast.makeText(view.getContext(), "You need to log in to buy items", Toast.LENGTH_SHORT).show();
		}
	}

	public void cancelToBuy() {
		intent = new Intent(getApplicationContext(), BuyingItems.class);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Dear "+ username);
		builder.setMessage("Do you want to cancel to buy this item?");
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Enable object
				ParseQuery query = new ParseQuery("Sellable");
				query.getInBackground(id, new GetCallback() {
					@Override
					public void done(ParseObject obj, ParseException e) {
						if (e == null) {
							Log.d("set enable ", "ok");
							obj.put(ENABLE, true);
							Log.d("set buyer ", "ok");
							obj.put(BUYER, "");
							obj.saveInBackground();
							Log.d("all set ", "ok");
						} else {
							Toast.makeText(getApplicationContext(), "Unfortunately, there are some error in server", Toast.LENGTH_SHORT).show();
						}
						startActivity(intent);
					}
				});
			} 
		});
		builder.setNegativeButton("Cancel", null);
		builder.create().show();
	}

	/**
	 * Check log in
	 * @return 	true if already log in
	 * 			false, otherwise
	 */
	public boolean isAlreadyLogIn() {
		SharedPreferences settings = getSharedPreferences(SETTING, 0);
		return settings.getBoolean(IS_ALREADY_LOG_IN, false);
	}

	/**
	 * Make start animation slide from right to left
	 */
	private void makeStartAnimation() {
		ViewGroup frame = (ViewGroup) findViewById(R.id.ItemDetail_Frame);
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_animation_item_detail);
		for (int i=0; i<frame.getChildCount(); i++) {
			View child = frame.getChildAt(i);
			child.setAnimation(animation);
		}
		animation.start();
	}
}




/* Need more work
	first load small picture, later load big picture
 */






//	File file = new File(Environment.getExternalStorageDirectory(), "small_buy.png");
//	Uri outputFileUri = Uri.fromFile(file);
//	String imgPath = outputFileUri.getPath();
//	try {
//		FileOutputStream fOut = new FileOutputStream(file);
//		item.getImages().compress(Bitmap.CompressFormat.PNG, 50, fOut);
//		fOut.flush();
//		fOut.close();
//	} catch (Exception e) {}
//	intent.putExtra("imgPath", imgPath);
//}