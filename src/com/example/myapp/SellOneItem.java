package com.example.myapp;
import static android.graphics.BitmapFactory.decodeFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Sellable.Condition;
import com.example.myapp.Sellable.SellType;

public class SellOneItem extends Activity {
	// Keep track of camera request
	private final int CAMERA_PIC_REQUEST = 314156;
	// Keep track of choosing photo from storage
	final int PICK_PHOTO = 271828;
	// Save multiple images
	public ArrayList<Bitmap> IMAGES = new ArrayList<Bitmap>();
	// Options for picture
	final String[] options = {"Set Avarta", "Delete"};
	final int MAX_NUMBER_PICTURES = 3;
	int current_photo_index = -1;
	int avarta_photo_index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell_one_item);
		loadSettingData(); 	// Load setting data from user
		setStatus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.activity_sell_one_item, menu);
		return true;
	}
	// Load data from User: name, email, address
	public void loadSettingData() {
		// GetSharedPreferences
		SharedPreferences settings = getSharedPreferences("Setting", 0);
		String userName = settings.getString("user name", "DUY HA");
		String email = settings.getString("email", "duyha@MIT.EDU");
		String address = settings.getString("address", "Next house, room 316");
		// Set Text View
		((TextView) findViewById(R.id.sell_one_item_UserName)).setText(userName);
		((TextView) findViewById(R.id.sell_one_item_Email)).setText(email);
		((TextView) findViewById(R.id.sell_one_item_Address)).setText(address);
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
			return; }
		Intent pickPhoto = new Intent(Intent.ACTION_PICK);
		pickPhoto.setType("image/*");
		startActivityForResult(pickPhoto, PICK_PHOTO);
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
			thePic = BitmapFactory.decodeResource(getResources(), R.drawable.bike);
		} else {
			thePic = IMAGES.get(current_photo_index);
		}
		int width = picView.getWidth();
		picView.setMinimumHeight(width);
		picView.setMaxHeight(width);
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
			if (IMAGES.size() == 1) textStatus.setText("");
			else textStatus.setText("no picture in description");

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
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("The price \""+priceString+"\" you suggest is not valid");
			builder.setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				} // do nothing
			});
			builder.create().show();
			return false; }
		return true;
	}
	// Button Cancel and Done
	public void cancelSellTheItem(View view) {
		// Go back to the ItemSelection
		startActivity(new Intent(view.getContext(), ItemSelection.class));
	}
	public void doneSellTheItem(View view) {
		Toast.makeText(view.getContext(), "will be continued", Toast.LENGTH_SHORT).show();
		String priceString = ((EditText) findViewById(R.id.sell_one_item_Price)).getText().toString();
		if (!isValidPrice(priceString)) return;
		// else { create Sellable object/ send to the server}
		// Create a Sellable object
		Sellable obj = createSellableObject();
		// Send to server
		// (new ParseDatabase(view.getContext())).sendSellableToServer(obj);
		// Create intent for the next activity
		Intent intent = new Intent(view.getContext(), ConfirmSellItem.class);
		// Put Images
		intent.putExtra("IMAGES", obj.getImages());
		// Put item, price, condition, category, description
		intent.putExtra("item", obj.getName());
		intent.putExtra("price", obj.getPrice());
		intent.putExtra("condition", ((Spinner)findViewById(R.id.sell_one_item_Quality)).getSelectedItem().toString().toUpperCase());
		intent.putExtra("category", ((Spinner)findViewById(R.id.sell_one_item_Category)).getSelectedItem().toString().toUpperCase());
		intent.putExtra("description", obj.getDescription());
		startActivity(intent);
	}

	/**
	 * This construct a Sellable object based on information that the seller fills out
	 * @return Sellable
	 */
	public Sellable createSellableObject() {
		// User
		SharedPreferences settings = getSharedPreferences("Setting", 0);
		User user = new User(settings.getString("user name", "Duy Ha"), 
				settings.getString("email", "duyha@mit.edu"), 
				settings.getString("password", "thelife"));
		// Item
		String item = ((EditText)findViewById(R.id.sell_one_item_Item)).getText().toString();
		// Price
		String price = ((EditText)findViewById(R.id.sell_one_item_Price)).getText().toString();
		// Condition
		String stringCondition = ((Spinner)findViewById(R.id.sell_one_item_Quality)).getSelectedItem().toString().toUpperCase();
		Condition condition = null;
		for (Condition c: Condition.values()) 
			if (stringCondition.equals(c.name())) condition = c;
		// Category
		String stringCategory = ((Spinner)findViewById(R.id.sell_one_item_Category)).getSelectedItem().toString().toUpperCase();
		SellType category = null;
		for (SellType s: SellType.values())
			if (stringCategory.equals(s.name())) category = s;
		// Description
		String description = ((EditText)findViewById(R.id.sell_one_item_Description)).getText().toString();
		// Images, notice that the main picture has an index of 0
		ArrayList<Bitmap> pictures = new ArrayList<Bitmap>();
		if (avarta_photo_index>=0 && avarta_photo_index<IMAGES.size())  pictures.add(IMAGES.get(avarta_photo_index));
		for (int i=0; i<IMAGES.size(); i++)
			if (i!=avarta_photo_index) pictures.add(IMAGES.get(i));
		IMAGES.clear(); IMAGES = pictures;
		// Return Sellable Object 
		return new Sellable(user, item, price, category, description, condition, pictures);
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
			Toast.makeText(this, "height, width = "+ thePic.getHeight(), Toast.LENGTH_SHORT).show();
		} else if (requestCode == PICK_PHOTO) {
			if (resultCode == Activity.RESULT_CANCELED) return;
			Uri pickedUri = data.getData();
			//declare the bitmap
			Bitmap thePic = null;
			// Find the ImageView
			ImageView picView = (ImageView)findViewById(R.id.sell_one_item_Piture);
			// Declare the path string
			String imgPath = "";
			// Retrieve the string using media data
			String[] medData = { MediaStore.Images.Media.DATA };
			// Query the data
			Cursor picCursor = managedQuery(pickedUri, medData, null, null, null);
			if(picCursor!=null) {
			    // Get the path string
			    int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			    picCursor.moveToFirst();
			    imgPath = picCursor.getString(index);
			} else imgPath = pickedUri.getPath();
			
			thePic = loadingBitmapEfficiently(imgPath);
			// Put image in ImageView
			picView.setImageBitmap(thePic);
			IMAGES.add(thePic);
			current_photo_index += 1;
			setStatus();
			Toast.makeText(this, "Bitmap has "+IMAGES.size()+" pictures", Toast.LENGTH_SHORT).show();
		}		
	}
	// Load image efficient 
	public Bitmap loadingBitmapEfficiently(String imgPath) {
		int width = 200, height = 1000;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// Find dimension of the picture
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		options.inSampleSize = Math.min(options.outHeight/height, options.outWidth/width);
		// Decode the image
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imgPath, options);
	}

}

