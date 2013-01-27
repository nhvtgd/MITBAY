package com.example.myapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.example.myapp.helper.AlertDialogManager;
import com.example.myapp.helper.ListViewAdapter;
import com.example.myapp.helper.SortingFunction;
import com.login.LogInPage;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
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

	ArrayList<Sellable> sortedList = new ArrayList<Sellable>();

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
		data = new GetData();
		data.execute(queryResult);
		Log.d("No way", "Shouldn't get here right away");

		settings = getSharedPreferences(SETTING, 0);

		Button refresh = (Button) findViewById(R.id.refresh_button_customized_listView);
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				data = new GetData();
				data.execute(queryResult);

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
				Intent i = new Intent(act, TopRequested.class);
				startActivity(i);

			}
		});
		/*
		 * if (itemList != null && itemList.size() > 0) { for (final Sellable i
		 * : itemList) { ParseQuery query = new ParseQuery("Sellable");
		 * query.getInBackground(i.getId(), new GetCallback() {
		 * 
		 * @Override public void done(ParseObject arg0, ParseException arg1) {
		 * if (arg1 == null) { ParseFile file = (ParseFile) arg0.get("pic");
		 * file.getDataInBackground(new GetDataCallback() { public void
		 * done(byte[] data, ParseException e) { if (e == null) {
		 * loadByteImageToArray(i,data); } else { } } });
		 * 
		 * } } }); }
		 * 
		 * }
		 */

	}

	// private void loadByteImageToArray(Sellable i,byte[] data){
	// if (data != null || data.length >0){
	//
	// }
	// }

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// SortData sort = new SortData();
		//
		// sort.execute(item.getTitle().toString().toLowerCase());
		if (search.getText().toString().trim().length() > 0) {
			sortedList = filterList;

		} else {
			sortedList = itemList;
		}
		if (!item.getTitle().toString().trim().toLowerCase()
				.equalsIgnoreCase(MITBAYActivity.DATE)) {
			SortingFunction.sort(item.getTitle().toString().trim()
					.toLowerCase(), sortedList);
			list = (ListView) findViewById(R.id.my_list);
			adapter = new ListViewAdapter(act, sortedList);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new ItemOnClickListener());
			adapter.notifyDataSetChanged();
			progressDialog.cancel();
		} else {
			SortData data = new SortData();
			data.execute(MITBAYActivity.DATE);
		}
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
				sell = newDataBase.returnListInOrderByDescending(query);
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
					sell = newDataBase
							.returnListInOrderByDescending("createdAt");
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

	private String getCreatedAtDate(Sellable item) throws ParseException {
		ParseDatabase database = new ParseDatabase(this);
		ParseQuery sellable = new ParseQuery("Sellable");
		Log.d("create data base", "OK");
		ParseObject obj = sellable.get(item.getId());
		Log.d("create parse obj", obj.getClassName());

		SimpleDateFormat df = new SimpleDateFormat("hh:ss, dd/MM/yy");

		return obj.getCreatedAt().toLocaleString();

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
			intent.putExtra("date", item.getDate());
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
