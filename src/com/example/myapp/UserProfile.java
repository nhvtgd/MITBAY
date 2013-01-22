package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class UserProfile extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
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
