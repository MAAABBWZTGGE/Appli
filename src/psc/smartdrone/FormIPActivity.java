package psc.smartdrone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Form to get the IP address to which we send packets.
 * @author guillaume
 *
 */
public class FormIPActivity extends Activity {
	
	public final static String IP_MESSAGE = "psc.smartdrone.MESSAGE.IP";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_ip);
	}
	
	/*
	 * Called when the user press button "valider".
	 */
	public void validateIP(View view) {
		EditText edit = (EditText)findViewById(R.id.editTextIP);
		String dstIP = edit.getText().toString();
		Log.d("IP", dstIP);

		Intent intent = new Intent(this, FlyActivity.class);
		intent.putExtra(IP_MESSAGE, dstIP);
		startActivity(intent);
	}
	
}
