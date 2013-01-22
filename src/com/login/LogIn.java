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
	private String email;
	private String password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		// SharedPreferences
		settings = getSharedPreferences("settings", 0);
		prefEditor = settings.edit();
		// Parse Initialization
		Parse.initialize(this, "2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");
		// Set backgroundColor is gray
		Button logIn = (Button) findViewById(R.id.signInConfirmButton);
		logIn.setBackgroundColor(Color.RED);
		// Check Remember the last password
		// Email Address
		EditText emailField = (EditText) findViewById(R.id.signInUsername);
		String previousEmail = settings.getString("user name", "");
		if (previousEmail != "") 	
			emailField.setText(previousEmail);
		// Password
		EditText passwordField = (EditText) findViewById(R.id.signInPassword);
		String previousPassword = settings.getString("password", "");
		if (previousPassword != "") {
			passwordField.setText(previousPassword);
		}
		// Confirm Button
		Button confirm = (Button) findViewById(R.id.signInConfirmButton);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Email Address
				EditText emailField = (EditText) findViewById(R.id.signInUsername);
				email = emailField.getText().toString();
				// Password
				EditText passwordField = (EditText) findViewById(R.id.signInPassword);
				password = passwordField.getText().toString();
				// Update
				isValidLogIn(email, password);
			}
		});
	}

	public boolean isValidLogIn(String email, String pass) {
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setPassword(pass);
		ParseUser.logInInBackground(email, pass, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					validLogin = true;
					prefEditor.putString("email", user.getEmail());
					prefEditor.putString("name", user.getUsername());
					prefEditor.putString("password", password);
					prefEditor.commit();
					Intent i = new Intent(getApplicationContext(), ItemSelection.class);
					startActivity(i); 
				} else {
					validLogin = false;
					TextView errorMessage = (TextView) findViewById(R.id.signInErrorMassage);
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