package com.example.myapp;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.myapp.helper.ConnectionDetector;
import com.example.myapp.helper.ListViewAdapter;
import com.login.LogInPage;
import com.parse.CountCallback;
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

	private SharedPreferences settings;
	protected CharSequence GREETING = "Dear User";

	private final String INTENT_QUERY = "query";

	private final String INTENT_SEARCH = "search";

	public CharSequence DOWNLOAD_MESSAGE = "Downloading data...";

	ConnectionDetector connection = new ConnectionDetector(this);

	private final String NETWORK_ERROR_TITLE = "NO CONNECTION";

	private final String NETWORK_ERROR_MESSAGE = "Please check your connection and try again";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customized_list_view);

		// set navigating icon
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);

		Log.d("set up", "Content View created");
		Boolean SelectOrSearch = getIntent().getExtras().containsKey(
				INTENT_QUERY);
		if (SelectOrSearch) {
			queryResult = getIntent().getStringExtra(INTENT_QUERY);
		} else {
			Log.d("Search", "get Search term");
			queryResult = getIntent().getStringExtra(INTENT_SEARCH);
		}
		if (connection.isConnectingToInternet()) {
			data = new GetData();
			data.execute(queryResult);
		} else {
			new AlertDialogManager().showAlertDialog(this, NETWORK_ERROR_TITLE,
					NETWORK_ERROR_MESSAGE, false);
		}
		Log.d("No way", "Shouldn't get here right away");

		settings = getSharedPreferences(SETTING, 0);

		Button refresh = (Button) findViewById(R.id.refresh_button_customized_listView);
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseDatabase newDataBase = new ParseDatabase(act);
				ParseQuery parseQuery = new ParseQuery(queryResult);

				parseQuery.countInBackground(new CountCallback() {

					@Override
					public void done(int arg0, ParseException arg1) {
						if (arg1 == null)
							doRefresh(arg0);

					}

				});
			}

		});

		ImageButton sort = (ImageButton) findViewById(R.id.sort_by_button);
		registerForContextMenu(sort);
		sort.setOnClickListener(this);

		search = (EditText) findViewById(R.id.search_bar_customized_listView);
		search.addTextChangedListener(new TextChangeRecorder());
		search.setSingleLine();

		// need to login to sell, see notification, or profile
		Button sell = (Button) findViewById(R.id.sell_button_customized_listView);
		sell.setOnClickListener(new CustomizedOnClickListener(act,
				SellOneItem.class));
		ImageButton notification = (ImageButton) findViewById(R.id.notification_by_button);
		notification.setOnClickListener(new CustomizedOnClickListener(act,
				NotificationHistory.class));

		ImageButton profile = (ImageButton) findViewById(R.id.profile_by_button);

		profile.setOnClickListener(new CustomizedOnClickListener(act,
				UserProfile.class));

		// No need to login to see top requested item
		ImageButton request = (ImageButton) findViewById(R.id.top_requested_by_button);

		request.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(act, RequestingItems.class);
				startActivity(i);

			}
		});

	}

	private void doRefresh(int arg0) {
		if (arg0 != itemList.size()) {
			Log.d("result", arg0 + " " + itemList.size());
			GetData data = new GetData();
			data.execute(queryResult);
		}

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case (android.R.id.home):
			Intent intent = new Intent(this, ItemSelection.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

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
						|| isSubString(searchText, itemList.get(i)
								.getCondition()))
					filterList.add(itemList.get(i));

			}

			list.setAdapter(new ListViewAdapter(act, filterList));

		}

	}

	private class CustomizedOnClickListener implements OnClickListener {

		private Activity origin;
		private Class<?> toActivity;

		public CustomizedOnClickListener(Activity origin, Class<?> toActivity) {
			this.origin = origin;
			this.toActivity = toActivity;
		}

		@Override
		public void onClick(View v) {
			if (settings.getBoolean(IS_ALREADY_LOG_IN, false)) {
				Intent i = new Intent(origin, toActivity);
				Log.d("sellOneItem", "here");
				startActivity(i);
			} else {
				AlertDialog alertDialog = new AlertDialog.Builder(origin)
						.create();

				// Setting Dialog Title
				alertDialog.setTitle(GREETING);

				// Setting Dialog Message
				alertDialog.setMessage(MESSAGE);

				alertDialog.setIcon(R.drawable.fail);

				// Setting OK Button
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent(act, LogInPage.class);
								startActivity(i);

							}
						});
				alertDialog.setButton2("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				alertDialog.show();
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
			progressDialog.setMessage(DOWNLOAD_MESSAGE);
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
				if (query.equals(ItemSelection.ALL)) {
					sell = newDataBase.getListEnabled();
				} else if (query.equals(ItemSelection.TEXTBOOK)) {
					sell = newDataBase.getListType(TEXTBOOK);
				} else if (query.equals(ItemSelection.FURNITURE)) {
					sell = newDataBase.getListType(FURNITURE);
				} else if (query.equals(ItemSelection.TRANSPORTATION)) {
					sell = newDataBase.getListType(TRANSPORTATION);
				} else if (query.equals(ItemSelection.MISC)) {
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
			intent.putExtra(LOCATION, item.getLocation());
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
