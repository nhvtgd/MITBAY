package com.login;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
	private SharedPreferences settings;
	private SharedPreferences.Editor prefEditor;
	private String email, username, password;
	private Button confirm;
	private TextView errorMessage;
	private long timeClick = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		// Animation
		makeAnimation();
		// SharedPreferences
		settings = getSharedPreferences(SETTING, 0);
		prefEditor = settings.edit();
		errorMessage = (TextView) findViewById(R.id.signInErrorMassage);
		// Parse Initialization
		Parse.initialize(this, "2TGrIyvNfLwNy3kM8OnZLAQGtSW2f6cR3k9oxHak",
				"Y8xlSKdSilJBepTNIJqthpbJ9KeppDWCdNUQdYFX");
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
				if (System.currentTimeMillis() - timeClick < 1000) return;
				else timeClick = System.currentTimeMillis();
				((TextView)findViewById(R.id.signInErrorMassage)).setText("");
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
						prefEditor.putBoolean(IS_ALREADY_LOG_IN, false);
						prefEditor.commit();
					} else {
						prefEditor.putBoolean(IS_ALREADY_LOG_IN, true);
						prefEditor.putString(EMAIL, user.getEmail());
						prefEditor.putString(USERNAME, user.getUsername());
						prefEditor.putString(PASSWORD, password);
						prefEditor.putString(USERID, user.getObjectId());
						prefEditor.commit();
						confirm.setEnabled(true);
						Intent i = new Intent(getApplicationContext(), ItemSelection.class);
						startActivity(i); 
					}
				} else {
					prefEditor.putBoolean(IS_ALREADY_LOG_IN, false);
					prefEditor.commit();
					errorMessage.setText("The username and password are not correct. Can you try again?");
				}
			}
		});
		return settings.getBoolean(IS_ALREADY_LOG_IN, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log_in, menu);
		return true;
	}
	
	/**
	 * Make animation move from right to left
	 */
	private void makeAnimation() {
		ViewGroup frame = (ViewGroup) findViewById(R.id.LogIn_LinearLayout);
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_to_left_log_in_page);
		for (int i=0; i<frame.getChildCount(); i++) {
			View child = frame.getChildAt(i);
			child.setAnimation(animation);
		}
		animation.start();
	}

}