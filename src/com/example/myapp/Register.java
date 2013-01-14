package com.example.myapp;

import com.example.myapp.R;
import com.example.myapp.R.layout;
import com.example.myapp.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// Confirm Button
		Button confirm = (Button) findViewById(R.id.ConfirmButton_Register);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "register", Toast.LENGTH_SHORT).show();
				// User Name
				String userName = ((EditText) findViewById(R.id.UserName_Register)).getText().toString();
				// Email Address
				String email = ((EditText) findViewById(R.id.EmailAddress_Register)).getText().toString();
				// Password
				String password = ((EditText) findViewById(R.id.Password_Register)).getText().toString();
				// Confirm Password
				String confirmPassword = ((EditText) findViewById(R.id.ConfirmPassword_Register)).getText().toString();
				// Message Error Checking
				TextView errorMessage = (TextView) findViewById(R.id.MessageError_Register); 
				// Update
				if (!isValidEmail(email)) {
					errorMessage.setText("The email is not valid. Can you try again?");
				} else if (!isMatchPassword(password, confirmPassword)) {
					errorMessage.setText("The password and confirmation password do not match "+password + "  "+confirmPassword);
				} else {
					// Save data in SharedPreferences
					SharedPreferences settings = getSharedPreferences("sign in", 0);
					SharedPreferences.Editor prefEditor = settings.edit();
					prefEditor.putString("user name", userName);
					prefEditor.putString("email", email);
					prefEditor.putString("password", password);
					prefEditor.commit();
					// Move to ListItems action
					Intent i = new Intent(v.getContext(), ListItem.class);
					startActivity(i);
				}
				Toast.makeText(v.getContext(), "get email and password", Toast.LENGTH_LONG).show();
			}
		});
	}
	public boolean isValidEmail(String email) {
		// Need server
		return (System.currentTimeMillis() % 5 == 0);
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
