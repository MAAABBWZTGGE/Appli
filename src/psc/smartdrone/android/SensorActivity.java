package psc.smartdrone.android;

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
	private Sensor mOrientation;
	private Sensor mAcceleration;
	private Sensor mGyroscope;
	private long mLastTimeGyroscope;
	private float[] mTotalRotation;
	
	private LocationManager mLocationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_activity);

		TextView text = (TextView)findViewById(R.id.pressure);
		text.setText("pressure : unknown");
		text = (TextView)findViewById(R.id.temperature);
		text.setText("temperature : unknown");
		text = (TextView)findViewById(R.id.humidity);
		text.setText("humidity : unknown");
		text = (TextView)findViewById(R.id.light);
		text.setText("light : unknown");
		text = (TextView)findViewById(R.id.magfield);
		text.setText("magnetic field : unknown");
		text = (TextView)findViewById(R.id.orientation);
		text.setText("orientation : unknown");
		text = (TextView)findViewById(R.id.acceleration);
		text.setText("acceleration : unknown");
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
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
		mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
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

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor == mPressure)
		{
			TextView text = (TextView)findViewById(R.id.pressure);
			text.setText("pressure : " + event.values[0] + " hPa");
		}
		if (event.sensor == mTemperature)
		{
			TextView text = (TextView)findViewById(R.id.temperature);
			text.setText("temperature : " + event.values[0] + " °C");
		}
		if (event.sensor == mHumidity)
		{
			TextView text = (TextView)findViewById(R.id.humidity);
			text.setText("humidity : " + event.values[0] + " %");
		}
		if (event.sensor == mLight)
		{
			TextView text = (TextView)findViewById(R.id.light);
			text.setText("light : " + event.values[0] + " lx");
		}
		if (event.sensor == mMagneticField)
		{
			TextView text = (TextView)findViewById(R.id.magfield);
			text.setText("magnetic field :\n" + event.values[0] + " µT\n" + event.values[1] + " µT\n" + event.values[2] + " µT");
		}
		if (event.sensor == mOrientation)
		{
			TextView text = (TextView)findViewById(R.id.orientation);
			text.setText("orientation :\n" + event.values[0] + " °\n" + event.values[1] + " °\n" + event.values[2] + " °");
		}
		if (event.sensor == mAcceleration)
		{
			TextView text = (TextView)findViewById(R.id.acceleration);
			text.setText("acceleration :\n" + event.values[0] + " m/s²\n" + event.values[1] + " m/s²\n" + event.values[2] + " m/s²");
		}
		if (event.sensor == mGyroscope)
		{
			if (mLastTimeGyroscope != 0)
			{
				long diff = event.timestamp - mLastTimeGyroscope;
				double time = diff;
				time /= 1000000000;
				time *= 180;
				time /= Math.PI;
				mTotalRotation[0] += event.values[0] * time;
				mTotalRotation[1] += event.values[1] * time;
				mTotalRotation[2] += event.values[2] * time;
			}
			mLastTimeGyroscope = event.timestamp;
			
			TextView text = (TextView)findViewById(R.id.gyroscope);
			text.setText("gyroscope :\n" + event.values[0] + " rad/s\n" + event.values[1] + " rad/s\n" + event.values[2] + " rad/s");
			
			text = (TextView)findViewById(R.id.gyrintegral);
			text.setText(mTotalRotation[0] + " °\n" + mTotalRotation[1] + " °\n" + mTotalRotation[2] + " °");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		TextView text = (TextView)findViewById(R.id.gps);
		text.setText("gps :\n" + location.getLatitude() + " °N\n" + location.getLongitude() + " °E\n" + location.getAltitude() + " m"
		+ "\n" + location.getSpeed() + " m/s\n+- " + location.getAccuracy() + " m\n" + location.getTime() + " ms since EPOCH\n");
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
