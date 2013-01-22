package com.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapp.MITBAYActivity;
import com.example.myapp.R;

public class LogInPage extends MITBAYActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in_page);
		// Register
		Button register = (Button) findViewById(R.id.Register_LogInPage);
		register.setBackgroundColor(255218185);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "register", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(v.getContext(), Register.class);
				startActivity(i);
			}
		});
		// Log in
		Button logIn = (Button) findViewById(R.id.LogIn_LogInPage);
		logIn.setBackgroundColor(Color.RED); 	
		logIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "log in", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(v.getContext(), LogIn.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log_in_page, menu);
		return true;
	}

}