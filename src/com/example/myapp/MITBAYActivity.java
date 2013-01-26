package com.example.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
/**
 * This class is the base class that will keep all of the preferences 
 * for the project
 * @author trannguyen
 *
 */
public class MITBAYActivity extends Activity {

	public static final String TEXTBOOK = "TEXTBOOK";
	public static final String FURNITURE= "FURNITURE";
	public static final String TRANSPORTATION = "TRANSPORTATION";
	public static final String MISC= "MISC";
	public static final String USERNAME="username";
	public static final String EMAIL="email";
	public static final String DATE="date";
	public static final String TYPE="type";
	public static final String DESCRIPTION="description";
	public static final String CONDITION="condition";
	public static final String PRICE="price";
	public static final String ITEM="item";
	public static final String ITEM_NAME="name";
	public static final String ID="id";
	public static final String IMAGE="pic";
	public static final String LOCATION = "location";
	// SharedPreferences settings = getSharePreferences(SETTING, 0);
	public static final String SETTING = "setting";
	public static final String NAME = "User name";
	// EMAIL is already declared 
	public static final String ADDRESS = "Address";
	public static final String PASSWORD = "Password";
	// Is already log in
	public static final String IS_ALREADY_LOG_IN = "is_already_log_in";
	public static final int MINIMUM_LENGTH_PASSWORD = 4;
	// Message
	public static final String MESSAGE = "Do you want to log in right now?";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitbay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mitbay, menu);
		return true;
	}
	
	/**
	 * Checking log in
	 * @return
	 */
	public boolean checkLogIn() {
		SharedPreferences settings = getSharedPreferences(SETTING, 0);
		String user_name = settings.getString(USERNAME, "");
		String pass = settings.getString(PASSWORD, "");
		ParseUser user = new ParseUser();
		user.setUsername(user_name);
		user.setPassword(pass);
		ParseUser.logInInBackground(user_name, pass, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				SharedPreferences settings = getSharedPreferences(SETTING, 0);
				SharedPreferences.Editor prefEditor = settings.edit();
				if (user != null) {
					if (user.getBoolean("emailVerified")!=true) {
						prefEditor.putBoolean(IS_ALREADY_LOG_IN, false);
						prefEditor.commit();
					} else {
						prefEditor.putBoolean(IS_ALREADY_LOG_IN, true);
						prefEditor.commit();
					}
				} else {
					prefEditor.putBoolean(IS_ALREADY_LOG_IN, false);
					prefEditor.commit();
				}
			}
		});
		return settings.getBoolean(IS_ALREADY_LOG_IN, false);
	}
	
	/**
	 * Require password has at least a minimum number of character
	 * @param password
	 * @return
	 */
	public boolean isValidPassword(String password) {
		if (password.length() < MINIMUM_LENGTH_PASSWORD) return false;
		else return true;
	}
	
	
	/**
	 * Check new password matching
	 * @param password
	 * @param confirmPassword
	 * @return
	 */
	public boolean isMatchPasswords(String password, String confirmPassword) {
		if (password.equals(confirmPassword)) return true;
		else return false;
	}
	
	/**
	 * Set user name cannot be an empty string
	 * @param user_name
	 * @return
	 */
	public boolean isValidUserName(String user_name) {
		if (user_name.length() == 0) return false;
		else return true;
	}
	
}
