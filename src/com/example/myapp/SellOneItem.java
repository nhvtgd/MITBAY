package com.example.myapp;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class SellOneItem extends MITBAYActivity {
	// Keep track of camera request
	private final int CAMERA_PIC_REQUEST = 314156;
	// Keep track of choosing photo from storage
	final int PICK_PHOTO = 271828;
	// Size of the picture
	final int WIDTH = 200;
	final int HEIGHT = 200;	
	// Save image, path
	public Bitmap IMAGE = null;
	public String imgPath = "";
	Uri mCapturedImageURI;
	long start, end;

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
	/**
	 * Loading setting data
	 */
	public void loadSettingData() {
		// GetSharedPreferences
		SharedPreferences settings = getSharedPreferences(SETTING, 0);
		String userName = settings.getString(USERNAME, "Anonymous");
		String email = settings.getString(EMAIL, "Not found");
		String address = settings.getString(ADDRESS, "");
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
		File file = new File(Environment.getExternalStorageDirectory(), "sell_item.png");
		Uri outputFileUri = Uri.fromFile(file);
		imgPath = outputFileUri.getPath();
		//Generate the Intent.
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, CAMERA_PIC_REQUEST); 
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
		IMAGE.recycle(); 
		IMAGE = null;
		imgPath = "";
		setView();
	}
	/**
	 * Set view for Image and text status
	 */
	public void setView() {
		ImageView picView = (ImageView)findViewById(R.id.sell_one_item_Piture);
		ImageButton deletePicture = (ImageButton) findViewById(R.id.sell_one_item_Delete);
		LinearLayout frameDevices = (LinearLayout) findViewById(R.id.sell_one_item_Devices);
		// Set layout params for image view
		if (IMAGE == null) {
			frameDevices.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			deletePicture.setVisibility(ImageButton.INVISIBLE);
		} else {
			frameDevices.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
			deletePicture.setVisibility(ImageButton.VISIBLE);
			picView.setMinimumHeight(picView.getWidth());
		}
		picView.setImageBitmap(IMAGE);
		
	}
	/**
	 * Check if a string is a valid number
	 * @param priceString
	 * @return
	 */
	public boolean isValidPrice(String priceString) {
		try {
			Double.parseDouble(priceString);
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
		// Go back to the ItemSelection
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
//		Sellable obj = createSellableObject();
//		// Send to server
//		start = System.currentTimeMillis();
//		Log.d("sending server", ""+System.currentTimeMillis());
//		ParseDatabase parse = new ParseDatabase(getApplicationContext());
//		String id = parse.sendSellableToServer(obj);
//		end = System.currentTimeMillis();
//		Log.d("sent server", ""+System.currentTimeMillis());
		// Create intent for the next activity
		Intent intent = new Intent(view.getContext(), ConfirmSellItem.class);
		putExtras(intent);
		startActivity(intent);
		Log.d("load Ok", "------------------");
	}

	/**
	 * Loading input information including item, price, condition, type, description
	 *  for the next activity (confirm to buy item)
	 * @return 
	 */
	private void putExtras(Intent intent) {
		// Loading item, price, condition, type, description
		String item = ((EditText)findViewById(R.id.sell_one_item_Item)).getText().toString();
		String price = ((EditText)findViewById(R.id.sell_one_item_Price)).getText().toString();
		String condition = ((Spinner)findViewById(R.id.sell_one_item_Quality)).getSelectedItem().toString();
		String type = ((Spinner)findViewById(R.id.sell_one_item_Category)).getSelectedItem().toString();
		String description = ((EditText)findViewById(R.id.sell_one_item_Description)).getText().toString();
		// Put extras
		intent.putExtra(ITEM, item);
		intent.putExtra(PRICE, price);
		intent.putExtra(CONDITION, condition);
		intent.putExtra(TYPE, type);
		intent.putExtra(DESCRIPTION, description);
		intent.putExtra("imgPath", imgPath);
	}

	/**
	 * Put extra information of Sellable object to intent for the next activity
	 * @param intent intent need to put extra information
	 * @param obj Sellable object
	 */
	public void putExtraIntent(Intent intent, Sellable obj) {
		// Put Images
		intent.putExtra("imgPath", imgPath);
		// Put item, price, condition, category, description
		intent.putExtra(ITEM, obj.getName());
		intent.putExtra(PRICE, obj.getPrice());
		intent.putExtra(CONDITION, ((Spinner)findViewById(R.id.sell_one_item_Quality)).getSelectedItem().toString());
		intent.putExtra(TYPE, ((Spinner)findViewById(R.id.sell_one_item_Category)).getSelectedItem().toString());
		intent.putExtra(DESCRIPTION, obj.getDescription());
	}

	/**
	 * Get result from taking photo and picking photo
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			if (resultCode == Activity.RESULT_CANCELED) return;
			// Load image in appropriate size
			IMAGE = loadingBitmapEfficiently(imgPath, WIDTH, HEIGHT);
			// Put image in ImageView
			setView();
		} else if (requestCode == PICK_PHOTO) {
			if (resultCode == Activity.RESULT_CANCELED) return;
			Uri uri = data.getData();
			// Get path
			imgPath = getPathFromUri(uri);
			// Load image in appropriate size
			IMAGE = loadingBitmapEfficiently(imgPath, WIDTH, HEIGHT);
			setView();
		}		
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
		Log.d("out height, width, inSampleSize", options.outHeight+", "+options.outWidth+", "+options.inSampleSize);
		Toast.makeText(getApplicationContext(), "inSampleSize = "+options.inSampleSize, Toast.LENGTH_SHORT).show();
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



