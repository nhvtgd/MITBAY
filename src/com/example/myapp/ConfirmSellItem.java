package com.example.myapp;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmSellItem extends SellOneItem {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load data
		setContentView(R.layout.activity_confirm_sell_item);
		Bundle bundle = getIntent().getExtras();
		// Set Name item, category, condition
		((TextView) findViewById(R.id.ItemName)).setText(bundle.getString("item").toString());
		((TextView) findViewById(R.id.Category)).setText(bundle.getString("category").toString());
		((TextView) findViewById(R.id.Quality)).setText(bundle.getString("condition").toString());
		// Set price
		((TextView) findViewById(R.id.Price)).setText("$"+bundle.getString("price").toString());
		// Set description
		((TextView) findViewById(R.id.Description)).setText(bundle.getString("description").toString());
		// Set Seller
		SharedPreferences settings = getSharedPreferences("Setting", 0);
		((TextView) findViewById(R.id.Seller)).setText(
				String.format("%s %n%s %n%s",settings.getString("user name", "Duy Ha"), 
						settings.getString("email", "duyha@mit.edu"), 
						settings.getString("address", "Next house, #316")));
		// Set Images
		Bitmap picture = null;
		String status = "No picture";
		ImageView picView = (ImageView)findViewById(R.id.Piture);
		String imgPath = bundle.getString("imgPath").toString();
		int req_height =  1, req_width = bundle.getInt("widthPicture");
		picture = super.loadingBitmapEfficiently(imgPath, req_height, req_width);
		if (picture != null) status = ""; 
		picView.setImageBitmap(picture);
		((TextView) findViewById(R.id.Status)).setText(status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_confirm_sell_item, menu);
		return true;
	}
	public void cancelSellItem(View view) {
		// Go back to the main screen
		Intent i = new Intent(view.getContext(), ItemSelection.class);
		startActivity(i);
	}
	public void editSellItem(View view) {
		// Go back to the sell item screen
		Intent i = new Intent(view.getContext(), SellOneItem.class);
		i.putExtra("isEdit", true);
		startActivity(i);
	}
	public void confirmSellItem(View view) {
		
	}
}
