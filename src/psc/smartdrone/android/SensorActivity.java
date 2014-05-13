package psc.smartdrone.android;

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;

import psc.smartdrone.R;

/**
 * Activity to test sensors.
 * @author guillaume
 *
 */
public class SensorActivity extends Activity implements SensorEventListener, LocationListener, NmeaListener {

	private SensorManager mSensorManager;
	private Sensor mPressure;
	private Sensor mTemperature;
	private Sensor mHumidity;
	private Sensor mLight;
	private Sensor mMagneticField;
	private Sensor mOldOrientation;
	private Sensor mAcceleration;
	private Sensor mLinearAcceleration;
	private Sensor mGyroscope;
	private long mLastTimeGyroscope;
	private float[] mTotalRotation;
	
	private LocationManager mLocationManager;
	
	private float[] accel;
	private float[] magnet;
	private float[] gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_activity);

		TextView text = (TextView)findViewById(R.id.pressure);
		text.setText("pressure : unknown");
		text = (TextView)findViewById(R.id.light);
		text.setText("light : unknown");
		text = (TextView)findViewById(R.id.temperature);
		text.setText("temperature : unknown");
		text = (TextView)findViewById(R.id.humidity);
		text.setText("humidity : unknown");
		text = (TextView)findViewById(R.id.acceleration);
		text.setText("acceleration : unknown");
		text = (TextView)findViewById(R.id.linear_acceleration);
		text.setText("linear acceleration : unknown");
		text = (TextView)findViewById(R.id.new_orientation);
		text.setText("new orientation : unknown");
		text = (TextView)findViewById(R.id.old_orientation);
		text.setText("old orientation : unknown");
		text = (TextView)findViewById(R.id.magfield);
		text.setText("magnetic field : unknown");
		text = (TextView)findViewById(R.id.compass);
		text.setText("compass : unknown");
		text = (TextView)findViewById(R.id.gyroscope);
		text.setText("gyroscope : unknown");
		text = (TextView)findViewById(R.id.gps);
		text.setText("gps : unknown");
		text = (TextView)findViewById(R.id.gpsdop);
		text.setText("gpsdop : unknown");
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mOldOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		
		mLastTimeGyroscope = 0;
		mTotalRotation = new float[] {0, 0, 0};
		
		mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mOldOrientation, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		mLocationManager.addNmeaListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		mLocationManager.removeUpdates(this);
	}
	
	private float round(float num, float coeff) {
		return Math.round(num * coeff) / coeff;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor == mPressure)
		{
			TextView text = (TextView)findViewById(R.id.pressure);
			text.setText("pressure : " + round(event.values[0], 10) + " hPa");
		}
		if (event.sensor == mTemperature)
		{
			TextView text = (TextView)findViewById(R.id.temperature);
			text.setText("temperature : " + round(event.values[0], 10) + " °C");
		}
		if (event.sensor == mHumidity)
		{
			TextView text = (TextView)findViewById(R.id.humidity);
			text.setText("humidity : " + round(event.values[0], 10) + " %");
		}
		if (event.sensor == mLight)
		{
			TextView text = (TextView)findViewById(R.id.light);
			text.setText("light : " + round(event.values[0], 1) + " lx");
		}
		if (event.sensor == mMagneticField)
		{
			TextView text = (TextView)findViewById(R.id.magfield);
			text.setText("magnetic field :\n" + round(event.values[0], 10) + " µT\n" + round(event.values[1], 10) + " µT\n" + round(event.values[2], 10) + " µT");

			magnet = event.values.clone();
			if (accel != null)
				updateOrientation();
		}
		if (event.sensor == mOldOrientation)
		{
			TextView text = (TextView)findViewById(R.id.old_orientation);
			text.setText("old orientation :\n" + round(event.values[0], 1) + " °\n" + round(event.values[1], 1) + " °\n" + round(event.values[2], 1) + " °");
		}
		if (event.sensor == mAcceleration)
		{
			TextView text = (TextView)findViewById(R.id.acceleration);
			text.setText("acceleration :\n" + round(event.values[0], 100) + " m/s²\n" + round(event.values[1], 100) + " m/s²\n" + round(event.values[2], 100) + " m/s²");
			
			accel = event.values.clone();
			if (magnet != null)
				updateOrientation();
		}
		if (event.sensor == mLinearAcceleration)
		{
			TextView text = (TextView)findViewById(R.id.linear_acceleration);
			text.setText("linear acceleration :\n" + round(event.values[0], 100) + " m/s²\n" + round(event.values[1], 100) + " m/s²\n" + round(event.values[2], 100) + " m/s²");			
		}
		if (event.sensor == mGyroscope)
		{
			event.values[0] *= 180 / Math.PI;
			event.values[1] *= 180 / Math.PI;
			event.values[2] *= 180 / Math.PI;
			
			if (mLastTimeGyroscope != 0)
			{
				long diff = event.timestamp - mLastTimeGyroscope;
				double time = diff;
				time /= 1000000000;
				mTotalRotation[0] += event.values[0] * time;
				mTotalRotation[1] += event.values[1] * time;
				mTotalRotation[2] += event.values[2] * time;
			}
			mLastTimeGyroscope = event.timestamp;
			
			TextView text = (TextView)findViewById(R.id.gyroscope);
			text.setText("gyroscope :\n" + round(event.values[0], 10) + " °/s\n" + round(event.values[1], 10) + " °/s\n" + round(event.values[2], 10) + " °/s");
			
			text = (TextView)findViewById(R.id.gyrintegral);
			text.setText("integrated gyro :\n" + round(mTotalRotation[0], 1) + " °\n" + round(mTotalRotation[1], 1) + " °\n" + round(mTotalRotation[2], 1) + " °");
		}
	}
	
	private void updateOrientation() {
		float[] rotation = new float[16];
		float[] orient = new float[3];
		
		SensorManager.getRotationMatrix(rotation, null, accel, magnet);
		SensorManager.getOrientation(rotation, orient);

		TextView text = (TextView)findViewById(R.id.new_orientation);
		text.setText("new orientation :\n" + round((float)Math.toDegrees(orient[0]), 1) + " °\n" + round((float)Math.toDegrees(orient[1]), 1) + " °\n" + round((float)Math.toDegrees(orient[2]), 1) + " °");
		
		if (gps != null) {
			GeomagneticField geofield = new GeomagneticField(gps[0], gps[1], gps[2], System.currentTimeMillis());
			
			text = (TextView)findViewById(R.id.compass);
			text.setText("compass :\n" + round((float)(Math.toDegrees(orient[0]) + geofield.getDeclination()), 1) + " ° from N");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		TextView text = (TextView)findViewById(R.id.gps);
		text.setText("gps :\n" + location.getLatitude() + " °N\n" + location.getLongitude() + " °E\n" + location.getAltitude() + " m"
		+ "\n" + location.getSpeed() + " m/s\n+- " + location.getAccuracy() + " m\n" + location.getTime() + " ms since EPOCH\n");
		
		gps = new float[3];
		gps[0] = (float)location.getLatitude();
		gps[1] = (float)location.getLongitude();
		gps[2] = (float)location.getAltitude();
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		Log.d("NMEA received", nmea);

		if (nmea.startsWith("$GPGSA"))
		{
			String[] data = nmea.split("[,\\*]");
			
			TextView text = (TextView)findViewById(R.id.gpsdop);
			if (data.length >= 4)
				text.setText(data[data.length - 4] + " ; " + data[data.length - 3] + " ; " + data[data.length - 2]);
			else
				text.setText("NMEA received");
		}
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

}
