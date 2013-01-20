package com.example.myapp;
import static android.graphics.BitmapFactory.decodeFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.provider.MediaStore.Images;
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


public class SellOneItem extends Activity {
	// Keep track of camera request
	private final int CAMERA_PIC_REQUEST = 314156;
	// Keep track of choosing photo from storage
	final int PICK_PHOTO = 271828;
	// Save image, path
	public Bitmap IMAGE = null;
	public String imgPath = "";
	Uri mCapturedImageURI;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell_one_item);
		loadSettingData(); 	// Load setting data from user
		setView();
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
	/**
	 * Take picture by camera
	 * @param view
	 */
	public void takePhoto(View view) {
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST); 
	}

	/**
	 * Take picture form directory
	 * @param view
	 */
	public void chosePicture(View view) {
		Intent pickPhoto = new Intent(Intent.ACTION_PICK);
		pickPhoto.setType("image/*");
		startActivityForResult(pickPhoto, PICK_PHOTO);
	}

	public void deletePicture(View view) {
		IMAGE = null;
		setView();
	}
	/**
	 * Set view for Image and text status
	 */
	public void setView() {
		ImageView picView = (ImageView)findViewById(R.id.sell_one_item_Piture);
		TextView takePicture = (TextView) findViewById(R.id.sell_one_item_TakePictureText);
		TextView pickPicture = (TextView) findViewById(R.id.sell_one_item_PickPictureText);
		ImageButton deletePicture = (ImageButton) findViewById(R.id.sell_one_item_Delete);
		picView.setImageBitmap(IMAGE);
		if (IMAGE == null) {
			takePicture.setText("Take picture");
			pickPicture.setText("Browser");
			deletePicture.setVisibility(ImageButton.INVISIBLE);
		} else {
		takePicture.setText("");
		pickPicture.setText(""); 
		deletePicture.setVisibility(ImageButton.VISIBLE);} 
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
	/**
	 * Cancel selling item
	 * @param view
	 * go back to home screen
	 */
	public void cancelSellTheItem(View view) {
//		 Go back to the ItemSelection
		startActivity(new Intent(view.getContext(), ItemSelection.class));
	}
	/**
	 * Handle click Done button
	 * @param view
	 * check valid information
	 * send to server
	 * start next activity (confirm)
	 */
	public void doneSellTheItem(View view) {
		Toast.makeText(view.getContext(), "will be continued", Toast.LENGTH_SHORT).show();
		String priceString = ((EditText) findViewById(R.id.sell_one_item_Price)).getText().toString();
		if (!isValidPrice(priceString)) return;
		// else { create Sellable object/ send to the server}
		// Create a Sellable object
		Sellable obj = createSellableObject();
		// Send to server
//		long start = System.currentTimeMillis();
//		Log.d("sending server", ""+System.currentTimeMillis());
//		ParseDatabase parse = new ParseDatabase(getApplicationContext());
//		parse.sendSellableToServer(obj);
//		long end = System.currentTimeMillis();
//		Log.d("sent server", ""+System.currentTimeMillis());
//		Log.d("Running time", ""+(end - start));
		// Create intent for the next activity
		Intent intent = new Intent(view.getContext(), ConfirmSellItem.class);
		putExtraIntent(intent, obj);
		startActivity(intent);
		Log.d("load Ok", "------------------");
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
		String condition = ((Spinner)findViewById(R.id.sell_one_item_Quality)).getSelectedItem().toString();
		// Category
		String category = ((Spinner)findViewById(R.id.sell_one_item_Category)).getSelectedItem().toString();
		// Description
		String description = ((EditText)findViewById(R.id.sell_one_item_Description)).getText().toString();
		// Return Sellable Object 
		return new Sellable(user, item, price, category, description, condition, IMAGE);
	}

	/**
	 * Put extra information of Sellable object to intent for the next activity
	 * @param intent intent need to put extra information
	 * @param obj Sellable object
	 */
	public void putExtraIntent(Intent intent, Sellable obj) {
		// Put Images
		intent.putExtra("imgPath", imgPath);
		intent.putExtra("widthPicture", getWidthPicture(R.id.sell_one_item_Piture));
		// Put item, price, condition, category, description
		intent.putExtra("item", obj.getName());
		intent.putExtra("price", obj.getPrice());
		intent.putExtra("condition", ((Spinner)findViewById(R.id.sell_one_item_Quality)).getSelectedItem().toString());
		intent.putExtra("category", ((Spinner)findViewById(R.id.sell_one_item_Category)).getSelectedItem().toString());
		intent.putExtra("description", obj.getDescription());
	}
	
	/**
	 * Get result from taking photo and picking photo
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			if (resultCode == Activity.RESULT_CANCELED) return;
			// Find imgPath
			Uri uri = data.getData();
			imgPath = getPathFromUri(uri);
			// Load image in appropriate size
			int req_height =  1, req_width = getWidthPicture(R.id.sell_one_item_Piture);
			IMAGE = loadingBitmapEfficiently(imgPath, req_height, req_width);
			// Put image in ImageView
			setView();
		} else if (requestCode == PICK_PHOTO) {
			if (resultCode == Activity.RESULT_CANCELED) return;
			Uri uri = data.getData();
			// Get path
			imgPath = getPathFromUri(uri);
			// Load image in appropriate size
			int req_height =  1, req_width = getWidthPicture(R.id.sell_one_item_Piture);
			IMAGE = loadingBitmapEfficiently(imgPath, req_height, req_width);
			// Put image in ImageView
			setView();
		}		
	}
	
	public int getWidthPicture(int ID) {
		return ((ImageView)findViewById(ID)).getWidth();
	}
	
	/**
	 * Loading picture with a desired size
	 * @param imgPath: directory of the picture
	 * @param required_height: desired height
	 * @param required_width: desired width
	 * @return
	 */
	public Bitmap loadingBitmapEfficiently(String imgPath, int required_height, int required_width) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// Find dimension of the picture
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		options.inSampleSize = Math.min(options.outHeight/required_height, options.outWidth/required_width);
//		Toast.makeText(getApplicationContext(), "inSampleSize = "+options.inSampleSize, Toast.LENGTH_SHORT).show();
		// Decode the image
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imgPath, options);
	}
	
	/**
	 * Get path from Uri
	 * @param uri
	 * @return
	 */
	public String getPathFromUri(Uri uri) {
		// Declare media
		String[] medData = { MediaStore.Images.Media.DATA };
		// Query the data
		Cursor picCursor = managedQuery(uri, medData, null, null, null);
		// Get the path string
		if(picCursor != null) {
		    int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    picCursor.moveToFirst();
		    return picCursor.getString(index);
		} else return uri.getPath();
	}
}

