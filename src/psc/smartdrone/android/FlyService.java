package psc.smartdrone.android;

import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;
import psc.smartdrone.asservissement.Program;
import psc.smartdrone.asservissement.SimInterface;
import psc.smartdrone.filtre.SensorsToPosition;
import psc.smartdrone.ioio.IOIOController;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/*
 * Service activated during the flight.
 */
public class FlyService extends /*IOIO*/Service {

	private SensorsToPosition mSensorsToPosition;
	private SensorListener mSensorListener;
	private SimInterface mInterface;
	private int mStartId;
	private IOIOController mController;
	private DataSender mDataSender;
	private Program mProgram;
	
	public FlyService() {
		mSensorsToPosition = new SensorsToPosition();
	}

	/*
	 * Start a new thread for monitoring events.
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d("FlyService", "onStartCommand()");
				
		//String dstIP = intent.getStringExtra(FlyActivity.IP_MESSAGE);

		mStartId = startId;
		mDataSender = new DataSender("84.99.63.234", 6157);
		
		mSensorListener = new SensorListener(this, mSensorsToPosition, mDataSender, "/storage/sdcard0/Documents/Logs/");
		//mSensorListener = new SensorListener(this, new DataSender("10.70.22.234", 6157), "/storage/sdcard0/Documents/Logs/");
		//mSensorListener = new SensorListener(this, new DataSender("192.168.44.204", 6157), "/storage/sdcard0/Documents/Logs/");
		//mSensorListener = new SensorListener(this, new DataSender("192.168.43.109", 6157), "/storage/sdcard0/Documents/Logs/");
		mSensorListener.start();
		
		return START_REDELIVER_INTENT;
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	/*
	 * Stop the service.
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {

		Log.d("FlyService", "onDestroy()");

		Toast.makeText(getApplicationContext(), "destroyed fly service", Toast.LENGTH_SHORT).show();
		
		if (mSensorListener != null)
			mSensorListener.close();		
		stopSelf(mStartId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		Log.d("FlyService", "onBind()");
		return null;
	}
	
	//@Override
	protected IOIOLooper createIOIOLooper() {
		if (mController == null) {
			mController = new IOIOController();
			mInterface = new SimInterface(mController, mSensorsToPosition);
			mProgram = new Program(mInterface);
			mProgram.initialisation();
		}
		return mController;
	}

}
