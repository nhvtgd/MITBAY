package com.example.myapp.helper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.MITBAYActivity;
import com.example.myapp.ParseDatabase;
import com.example.myapp.R;
import com.example.myapp.Sellable;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * This class is a customized list view adapter to bind a certain listView with
 * 
 * @author trannguyen
 * 
 */
public class ListViewAdapter extends BaseAdapter {
	/** The activity where the list selection page appears */
	private Activity activity;
	/** The collections of sellable objects */
	private ArrayList<Sellable> data;
	/** To inflate the view from an xml file */
	private static LayoutInflater inflater = null;

	LruCache<String, Bitmap> mMemoryCache;

	// private ImageLoader imageLoader;

	/**
	 * This is a constructor that take in the activity where the list View is
	 * displayed and the collection of sellable objects
	 * 
	 * @param a
	 * @param d
	 */
	public ListViewAdapter(Activity a, ArrayList<Sellable> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// imageLoader = new ImageLoader(a.getApplicationContext());
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};
		ParseDatabase database = new ParseDatabase(a);

	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.activity_listing, null);

		TextView name = (TextView) vi.findViewById(R.id.item_name_in_post);
		// TextView category =
		// (TextView)vi.findViewById(R.id.item_category_in_post);
		TextView location = (TextView) vi
				.findViewById(R.id.item_location_in_post);
		TextView description = (TextView) vi
				.findViewById(R.id.item_description_in_post);
		TextView priceValue = (TextView) vi.findViewById(R.id.item_price_value);
		ImageView image = (ImageView) vi.findViewById(R.id.item_image_in_post);

		Log.d("view", "create view successfully");
		Sellable item = data.get(position);

		Log.d("View", "create view at position" + position);

		// Setting all values in listview
		name.setText(item.getName() + " (" + item.getCondition() + ")");
		// category.setText(item.getType().toString());
		
		location.setText(item.getLocation());
		description.setText(item.getDescription());
		priceValue.setText(item.getPrice());

		if (item.getImages() == null) {
			if (item.getType().toString().equals(MITBAYActivity.TEXTBOOK)) {
				image.setImageResource(R.drawable.text_book);
			} else if (item.getType().toString()
					.equals(MITBAYActivity.FURNITURE)) {
				image.setImageResource(R.drawable.furniture_icon);
			} else if (item.getType().toString()
					.equals(MITBAYActivity.TRANSPORTATION)) {
				image.setImageResource(R.drawable.bike);
			} else if (item.getType().toString().equals(MITBAYActivity.MISC)) {
				image.setImageResource(R.drawable.misc);
			} else
				image.setImageResource(R.drawable.unknown);
		} else{
			Log.d("Image not null", item.getId());
			image.setImageBitmap(item.getImages());
		}

		// ParseQuery query = new ParseQuery("Sellable");
		// query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
		// ParseObject obj = null;
		// try {
		// Log.d("fuck here", "doom");
		//
		// obj = query.getInBackground(objectId, callback)
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// Log.d("before", "fuck");
		// ParseFile file = (ParseFile) obj.get("pic");
		// Log.d("URL", file.getUrl());
		return vi;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	public void loadBitmap(String id, final ImageView imageView,
			final Sellable item) throws ParseException {
		final String imageKey = id;

		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			item.setImages(bitmap);
		} else {
			if (item.getType().toString().equals(MITBAYActivity.TEXTBOOK)) {
				imageView.setImageResource(R.drawable.text_book);
			} else if (item.getType().toString()
					.equals(MITBAYActivity.FURNITURE)) {
				imageView.setImageResource(R.drawable.furniture_icon);
			} else if (item.getType().toString()
					.equals(MITBAYActivity.TRANSPORTATION)) {
				imageView.setImageResource(R.drawable.bike);
			} else if (item.getType().toString().equals(MITBAYActivity.MISC)) {
				imageView.setImageResource(R.drawable.misc);
			} else
				imageView.setImageResource(R.drawable.unknown);
			ParseQuery query = new ParseQuery("Sellable");
			query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
			ParseObject obj = query.get(item.getId());
			ParseFile file = (ParseFile) obj.get("pic");
			Log.d("URL", file.getUrl());

		}
		// private class ImageLoader {
		// Activity activity;
		// public ImageLoader(Activity a) {
		// activity = a;
		// }
		//
		// public void displayImage(ImageView imaview,String id){
		// File file = open()
		// }
		// }
	}
}