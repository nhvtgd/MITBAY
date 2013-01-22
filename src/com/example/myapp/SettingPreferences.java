package com.example.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SettingPreferences extends Activity {

	private boolean isChangingPassword = false;
	private boolean isChangingLocation = false;
	private boolean validLogin = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_preferences);
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
		String new_location = ((EditText) findViewById(R.id.EditLocation)).getText().toString();
		((TextView) findViewById(R.id.Location)).setText(new_location);
		// Save in SharedPreferences
		SharedPreferences settings = getSharedPreferences("settings", 0);
		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putString("Location", new_location);
		prefEditor.commit();
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
		String currentPassword = ((EditText) findViewById(R.id.CurrentPassword)).getText().toString();
		String newPassword = ((EditText) findViewById(R.id.NewPassword)).getText().toString();
		String newConfirmPassword = ((EditText) findViewById(R.id.NewConfirmPassword)).getText().toString();
		// Get preferences, email
		SharedPreferences settings = getSharedPreferences("settings", 0);
		SharedPreferences.Editor prefEditor = settings.edit();
		String email = settings.getString("Email", "");
		// Check Password criteria
		if ( isValidPassword(newPassword) &&
			isMatchPasswords(newPassword, newConfirmPassword) &&
			isValidLogIn(email, currentPassword)) {
			// Send to server to change
			// Remember
			prefEditor.putString("Password", newPassword);
			prefEditor.commit();
			// Close frame
			isChangingPassword = ! isChangingPassword;
			setView();
		}
	}
	
	/**
	 * Check valid log in
	 * @param email
	 * @param password
	 * @return If the email and password match with one user
	 */
	public boolean isValidLogIn(String email, String password) {
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setPassword(password);
		ParseUser.logInInBackground(email, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					validLogin = true;
				} else {
					validLogin = false;
					remindDialog("Your current password and email do not match");
				}
			}
		});
		return validLogin;
	}
	
	/**
	 * Require password has at least a minimum number of character
	 * @param password
	 * @return
	 */
	public boolean isValidPassword(String password) {
		if (password.length() < 4) {
			remindDialog("Your password is required at least 4 characters");
			return false;
		} else return true;
	}
	
	/**
	 * Check new password matching
	 * @param password
	 * @param confirmPassword
	 * @return
	 */
	public boolean isMatchPasswords(String password, String confirmPassword) {
		if (password.equals(confirmPassword)) return true;
		else {
			remindDialog("The two new passwords do not match");
			return false;
		}
	}
	
	public void remindDialog(String text) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(text);
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
		LinearLayout framePassword = (LinearLayout) findViewById(R.id.FramePassword);
		LinearLayout frameLocation = (LinearLayout) findViewById(R.id.FrameEditLocation);
		if (isChangingPassword) {
			framePassword.setVisibility(LinearLayout.VISIBLE);
		} else framePassword.setVisibility(LinearLayout.INVISIBLE);
		if (isChangingLocation) {
			frameLocation.setVisibility(LinearLayout.VISIBLE);
			frameLocation.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			((EditText) findViewById(R.id.EditLocation)).setHint("Edit your location here");
		} else {
			frameLocation.setVisibility(LinearLayout.INVISIBLE);
			frameLocation.setLayoutParams(new LayoutParams(0, 0));
		}
	}
}
