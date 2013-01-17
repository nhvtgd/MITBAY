package com.login;

import com.example.myapp.ItemSelection;
import com.example.myapp.R;
import com.example.myapp.R.id;
import com.example.myapp.R.layout;
import com.example.myapp.R.menu;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		// Set backgroundColor is gray
		Button logIn = (Button) findViewById(R.id.signInConfirmButton);

		logIn.setBackgroundColor(Color.RED);
		// Check Remember the last password
		// SharedPreferences
		SharedPreferences settings = getSharedPreferences("sign in", 0);
		// Email Address
		EditText emailField = (EditText) findViewById(R.id.signInEmailAddress);
		String previousEmail = settings.getString("email", "");
		if (previousEmail != "") {
			emailField.setText(previousEmail);
		}
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
				Toast.makeText(v.getContext(), "sign in", Toast.LENGTH_SHORT)
						.show();
				// SharedPreferences
				SharedPreferences settings = getSharedPreferences("sign in", 0);
				SharedPreferences.Editor prefEditor = settings.edit();
				// Email Address
				EditText emailField = (EditText) findViewById(R.id.signInEmailAddress);
				String email = emailField.getText().toString();
				// Password
				EditText passwordField = (EditText) findViewById(R.id.signInPassword);
				String password = passwordField.getText().toString();
				// Update
				if (isValidLogIn(email, password)) {
					// Correct email and password
					prefEditor.putString("email", email);
					prefEditor.putString("password", password);
					prefEditor.commit();
					// Move to ListItems action
					Intent i = new Intent(v.getContext(), ItemSelection.class);
					startActivity(i);
				} else {
					// Incorrect email or password
					// Set message
					TextView errorMessage = (TextView) findViewById(R.id.signInErrorMassage);
					errorMessage
							.setText("The email and password are not correct. Can you try again?");
				}
			}
		});
	}

	public boolean isValidLogIn(String email, String password) {
		long time = System.currentTimeMillis();
		return (time % 4 == 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log_in, menu);
		return true;
	}

}
