package com.example.myapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.myapp.helper.AlertDialogManager;
import com.example.myapp.helper.ListViewAdapter;
import com.parse.ParseException;

/**
 * This class creates the list selection screen when the user click on a
 * paticular category
 * 
 * @author trannguyen
 * 
 */
public class CustomizedListView extends MITBAYActivity {
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
	
	GetData data;

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
		data = new GetData();
		data.execute(queryResult);
		Log.d("No way", "Shouldn't get here right away");
		Button sell = (Button) findViewById(R.id.sell_button_customized_listView);
		sell.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),SellOneItem.class);
				Log.d("sellOneItem", "here");
				startActivity(i);
				
			}
		});
		Button refresh = (Button) findViewById(R.id.refresh_button_customized_listView);
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.refreshDrawableState();
				
			}
		});

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
				list.setOnItemClickListener(new ItemOnClickListener());
				adapter.notifyDataSetChanged();
				progressDialog.cancel();
			}
			else{
				progressDialog.cancel();
				new AlertDialogManager().showAlertDialog(act, "Not Found", "No Item matches your query", false);
				
			}		
		}

	}
	
		public class ItemOnClickListener implements OnItemClickListener{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Sellable item = (Sellable) adapter.getItem(arg2);
				Intent intent = new Intent(arg1.getContext(),ItemDetail.class);
				intent.putExtra(MITBAYActivity.USERNAME, item.getSeller().getName());
				Log.d("user", item.getSeller().getName());
				intent.putExtra(EMAIL, item.getSeller().getEmail());
				intent.putExtra(DATE, item.getDate());
				Log.d("date", item.getDate());
				intent.putExtra("type", item.getType());
				Log.d("type", item.getType());
				intent.putExtra(DESCRIPTION, item.getDescription());
				intent.putExtra(ID, item.getId());
				intent.putExtra(IMAGE, item.getDefaultImage(item.getType()));
				intent.putExtra(CONDITION, item.getCondition());
				intent.putExtra(PRICE, item.getPrice());
				intent.putExtra(ITEM, item.getName());
				startActivity(intent);
				
			}
			
		}

}
