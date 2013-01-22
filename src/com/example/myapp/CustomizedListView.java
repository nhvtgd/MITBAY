package com.example.myapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.myapp.helper.AlertDialogManager;
import com.example.myapp.helper.ListViewAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * This class creates the list selection screen when the user click on a
 * paticular category
 * 
 * @author trannguyen
 * 
 */
public class CustomizedListView extends Activity {
	/**
	 * The listview that holds the images (defined in xml file)
	 */
	ListView list;
	/**
	 * The adapter for the list
	 */
	ListViewAdapter adapter;

	/**
	 * Show the progress dialog while waiting for the screen to load
	 */
	ProgressDialog progressDialog;

	/**
	 * The list that will store the resulting items
	 */
	ArrayList<Sellable> itemList = new ArrayList<Sellable>();
	
	/**
	 * This activity 
	 */
	Activity act = this;
	
	String queryResult;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customized_list_view);

		Log.d("set up", "Content View created");
		Boolean SelectOrSearch = getIntent().getExtras().containsKey("query");
		if (SelectOrSearch){
			queryResult= getIntent().getStringExtra("query");
		}
		else{
			Log.d("Search", "get Search term");
			queryResult = getIntent().getStringExtra("search");
		}
		GetData data = new GetData();
		data.execute(queryResult);
		Log.d("No way", "Shouldn't get here right away");

	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_customized_list_view, menu);
		return true;
	};

	/**
	 * This is a class that will run in background to populate the list
	 * of item in the server
	 * @author trannguyen
	 *
	 */
	private class GetData extends
			AsyncTask<String, Void, ArrayList<Sellable>> {
		
		/**
		 * Some constant default query to call on the server side
		 */
		private static final String TEXTBOOK = "TEXT BOOK";
		private static final String FURNITURE= "FURNITURE";
		private static final String TRANSPORTATION = "FURNITURE";
		private static final String MISC= "MISC";
		
		/**
		 * Place holder constructor to call excute method later
		 */
		public GetData() {
			
		}

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(act);
			progressDialog.setMessage("Downloading data ...");
			progressDialog.show();
			Log.d("pD", "progess Dialog created");
		}

		@Override
		protected ArrayList<Sellable> doInBackground(String... params) {
			String query = params[0];// get the activity
			// create the data base to initialize things
			ParseDatabase newDataBase = new ParseDatabase(act);
			Log.d("dataBase", "Creat data base");			
			
			ArrayList<Sellable> sell = null;
			try{
				if (query.equals("all")){
					sell = newDataBase.getListAllSellable();
				}
				else if (query.equals("text")){
					sell = newDataBase.getListType(TEXTBOOK);
				}
				else if (query.equals("fur")){
					sell = newDataBase.getListType(FURNITURE);
				}
				else if (query.equals("trans")){
					sell = newDataBase.getListType(TRANSPORTATION);
				}
				else if (query.equals("misc")){
					sell = newDataBase.getListType(MISC);
				}
				else {
					sell = newDataBase.getListSellableWithName(queryResult);
				}			
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("create query", "get the content");
			return sell;
			

			

		}

		@Override
		protected void onPostExecute(ArrayList<Sellable> result) {
			if (result.size() > 0) {
				Log.d("post", "at least it returns");
				list = (ListView) findViewById(R.id.my_list);
				adapter = new ListViewAdapter(act, result);
				// bind the adapter with the view
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				progressDialog.cancel();
			}
			else{
				progressDialog.cancel();
				new AlertDialogManager().showAlertDialog(act, "Not Found", "No Item matches your query", false);
				
			}
			
			

		}

	}

}
