package com.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapp.ItemSelection;
import com.example.myapp.MITBAYActivity;
import com.example.myapp.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
/**
 * Log In Page, remembered email password in SharedPreferences (optional)
 * @author Duy  
 * get email, password
 */
public class LogIn extends MITBAYActivity {
	private boolean validLogin;
	private SharedPreferences settings;
	private SharedPreferences.Editor prefEditor;
	private String email, username, password;
	Button confirm;
	TextView errorMessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		// SharedPreferences
		settings = getSharedPreferences(SETTING, 0);
		prefEditor = settings.edit();
		errorMessage = (TextView) findViewById(R.id.signInErrorMassage);
		// Parse Initialization
		Parse.initialize(this, "2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");
		// Set backgroundColor is gray
		Button logIn = (Button) findViewById(R.id.signInConfirmButton);
		logIn.setBackgroundColor(Color.RED);
		// Check Remember the last password
		// Email Address
		EditText usernameField = (EditText) findViewById(R.id.signInUsername);
		String previousUserName = settings.getString(USERNAME, "");
		if (previousUserName != "") 	
			usernameField.setText(previousUserName);
		// Password
		EditText passwordField = (EditText) findViewById(R.id.signInPassword);
		String previousPassword = settings.getString(PASSWORD, "");
		if (previousPassword != "")
			passwordField.setText(previousPassword);
		// Confirm Button
		confirm = (Button) findViewById(R.id.signInConfirmButton);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((TextView)findViewById(R.id.signInErrorMassage)).setText("");
				// Firstly deactivate the button
				confirm.setEnabled(false);
				// Email Address
				EditText usernameField = (EditText) findViewById(R.id.signInUsername);
				username = usernameField.getText().toString();
				// Password
				EditText passwordField = (EditText) findViewById(R.id.signInPassword);
				password = passwordField.getText().toString();
				// Update
				isValidLogIn(username, password);
			}
		});
	}

	public boolean isValidLogIn(String user_name, String pass) {
		ParseUser user = new ParseUser();
		user.setUsername(user_name);
		user.setPassword(pass);
		ParseUser.logInInBackground(user_name, pass, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					if (user.getBoolean("emailVerified")!=true) {
						errorMessage.setText("You have not verified email " + user.getEmail());
						validLogin = false;
					} else {
						validLogin = true;
						prefEditor.putString(EMAIL, user.getEmail());
						prefEditor.putString(USERNAME, user.getUsername());
						prefEditor.putString(PASSWORD, password);
						prefEditor.commit();
						confirm.setEnabled(true);
						Intent i = new Intent(getApplicationContext(), ItemSelection.class);
						startActivity(i); 
					}
				} else {
					confirm.setEnabled(true);
					validLogin = false;
					errorMessage.setText("The username and password are not correct. Can you try again?");
				}
			}
		});
		return validLogin;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log_in, menu);
		return true;
	}

}