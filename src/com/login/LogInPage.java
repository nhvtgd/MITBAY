package com.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.sax.EndTextElementListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapp.MITBAYActivity;
import com.example.myapp.R;

public class LogInPage extends MITBAYActivity {
	
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in_page);
		// Make animation
		makeStartAnimation();
		// Register
		Button register = (Button) findViewById(R.id.Register_LogInPage);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(v.getContext(), Register.class);
				startActivity(intent);
			}
		});
		// Log in
		Button logIn = (Button) findViewById(R.id.LogIn_LogInPage);
		logIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(v.getContext(), LogIn.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log_in_page, menu);
		return true;
	}
	
	private void makeStartAnimation() {
		ViewGroup frame = (ViewGroup) findViewById(R.id.Frame_LogInPage);
		Animation animation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_from_up_to_down_log_in_page);
		frame.startAnimation(animation);
	}
}

/* Need more work
 * 	Animation
 * 	MIT BAY logo
 */
