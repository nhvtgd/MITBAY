package com.login;

import com.example.myapp.R;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	private boolean registered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// Parse Initialization
		Parse.initialize(this, "2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");
		// Confirm Button
		Button confirm = (Button) findViewById(R.id.ConfirmButton_Register);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// User Name
				String userName = ((EditText) findViewById(R.id.UserName_Register))
						.getText().toString();
				// Email Address
				String email = ((EditText) findViewById(R.id.EmailAddress_Register))
						.getText().toString();
				// Password
				String password = ((EditText) findViewById(R.id.Password_Register))
						.getText().toString();
				// Confirm Password
				String confirmPassword = ((EditText) findViewById(R.id.ConfirmPassword_Register))
						.getText().toString();
				// Message Error Checking
				TextView errorMessage = (TextView) findViewById(R.id.MessageError_Register);
				// Update
				if (!isMatchPassword(password, confirmPassword)) {
					errorMessage
							.setText("The password and confirmation password do not match "
									+ password + "  " + confirmPassword);
				} else if (!registerUserToParse(userName, password, email)) {
					errorMessage
							.setText("Failure to register to server. Either duplicate username or check email.");

				} else {

					Toast.makeText(
							getApplicationContext(),
							"A verification email has been sent to your inbox. Please check it!",
							Toast.LENGTH_LONG).show();

					// Save data in SharedPreferences
					SharedPreferences settings = getSharedPreferences(
							"sign in", 0);
					SharedPreferences.Editor prefEditor = settings.edit();
					prefEditor.putString("user name", userName);
					prefEditor.putString("email", email);
					prefEditor.putString("password", password);
					prefEditor.commit();
					// Move to ListItems action
					Intent i = new Intent(v.getContext(), LogIn.class);
					startActivity(i);
				}
			}
		});
	}

	public boolean registerUserToParse(String name, String password,
			String email) {
		ParseUser user = new ParseUser();
		user.setUsername(name);
		user.setPassword(password);
		user.setEmail(email);
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					registered = true;
				} else {
					registered = false;
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		return registered;
	}

	public boolean isMatchPassword(String password, String confirmPassword) {
		return (password.equals(confirmPassword));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}
}
