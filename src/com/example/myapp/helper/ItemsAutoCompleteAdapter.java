package com.example.myapp.helper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
/**
 * This class will generate auto complete based on EBAY auto complete API, amazon seems
 * not to work well, this requires connection in order to perform the auto complete
 * @author trannguyen
 *
 */
/**
 * This class will generate auto complete based on EBAY auto complete API, amazon seems
 * not to work well, this requires connection in order to perform the auto complete
 * @author trannguyen
 *
 */
public class ItemsAutoCompleteAdapter extends ArrayAdapter<String> implements
		Filterable {
	private static final String LOG_TAG = "MITBAY";

	private static final String ITEM_API_BASE = "http://api.bing.com/osjson.aspx?query=";
	private static final String ITEM_API_EBAY = "http://anywhere.ebay.com/services/suggest/?q=";

	private ArrayList<String> resultList;

	private String TAG_CLASS = "id";
	
	private String TAG_ITEM = "items";

	/**
	 * Take in the context of the current activity and the textView resourse id 
	 * to populate the result
	 * @param context the context of the activity
	 * @param textViewResourceId the view resourse id
	 */
	public ItemsAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.
					ArrayList<String> result1 = autocomplete(constraint.toString());
//					ArrayList<String> result2 = autocompleteClass(constraint.toString());
//					result1.addAll(result2);
					resultList = result1;

					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}

	/**
	 * Perform auto complete on an input, default length to perform
	 * autocomplete search is 1
	 * @param input the String input
	 * @return the ArrayList<String> containing relevant search
	 */
	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(ITEM_API_EBAY);
			sb.append(URLEncoder.encode(input, "utf8"));
			Log.d("URL", sb.toString());
			
			
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		String result = jsonResults.toString().replaceAll("\"", "");
		String[] resultArray = result.split("\\[|,|\\]");


		// JSONArray predsJsonArray = new JSONArray(jsonResults.toString());
		Log.d("json", "create successful array");
		// Extract the Place descriptions from the results
		resultList = new ArrayList<String>();
		for (String i : resultArray) {
			if (i.length() > 0)
				resultList.add(i);
		}

		return resultList;
	}
	
	private ArrayList<String> autocompleteClass(String input){
		JSONParser parser = new JSONParser();
		
		JSONObject obj = parser.getJSONFromUrl("http://coursews.mit.edu/coursews/?term=2013SP");
		ArrayList<String> result = new ArrayList<String>();
		Set<String> removeDuplicate = new HashSet<String>();
		try {
			JSONArray classList = obj.getJSONArray(TAG_ITEM);
			for (int i = 0; i < classList.length();i++){
				JSONObject className = classList.getJSONObject(i);		
				if (className.has(TAG_CLASS) && className.getString(TAG_CLASS).contains(input) && !removeDuplicate.contains(className.getString(TAG_CLASS))){
					result.add(className.getString(TAG_CLASS));
					removeDuplicate.add(className.getString(TAG_CLASS));					
				}
					
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (ArrayList<String>) result.subList(0, 10);
		
		
	}
}
