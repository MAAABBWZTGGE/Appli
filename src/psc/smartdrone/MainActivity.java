package psc.smartdrone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Main activity
 * @author guillaume
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}

	/*
	 * Called when user press button.
	 */
	public void launchFly(View view) {
		startActivity(new Intent(this, FormIPActivity.class));
	}

	public void launchSensors(View view) {
		startActivity(new Intent(this, SensorActivity.class));
	}
	
}
