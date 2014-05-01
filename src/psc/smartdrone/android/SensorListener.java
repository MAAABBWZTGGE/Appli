package psc.smartdrone.android;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import psc.smartdrone.filtre.SensorsToPosition;
import psc.smartdrone.sensor.*;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

/**
 * Class which listen to events from the system and process them with a DataSender.
 * @author guillaume
 * 
 */
public class SensorListener implements SensorEventListener, LocationListener {

	private SensorsToPosition mSTP;
	private DataSender mDataSender;
	private boolean mStatus;
	private boolean mStarted;
	
	private SensorManager mSensorManager;
	private Sensor mAcceleration;
	private Sensor mGyroscope;
	private Sensor mPressure;
	private Sensor mMagneticField;
	private Sensor mOrientation;
	private LocationManager mLocationManager;
	
	private String mPathFileLog;
	private File mLogFile;
	private FileWriter mFileWriter;
	
	/*
	 * Configures a DataSender and a file to write events.
	 */
	public SensorListener(Context context, SensorsToPosition stp, DataSender sender, String pathFileLog) {
		mSTP = stp;
		mDataSender = sender;

		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		mPathFileLog = pathFileLog;
		mLogFile = null;
		mFileWriter = null;

		mStatus = false;
		mStarted = false;
	}
	
	/*
	 * Start the DataSender (open a socket on network) and start listening to events.
	 */
	public void start() {
		
		Log.d("SensorListener", "start()");
		
		if (!mStarted) {
			mDataSender.start();
			resume();
			mStarted = true;
		}
	}
	
	/*
	 * Stop listening to events and close file.
	 */
	public void pause() {
		
		Log.d("SensorListener", "pause()");
		
		if (mStatus) {
			try {
				mFileWriter.flush();
				mFileWriter.close();
				mLogFile = null;
				mFileWriter = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mDataSender.close();
			mSensorManager.unregisterListener(this);
			mLocationManager.removeUpdates(this);
			mStatus = false;
		}
	}

	/*
	 * Stop listening to events, close file and socket.
	 */
	public void close() {
		
		Log.d("SensorListener", "close()");
		
		pause();
		mDataSender.close();
	}
	
	/*
	 * Create a new file and start listening to events.
	 */
	public void resume() {
		
		Log.d("SensorListener", "resume()");
		
		if (!mStatus) {
			try {
				File dir = new File(mPathFileLog);
				dir.mkdirs();
				
				String fmt = "yyyy-MM-dd HH:mm:ss";
				SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.US);
				String date = String.format("%s", sdf.format(new Date()));
				
				mLogFile = File.createTempFile(date + "~", ".txt", dir);
				mFileWriter = new FileWriter(mLogFile);

				Log.d("SensorListener : resume", mLogFile.getAbsolutePath());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mLogFile = null;
				mFileWriter = null;
			}

			mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_UI);//*
			mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_UI);
			// N/A for Galaxy S3 mini.
			mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_UI);
			// Stops when screen is locked...
			mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_UI);
			mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_UI);//*/
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			
			mStatus = true;
		}
	}

	/*
	 * Called by Android each time a new event comes.
	 * Write data to file and send packet on the network.
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		try {
			if (event.sensor == mAcceleration) {
				//Log.d("acceleration", String.valueOf(event.timestamp / 1000000000.0));

				Accel a = new Accel(event.values[0], event.values[1], event.values[2]);
				mSTP.addAccel(a);
				
				mDataSender.sendPaquet(Paquet.makeAcceleration(event.timestamp, a));
				if (mFileWriter != null)
					mFileWriter.write("a:" + (event.timestamp / 1000000000.0) + ":" + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
			} else if (event.sensor == mGyroscope) {
				//Log.d("gyro", String.valueOf(event.timestamp / 1000000000.0));

				Gyro g = new Gyro(event.values[0], event.values[1], event.values[2]);
				mSTP.addGyro(g);
				
				mDataSender.sendPaquet(Paquet.makeGyroscope(event.timestamp, g));
				if (mFileWriter != null)
					mFileWriter.write("g:" + (event.timestamp / 1000000000.0) + ":" + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
			} else if (event.sensor == mPressure) {
				//Log.d("pressure", String.valueOf(event.timestamp / 1000000000.0));
				if (mFileWriter != null)
					mFileWriter.write("p:" + (event.timestamp / 1000000000.0) + ":" + event.values[0] + "\n");
			} else if (event.sensor == mMagneticField) {
				//Log.d("magnetic field", String.valueOf(event.timestamp / 1000000000.0));

				Magn m = new Magn(event.values[0], event.values[1], event.values[2]);
				mSTP.addMagn(m);
				
				mDataSender.sendPaquet(Paquet.makeMagneticField(event.timestamp, m));
				if (mFileWriter != null)
					mFileWriter.write("m:" + (event.timestamp / 1000000000.0) + ":" + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
			} else if (event.sensor == mOrientation) {
				//Log.d("orientation", String.valueOf(event.timestamp / 1000000000.0));

				Orient o = new Orient(event.values[0], event.values[1], event.values[2]);
				mSTP.addOrient(o);
				
				mDataSender.sendPaquet(Paquet.makeOrientation(event.timestamp, o));
				if (mFileWriter != null)
					mFileWriter.write("o:" + (event.timestamp / 1000000000.0) + ":" + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Called by Android each time a new location event comes.
	 * Write data to file and send packet on the network.
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		try {
			//Log.d("location", String.valueOf(location.getTime() / 1000.0));
			long timeStampSinceEpoch = location.getTime();
			long timeStampSinceBoot = timeStampSinceEpoch + SystemClock.uptimeMillis() - System.currentTimeMillis();
			GPSLocation l = new GPSLocation((float)location.getLatitude(), (float)location.getLongitude(), (float)location.getAltitude(), location.getSpeed(), location.getAccuracy());
			mSTP.addLocation(l);
			
			mDataSender.sendPaquet(Paquet.makeLocation(timeStampSinceBoot * 1000000, l));
			if (mFileWriter != null)
				mFileWriter.write("\nl:" + (timeStampSinceBoot / 1000.0) + ":" + location.getLatitude() + "," + location.getLongitude() + "," + location.getAltitude() + ":" + location.getSpeed() + ":" + location.getAccuracy() + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("onProviderDisabled()", provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("onProviderEnabled()", provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.d("onStatusChanged()", provider + " : " + status);
	}
	
}
