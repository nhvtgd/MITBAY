package com.example.myapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmSellItem extends Activity {
	
	// Images for the item
	ArrayList<Bitmap> IMAGES;
	int current_photo_index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load data
		setContentView(R.layout.activity_confirm_sell_item);
		Bundle bundle = getIntent().getExtras();
		int i = 0;
		// Set Name item, category, condition
		((TextView) findViewById(R.id.ItemName)).setText(bundle.getString("item").toString());
		((TextView) findViewById(R.id.Category)).setText(bundle.getString("category").toString());
		((TextView) findViewById(R.id.Quality)).setText(bundle.getString("condition").toString());
		// Set price
		((TextView) findViewById(R.id.Price)).setText(bundle.getString("price").toString());
		// Set description
		((TextView) findViewById(R.id.Description)).setText(bundle.getString("description").toString());
		// Set Seller
		SharedPreferences settings = getSharedPreferences("Setting", 0);
		((TextView) findViewById(R.id.Seller)).setText(
				String.format("%s %n%s %n%s",settings.getString("user name", "Duy Ha"), 
										settings.getString("email", "duyha@mit.edu"), 
										settings.getString("password", "lifeisbeautiful")));
		// Set Images
//		Bitmap avarta = (Bitmap) bundle.get("avarta");
//		((ImageView) findViewById(R.id.Piture)).setImageBitmap(avarta);
		IMAGES = bundle.getParcelableArrayList("IMAGES");
		setView();
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
	public void goPreviousPicture(View view) {
		Toast.makeText(view.getContext(), "will go previous pic", Toast.LENGTH_SHORT).show();
		current_photo_index = (current_photo_index+IMAGES.size()-1) % IMAGES.size();
		setView();
	}
	public void goNextPicture(View view) {
		Toast.makeText(view.getContext(), "will go next pic", Toast.LENGTH_SHORT).show();
		current_photo_index += 1;
		setView();
	}
	public void setView() {
		ImageView image = (ImageView) findViewById(R.id.Piture);
		Bitmap pic = null;
		TextView textStatus = (TextView) findViewById(R.id.status);
		// Set Image view
		if (IMAGES.size() == 0) {
			textStatus.setText("No picture");
		} else {
			textStatus.setText(current_photo_index+"/"+IMAGES.size());
			pic = IMAGES.get(current_photo_index);
		}
		image.setImageBitmap(pic);
		// Set Button next and previous
		if (IMAGES.size() < 2) {
			((ImageButton)findViewById(R.id.nextPicture)).setVisibility(ImageButton.INVISIBLE);
			((ImageButton)findViewById(R.id.previousPicture)).setVisibility(ImageButton.INVISIBLE);
		}
	}
}
