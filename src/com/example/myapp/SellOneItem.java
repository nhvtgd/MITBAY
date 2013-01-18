package com.example.myapp;
import static android.provider.MediaStore.Images.Media.getBitmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SellOneItem extends Activity {
	
	// Keep track of camera request
	private final int CAMERA_PIC_REQUEST = 314156;
	// Keep track of choosing photo from storage
	final int PICK_PHOTO = 271828;
	// Save multiple images
	List<Bitmap> IMAGES = new ArrayList<Bitmap>();
	// Options for picture
	final String[] options = {"Set Avarta", "Delete"};
	final int MAX_NUMBER_PICTURES = 3;
	int current_photo_index = -1;
	int avarta_photo_index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell_one_item);
		// Need to load data from user: Name, email, address
		setStatus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.activity_sell_one_item, menu);
		return true;
	}
	// Take Photo
	public void takePhoto(View view) {
		if (IMAGES.size() >= MAX_NUMBER_PICTURES) {
			overPictures(); 
			return;
		}
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST); 
	}

	// Chose photo from directory
	public void chosePicture(View view) {
		if (IMAGES.size() >= MAX_NUMBER_PICTURES) {
			overPictures(); 
			return;
		}
		Toast.makeText(getApplicationContext(), "Browers", Toast.LENGTH_SHORT).show();
		Intent pickPhoto = new Intent(Intent.ACTION_PICK);
		pickPhoto.setType("image/*");
		startActivityForResult(pickPhoto, PICK_PHOTO);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			if (resultCode == Activity.RESULT_CANCELED) return;
			//get the returned data
			Bundle extras = data.getExtras();
			
			//get the cropped bitmap
			Bitmap thePic = extras.getParcelable("data");
			//retrieve a reference to the ImageView
			ImageView picView = (ImageView)findViewById(R.id.sell_one_item_Piture);
			//display the returned image
			picView.setImageBitmap(thePic);
			IMAGES.add(thePic);
			current_photo_index += 1;
			setStatus();
			Toast.makeText(this, "Bitmap has "+IMAGES.size()+" pictures", Toast.LENGTH_SHORT).show();
		} else if (requestCode == PICK_PHOTO) {
			if (resultCode == Activity.RESULT_CANCELED) return;
			Uri photoUri = data.getData();
			Bitmap thePic = null;
			try {
				thePic = getBitmap(this.getContentResolver(), photoUri);
			} catch (IOException e) {}
			ImageView picView = (ImageView)findViewById(R.id.sell_one_item_Piture);
			picView.setImageBitmap(thePic);
			IMAGES.add(thePic);
			current_photo_index += 1;
			setStatus();
			Toast.makeText(this, "Bitmap has "+IMAGES.size()+" pictures", Toast.LENGTH_SHORT).show();
		}		
	}
	// Click on the picture
	// there are 2 options: set main picture,delete
	public void pictureOptions(View view) {
		if (IMAGES.size() == 0) return;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Options");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: avartaPicture();break;
				case 1: deletePicture(); break;
				}
			}
		});
		builder.create().show();
	}
	// Over maximum number of pictures
	protected void overPictures() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("You have at most "+MAX_NUMBER_PICTURES+" pictures for each item");
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			} // do nothing
		});
		builder.create().show();
	}
	// Display the next picture
	public void nextPicture(View view) {
		if (IMAGES.size() == 0) return;
		current_photo_index = (current_photo_index+1) % IMAGES.size();
		setStatus();
	}
	// Display the previous picture 
	public void previousPicture(View view) {
		if (IMAGES.size() == 0) return;
		current_photo_index = (current_photo_index+IMAGES.size()-1) % IMAGES.size();
		setStatus();
	}
	public void deletePicture() {
		if (IMAGES.size() == 0) return;
		IMAGES.remove(current_photo_index);
		if (avarta_photo_index == current_photo_index)
			// change avata_photo_index
			avarta_photo_index = 0;
		if (current_photo_index > IMAGES.size()-1)
			current_photo_index = IMAGES.size()-1;
		setStatus();
	}
	public void avartaPicture() {
		avarta_photo_index = current_photo_index;
		setStatus();
	}
	public void setStatus() {
		TextView textStatus = (TextView) findViewById(R.id.sell_one_item_Status);
		ImageView picView = (ImageView)findViewById(R.id.sell_one_item_Piture);
		ImageButton next = (ImageButton) findViewById(R.id.sell_one_item_NextPicture);
		ImageButton previous = (ImageButton) findViewById(R.id.sell_one_item_PreviousPicture);
		Bitmap thePic;
		
		// Set View picture
		if (IMAGES.size()==0) {
			thePic = null;
			LinearLayout framePicture = (LinearLayout) findViewById(R.id.sell_one_item_FramePicture);
			framePicture.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
		}
		else thePic = IMAGES.get(current_photo_index);
		picView.setImageBitmap(thePic);
		
		// Set visibility of next and previous buttons, and status bar
		if (IMAGES.size() >= 2) {
			// Set navigation bar visible
			next.setVisibility(ImageButton.VISIBLE);
			previous.setVisibility(ImageButton.VISIBLE);
			// Set status visible
			textStatus.setText(""+(current_photo_index+1)+"/"+IMAGES.size());
		} else {
			// Set navigation bars and textStatus invisible if there are less than 2 pictures
			next.setVisibility(ImageButton.INVISIBLE);
			previous.setVisibility(ImageButton.INVISIBLE);
			textStatus.setText("");
		}
	}
	/**
	 * Check if a string is a valid number
	 * @param priceString
	 * @return
	 */
	public boolean isValidPrice(String priceString) {
		try {
			double price = Double.parseDouble(priceString);
		} catch (NumberFormatException nfe) {
			return false; }
		return true;
	}
	// Button Cancel and Done
	public void cancelSellTheItem(View view) {
		Toast.makeText(view.getContext(), "will cancel", Toast.LENGTH_SHORT).show();
	}
	public void doneSellTheItem(View view) {
		Toast.makeText(view.getContext(), "will be continued", Toast.LENGTH_SHORT).show();
		String priceString = ((EditText) findViewById(R.id.sell_one_item_Price)).getText().toString();
		if (!isValidPrice(priceString)) {
			// Build a dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("The price "+priceString+" you suggest is not valid");
			builder.setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				} // do nothing
			});
			builder.create().show();
		}
		// else { create Sellable object/ confirm / send to the server}
	}
}

