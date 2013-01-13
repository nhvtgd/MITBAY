package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * This class will be the first screen to show an animation to transition to the
 * Item Selection Page
 * 
 * @author trannguyen
 * 
 */
public class SplashScreen extends Activity {

	private static final int SPLASH_DISPLAY_TIME = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		// Get the view and start the animation
		ImageView logo_image = (ImageView) findViewById(R.id.logo_image);
		Animation logo_anim = AnimationUtils
				.loadAnimation(this, R.anim.fade_in);
		logo_image.startAnimation(logo_anim);

		// Can do the same as the above, but want to have cascading effect
		// so go to every table row and make it spin using
		// LayoutAnimationController
		// no need to start Animation since it's already taken care of
		Animation spinin = AnimationUtils.loadAnimation(this,
				R.anim.custom_anim);
		LayoutAnimationController controller = new LayoutAnimationController(
				spinin);
		TableLayout table = (TableLayout) findViewById(R.id.TableLayout_Splash);
		for (int i = 0; i < table.getChildCount(); i++) {
			TableRow row = (TableRow) table.getChildAt(i);
			row.setLayoutAnimation(controller);
		}

		TextView description_text = (TextView) findViewById(R.id.description_text);
		Animation description_anim = AnimationUtils.loadAnimation(this,
				R.anim.fade_in);
		description_text.startAnimation(description_anim);

		TextView version_text = (TextView) findViewById(R.id.version_text);
		Animation version_anim = AnimationUtils.loadAnimation(this,
				R.anim.fade_in2);
		version_text.startAnimation(version_anim);

		// This is the last animation so need to make the transition
		// by calling on Transition end
		version_anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
			// A Handler to make the fade in and fade out effect for better transition
				new Handler().postDelayed(new Runnable() {
					public void run() {

						Intent intent = new Intent(SplashScreen.this,
								ItemSelection.class);
						SplashScreen.this.startActivity(intent);
						SplashScreen.this.finish();

						// transition from splash to main menu
						overridePendingTransition(R.anim.fade_in,
								R.anim.fade_out);

					}
				}, SPLASH_DISPLAY_TIME);
			}
		});

	}

	/*Since Animation is costly, we need to clear the Animation if 
	 * the user decide to stop or pause it*/
	@Override
	protected void onPause() {
		super.onPause();

		ImageView logo_image = (ImageView) findViewById(R.id.logo_image);
		logo_image.clearAnimation();
		TableLayout table = (TableLayout) findViewById(R.id.TableLayout_Splash);
		for (int i = 0; i < table.getChildCount(); i++) {
			TableRow row = (TableRow) table.getChildAt(i);
			row.clearAnimation();
		}
		TextView description_text = (TextView) findViewById(R.id.description_text);
		description_text.clearAnimation();
		TextView version_text = (TextView) findViewById(R.id.description_text);
		version_text.clearAnimation();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
		return true;
	}

}
