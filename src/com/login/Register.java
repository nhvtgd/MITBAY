package com.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.MITBAYActivity;
import com.example.myapp.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Register extends MITBAYActivity {
	
	private boolean registered;
	private SharedPreferences settings;
	private SharedPreferences.Editor prefEditor;
	private String email, username, password;
	private Button confirm;
	private long timeClick = System.currentTimeMillis();
	TextView errorMessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// Parse Initialization
		Parse.initialize(this, "2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");
		// Initial Error Mesage text
		errorMessage = (TextView) findViewById(R.id.MessageError_Register);
		// Confirm Button
		confirm = (Button) findViewById(R.id.ConfirmButton_Register);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Limit one click each second
				if (System.currentTimeMillis() - timeClick < 1000) return;
				else timeClick = System.currentTimeMillis();
				// Clear error message
				errorMessage.setText("");
				// User Name
				username = ((EditText) findViewById(R.id.UserName_Register)).getText().toString();
				// Email Address
				email = ((EditText) findViewById(R.id.EmailAddress_Register)).getText().toString();
				// Password
				password = ((EditText) findViewById(R.id.Password_Register)).getText().toString();
				// Confirm Password
				String confirmPassword = ((EditText) findViewById(R.id.ConfirmPassword_Register)).getText().toString();
				// Update
				if (checkValidInput(username, password, confirmPassword))
						registerUserToParse(username, password, email);
			}
		});
	}
	
	private boolean checkValidInput(String username, String password, String confirmPassword) {
		String text = "";
		if (!isValidUserName(username)) {
			text = "Your user name cannot be empty";
			errorMessage.setText(text);
			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
			return false;
		} else if (!isMatchPasswords(password, confirmPassword)) {
			text = "The password and confirmation password do not match";
			errorMessage.setText(text);
			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
			return false;
		} else if (!isValidPassword(password)) {
			text = "Your password should have at least "+MINIMUM_LENGTH_PASSWORD+" characters";
			errorMessage.setText(text);
			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public boolean registerUserToParse(String name, String pass,
			String user_email) {
		ParseUser user = new ParseUser();
		user.setUsername(name);
		user.setPassword(pass);
		user.setEmail(user_email);
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(),"A verification email has been sent to your inbox. Please check it!",Toast.LENGTH_LONG).show();
					// Save data in SharedPreferences
					settings = getSharedPreferences(SETTING, 0);
					SharedPreferences.Editor prefEditor = settings.edit();
					prefEditor.putString(USERNAME, username);
					prefEditor.putString(EMAIL, email);
					prefEditor.putString(PASSWORD, password);
					prefEditor.commit();
					// Move to ListItems action
					Intent i = new Intent(getApplicationContext(), LogIn.class);
					startActivity(i);
					registered = true;
				} else {
					registered = false;
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		return registered;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}
}

/* Need more work
 * 	Theme
 * 	Location
 * 	Register button
 */
