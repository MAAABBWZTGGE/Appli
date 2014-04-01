package psc.smartdrone.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import psc.smartdrone.R;

/**
 * Form to get the IP address to which we send packets.
 * @author guillaume
 *
 */
public class FlyActivity extends Activity {
	
	public final static String IP_MESSAGE = "psc.smartdrone.MESSAGE.IP";
	static Intent mService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fly_activity);

		Button button = (Button)findViewById(R.id.buttonIP);
		if (mService == null) {
			button.setText("Demarrer");
		} else {
			button.setText("Arreter");
		}
	}
	
	/*
	 * Called when the user press button "start/stop".
	 */
	public void toggle(View view) {
		Button button = (Button)findViewById(R.id.buttonIP);
		
		if (mService == null) {
			EditText edit = (EditText)findViewById(R.id.editTextIP);
			String dstIP = edit.getText().toString();
			Log.d("IP", dstIP);

			Toast.makeText(getApplicationContext(), "new fly service", Toast.LENGTH_SHORT).show();
	
			mService = new Intent(this, FlyService.class);
			mService.putExtra(IP_MESSAGE, dstIP);
			startService(mService);
			
			button.setText("Arreter");
		} else {
			stopService(mService);
			mService = null;

			button.setText("Demarrer");
		}
	}
	
}
