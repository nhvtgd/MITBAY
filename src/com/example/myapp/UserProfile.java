package com.example.myapp;

import com.login.LogInPage;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class UserProfile extends MITBAYActivity {
	
	private SharedPreferences settings;
	private SharedPreferences.Editor prefEditor;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case (android.R.id.home):
			Intent intent = new Intent(this, ItemSelection.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case (R.id.menu_log_out): 
			// Process to log out
			Toast.makeText(getApplicationContext(), "You have already loged out", Toast.LENGTH_SHORT).show();
			prefEditor.clear();
			prefEditor.commit();
			intent = new Intent(this, LogInPage.class);
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
		settings = getSharedPreferences(SETTING, 0);
		prefEditor = settings.edit();
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
