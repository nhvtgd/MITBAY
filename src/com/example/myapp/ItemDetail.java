package com.example.myapp;


import java.io.ByteArrayOutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.login.LogInPage;
import com.parse.ParseFile;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

public class ItemDetail extends MITBAYActivity {

	private String item, date, type, condition, price, description, username, email, address;
	private int id;
	private Bitmap image;
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
	 * Load picture from extras of the last activity
	 */
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
		id = bundle.getInt(ID, -1);
		// Set item name
		((TextView) findViewById(R.id.ItemDetail_ItemName))
		.setText(String.format("%s %n%s",item, date));
		// Set condition
		((TextView) findViewById(R.id.ItemDetail_Condition)).setText(condition);
		// Set price
		((TextView) findViewById(R.id.ItemDetail_Price))
		.setText(price);;
		// Set description
		((TextView) findViewById(R.id.ItemDetail_Description)).setText(description);
		// Get seller information
		String user_information = String.format("%s %n %s", username, email);
		((TextView) findViewById(R.id.ItemDetail_Seller)).
								setText(user_information);
		// Load category picture
		ImageView pic = (ImageView) findViewById(R.id.ItemDetail_ImageForItem);
		if (type.equals(TEXTBOOK)) pic.setImageResource(R.drawable.textbook);
		else if (type.equals(FURNITURE)) pic.setImageResource(R.drawable.furniture);
		else if (type.equals(TRANSPORTATION)) pic.setImageResource(R.drawable.bike);
		else pic.setImageResource(R.drawable.miscellaneous);
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
		intent.putExtra(IMAGE, image);
	}
	/**
	 * Do action buy item, need to check log in
	 * @param view
	 */
	public void buyOneItem(View view) {
		// Already log in
		if (isAlreadyLogIn()) {
			Intent i = new Intent(view.getContext(), ConfirmBuyItem.class);
			putExtras(i);
			startActivity(i);
		} else { 
			// Not yet, go back to Log in page
			// Build a dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Do you want to log in right now?");
			builder.setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(getApplicationContext(), LogInPage.class);
					startActivity(i);
				} 
			});
			builder.setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				} // do nothing
			});
			builder.create().show();
			Toast.makeText(view.getContext(), "You need to log in to buy items", Toast.LENGTH_SHORT).show();
		}
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
}
	
	
	
	
/* Need more work
	Theme
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