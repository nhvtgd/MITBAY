package com.example.myapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.myapp.helper.AlertDialogManager;
import com.example.myapp.helper.ListViewAdapter;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * This class creates the list selection screen when the user click on a
 * paticular category
 * 
 * @author trannguyen
 * 
 */
public class CustomizedListView extends MITBAYActivity implements
		OnClickListener {
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
	 * The list that will store the filter item when user start typing
	 */
	ArrayList<Sellable> filterList = new ArrayList<Sellable>();

	/**
	 * This activity
	 */
	Activity act = this;

	String queryResult;

	/**
	 * GetData object used to populate data from server
	 */
	GetData data;

	/**
	 * edit text for filetering.
	 */
	EditText search;
	protected final static String DEFAULT = "Sort";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customized_list_view);

		Log.d("set up", "Content View created");
		Boolean SelectOrSearch = getIntent().getExtras().containsKey("query");
		if (SelectOrSearch) {
			queryResult = getIntent().getStringExtra("query");
		} else {
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
				Intent i = new Intent(v.getContext(), SellOneItem.class);
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

		ImageButton sort = (ImageButton) findViewById(R.id.sort_by_button);
		registerForContextMenu(sort);
		sort.setOnClickListener(this);

		search = (EditText) findViewById(R.id.search_bar_customized_listView);
		search.addTextChangedListener(new TextChangeRecorder());
		search.setSingleLine();

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		SortData sort = new SortData();

		sort.execute(item.getTitle().toString().toLowerCase());
		return true;

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_customized_list_view, menu);
		return true;
	};

	private class TextChangeRecorder implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			int textlength = search.getText().length();
			filterList.clear();

			for (int i = 0; i < itemList.size(); i++) {
				String searchText = search.getText().toString();
				if (isSubString(searchText, itemList.get(i).getName())
						|| isSubString(searchText, itemList.get(i)
								.getCondition())
						|| isSubString(searchText, itemList.get(i)
								.getDescription())
						|| isSubString(searchText, itemList.get(i).getType())
						|| isSubString(searchText, itemList.get(i).getCondition()))
					filterList.add(itemList.get(i));

			}

			list.setAdapter(new ListViewAdapter(act, filterList));

		}

	}

	/**
	 * This is a class that will run in background to populate the list of item
	 * in the server
	 * 
	 * @author trannguyen
	 * 
	 */
	private class SortData extends AsyncTask<String, Void, ArrayList<Sellable>> {

		/**
		 * Some constant default query to call on the server side
		 */

		/**
		 * Place holder constructor to call excute method later
		 */
		public SortData() {

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
			try {
				sell = newDataBase.returnListInOrderByAscending(query);
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
				itemList = result;
				Log.d("post", "at least it returns");
				list = (ListView) findViewById(R.id.my_list);
				adapter = new ListViewAdapter(act, result);
				// bind the adapter with the view
				list.setAdapter(adapter);
				list.setOnItemClickListener(new ItemOnClickListener());
				adapter.notifyDataSetChanged();
				progressDialog.cancel();
			} else {
				progressDialog.cancel();
				new AlertDialogManager().showAlertDialog(act, "Not Found",
						"No Item matches your query", false);

			}
		}

	}

	/**
	 * This is a class that will run in background to populate the list of item
	 * in the server
	 * 
	 * @author trannguyen
	 * 
	 */
	private class GetData extends AsyncTask<String, Void, ArrayList<Sellable>> {

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
			ParseQuery parseQuery = new ParseQuery(query);

			Log.d("dataBase", "Creat data base");

			ArrayList<Sellable> sell = null;
			try {
				if (query.equals("all")) {
					sell = newDataBase.getListAllSellable();
				} else if (query.equals("text")) {
					sell = newDataBase.getListType(TEXTBOOK);
				} else if (query.equals("fur")) {
					sell = newDataBase.getListType(FURNITURE);
				} else if (query.equals("trans")) {
					sell = newDataBase.getListType(TRANSPORTATION);
				} else if (query.equals("misc")) {
					sell = newDataBase.getListType(MISC);
				} else {
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
				itemList = result;
				Log.d("post", "at least it returns");
				list = (ListView) findViewById(R.id.my_list);
				adapter = new ListViewAdapter(act, result);
				// bind the adapter with the view
				list.setAdapter(adapter);
				list.setOnItemClickListener(new ItemOnClickListener());
				adapter.notifyDataSetChanged();
				progressDialog.cancel();
			} else {
				progressDialog.cancel();

				new AlertDialogManager().showAlertDialog(act, "Not Found",
						"No Item matches your query", false);
			}

		}

	}

	public class ItemOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Sellable item = (Sellable) adapter.getItem(arg2);
			Intent intent = new Intent(arg1.getContext(), ItemDetail.class);
			intent.putExtra(MITBAYActivity.USERNAME, item.getSeller().getName());
			Log.d("user", item.getSeller().getName());
			intent.putExtra(EMAIL, item.getSeller().getEmail());
			intent.putExtra(DATE, item.getDate());
			Log.d("date", item.getDate());
			intent.putExtra("type", item.getType());
			Log.d("type", item.getType());
			intent.putExtra(DESCRIPTION, item.getDescription());
			intent.putExtra(ID, item.getId());
			intent.putExtra(IMAGE, item.getImages());
			intent.putExtra(CONDITION, item.getCondition());
			intent.putExtra(PRICE, item.getPrice());
			intent.putExtra(ITEM, item.getName());
			startActivity(intent);

		}

	}

	public boolean isSubString(String text1, String text2) {
		if (text1 == null)
			return true;
		if (text2 == null || text2.length() < text1.length())
			return false;
		int len1 = text1.length();
		for (int i = 0; i < text2.length(); i++) {
			if (i + len1 <= text2.length()) {
				if (text2.substring(i, i + len1).equalsIgnoreCase((text1)))
					return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		this.openContextMenu(v);

	}

}
