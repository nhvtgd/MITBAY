package Buy;

import com.example.myapp.R;
import com.example.myapp.R.layout;
import com.example.myapp.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Listing extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listing);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_listing, menu);
		return true;
	}

}
