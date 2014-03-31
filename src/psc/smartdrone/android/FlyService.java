package psc.smartdrone.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/*
 * Service activated during the flight.
 */
public class FlyService extends Service {

	private SensorListener mSensorListener;
	private int mStartId;

	/*
	 * Start a new thread for monitoring events.
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d("FlyService", "onStartCommand()");
		
		String dstIP = intent.getStringExtra(FlyActivity.IP_MESSAGE);

		mStartId = startId;
		mSensorListener = new SensorListener(this, new DataSender(dstIP, 6157), "/storage/sdcard0/Documents/Logs/");
		//mSensorListener = new SensorListener(this, new DataSender("10.70.22.234", 6157), "/storage/sdcard0/Documents/Logs/");
		//mSensorListener = new SensorListener(this, new DataSender("192.168.44.204", 6157), "/storage/sdcard0/Documents/Logs/");
		//mSensorListener = new SensorListener(this, new DataSender("192.168.43.109", 6157), "/storage/sdcard0/Documents/Logs/");
		mSensorListener.start();
		
		return START_REDELIVER_INTENT;
	}

	/*
	 * Stop the service.
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {

		Log.d("FlyService", "onDestroy()");

		Toast.makeText(getApplicationContext(), "destroyed fly service", Toast.LENGTH_SHORT).show();
		
		mSensorListener.close();
		stopSelf(mStartId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		Log.d("FlyService", "onBind()");
		
		// TODO Auto-generated method stub
		return null;
	}

}
