package com.example.myapp;


import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.login.LogInPage;

public class ItemDetail extends MITBAYActivity {

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
	 * Load picture from extras of the last axtivity
	 */
	public void loadPicture(Bundle bundle) {
//		Bitmap bitmap = getIntent().getParcelableExtra(IMAGE);
		String imgPath = bundle.getString("imgPath").toString();
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
		Log.d("Image", (bitmap==null) +", "+imgPath);
		((ImageView) findViewById(R.id.ItemDetail_Piture)).setImageBitmap(bitmap);
	}
	/**
	 * Load Item information from extras of the last activity
	 * @param bundle
	 */
	public void loadTextInformation(Bundle bundle) {
		// Item name
		((TextView) findViewById(R.id.ItemDetail_ItemName))
		.setText(String.format("%s %n%s",
				bundle.getString(ITEM, "No named").toString(),
				bundle.getString(DATE, "")).toString());
		// Category
		((TextView) findViewById(R.id.ItemDetail_Category))
		.setText(bundle.getString(CONDITION, "").toString());
		// Price
		((TextView) findViewById(R.id.ItemDetail_Price))
		.setText(bundle.getString(PRICE).toString());;
		// Description
		((TextView) findViewById(R.id.ItemDetail_Description))
		.setText(bundle.getString(DESCRIPTION, "").toString());
		// Get seller information
		String user_information = String.format("%s %n %s",
				bundle.getString(USERNAME, "Anonymous").toString(),
				bundle.getString(EMAIL, "No email available").toString());
		((TextView) findViewById(R.id.ItemDetail_Seller)).
								setText(user_information);
		// Load category
		String type = bundle.getString(TYPE).toString();
		ImageView pic = (ImageView) findViewById(R.id.ItemDetail_ImageForItem);
		if (type.equals(TEXTBOOK)) pic.setImageResource(R.drawable.textbook);
		else if (type.equals(FURNITURE)) pic.setImageResource(R.drawable.furniture);
		else if (type.equals(TRANSPORTATION)) pic.setImageResource(R.drawable.bike);
		else pic.setImageResource(R.drawable.miscellaneous);
	}
	
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