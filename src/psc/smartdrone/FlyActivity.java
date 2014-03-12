package psc.smartdrone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
 * Activity activated during the flight.
 */
public class FlyActivity extends Activity {
	
	Intent mService;
	
	public void stopFly(View view) {
		stopService(mService);
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fly_activity);
		
		Intent intent = getIntent();
		String dstIP = intent.getStringExtra(FormIPActivity.IP_MESSAGE);

		mService = new Intent(this, FlyService.class);
		mService.putExtra(FormIPActivity.IP_MESSAGE, dstIP);
		startService(mService);
	}
	
}
