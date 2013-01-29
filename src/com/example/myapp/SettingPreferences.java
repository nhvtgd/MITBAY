package com.example.myapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SettingPreferences extends MITBAYActivity {
	private boolean isValidLogIn = false;
	private boolean isChangingPassword = false;
	private boolean isChangingLocation = false;
	private String username;
	private String email;
	private String password;
	private String location;
	private SharedPreferences settings;
	private SharedPreferences.Editor prefEditor;
	private User current_user;
	private String newPassword;
	private Intent intent;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case (android.R.id.home):
			intent = new Intent(this, ItemSelection.class);
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
		setContentView(R.layout.activity_setting_preferences);
		// set navigating icon
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		settings = getSharedPreferences(SETTING, 0);
		prefEditor = settings.edit();
		username = settings.getString(USERNAME, "Anonymous");
		email = settings.getString(EMAIL, "");
		password = settings.getString(PASSWORD, "");
		setView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_setting_preferences, menu);
		return true;
	}
	/*
	 * Change location option
	 * 
	 */
	/**
	 * Handle click edit location
	 * @param view
	 * Changing state
	 */
	public void editLocation(View view) {
		isChangingLocation = ! isChangingLocation;
		setView();
	}
	/**
	 * Handle confirm new location
	 * @param view
	 * remember the new location
	 */
	public void confirmEditLocation(View view) {
		isChangingLocation = ! isChangingLocation;
		location = ((EditText) findViewById(R.id.setting_preferences_EditLocation)).getText().toString();
		((TextView) findViewById(R.id.setting_preferences_Location)).setText(location);
		// Save in SharedPreferences
		settings = getSharedPreferences(SETTING, 0);
		prefEditor = settings.edit();
		prefEditor.putString(LOCATION, location);
		prefEditor.commit();
		// save in user
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			      user.put(LOCATION, location);
			      user.saveInBackground();
			    } else {
			    	Toast.makeText(getApplicationContext(), "Unfortunately, there are some problem with server", Toast.LENGTH_LONG).show();
			    }
			  }
			});
		setView();
	}



	/*
	 *  Change password option
	 */
	/**
	 * Change password 
	 * @param view
	 * Result new password
	 */
	public void changePassword(View view) {
		isChangingPassword = ! isChangingPassword;
		setView();
	}

	/**
	 * Confirm to change password
	 * 	Check password
	 *  Send to server
	 *  Remember
	 * @param view
	 */
	public void confirmChangePassword(View view) {
		String currentPassword = ((EditText) findViewById(R.id.setting_preferences_CurrentPassword)).getText().toString();
		newPassword = ((EditText) findViewById(R.id.setting_preferences_NewPassword)).getText().toString();
		String newConfirmPassword = ((EditText) findViewById(R.id.setting_preferences_NewConfirmPassword)).getText().toString();
		// Get preferences, email
		settings = getSharedPreferences("settings", 0);
		prefEditor = settings.edit();
		// Check Password criteria
		if (!isValidPassword(newPassword)) {
			remindDialog("Your new password should have at least " + MINIMUM_LENGTH_PASSWORD +" characters");
			return ; }
		if (!isMatchPasswords(newPassword, newConfirmPassword)) {
			remindDialog("Your new password and confirmed password do not match");
			return ; }
		isValidLogIn(username, currentPassword);
	}

	/**
	 * Check valid log in
	 * @param email
	 * @param password
	 * @return If the email and password match with one user
	 */
	private boolean isValidLogIn(String username, String password) {
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					isValidLogIn = true;
					// Set new password
					user.setPassword(newPassword);
					user.saveInBackground();
					// Remember
					prefEditor.putString(PASSWORD, newPassword);
					prefEditor.commit();
					// Close frame
					isChangingPassword = ! isChangingPassword;
					setView();
					remindDialog("Your new password has updated successfully");
				} else {
					isValidLogIn = false;
					remindDialog("Your current password and user name do not match");
				}
			}
		});
		return isValidLogIn;
	}

	/**
	 * Create a remind a dialog with a text and Ok button
	 * @param text
	 */
	private void remindDialog(String text) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(text);
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			} // do nothing
		});
		builder.create().show();
	}


	/**
	 * Set view
	 * @param view
	 * Result appropriate view
	 */
	public void setView() {
		// Set Frame for change password area
		LinearLayout framePassword = (LinearLayout) findViewById(R.id.setting_preferences_FramePassword);
		LinearLayout frameLocation = (LinearLayout) findViewById(R.id.setting_preferences_FrameEditLocation);
		if (isChangingPassword) {
			framePassword.setVisibility(LinearLayout.VISIBLE);
		} else framePassword.setVisibility(LinearLayout.INVISIBLE);
		if (isChangingLocation) {
			frameLocation.setVisibility(LinearLayout.VISIBLE);
			frameLocation.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			((EditText) findViewById(R.id.setting_preferences_EditLocation)).setHint("Edit your location here");
		} else {
			frameLocation.setVisibility(LinearLayout.INVISIBLE);
			frameLocation.setLayoutParams(new LayoutParams(0, 0));
		}
		// Loading preferences data
		location = settings.getString(LOCATION, "(Add your location?)");
		// Set text
		((TextView)findViewById(R.id.setting_preferences_UserName)).setText(username);
		((TextView)findViewById(R.id.setting_preferences_Email)).setText(email);
		((TextView)findViewById(R.id.setting_preferences_Location)).setText(location);
	}

}


/* Need work
 * 	animation
 *  change password
 */ 
