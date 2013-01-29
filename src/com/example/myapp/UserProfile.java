package com.example.myapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class UserProfile extends Activity {
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		// set navigating icon
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_profile, menu);
		return true;
	}

	public void changeAccount(View view) {
		Intent intent = new Intent(view.getContext(), SettingPreferences.class);
		startActivity(intent);
	}

	public void goBuying(View view) {
		Intent intent = new Intent(view.getContext(), BuyingItems.class);
		startActivity(intent);
	}

	public void goSelling(View view) {
		Intent intent = new Intent(view.getContext(), SellingItems.class);
		startActivity(intent);

	}
	public void goRequesting(View view) {
		Intent intent = new Intent(view.getContext(), RequestingItems.class);
		startActivity(intent);
	}

	public void goHistory(View view) {
		Intent intent = new Intent(view.getContext(), History.class);
		startActivity(intent);
	}
}
