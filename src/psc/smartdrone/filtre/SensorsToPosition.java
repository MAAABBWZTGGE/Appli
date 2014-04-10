package psc.smartdrone.filtre;

import psc.smartdrone.asservissement.Convert;
import psc.smartdrone.asservissement.Vector3;
import psc.smartdrone.sensor.*;

/*
 * Convert sensors data to position, using filter.
 */
public class SensorsToPosition {
	
	Sensors mSensors;
	Position mPosition;
	Position mLastGPS;
	
	public SensorsToPosition() {
		mSensors = new Sensors();
		mPosition = new Position();
	}
	
	public void addAccel(Accel a) {
		mSensors.a.add(a);
	}
	
	public void addGyro(Gyro g) {
		mSensors.g.add(g);
	}
	
	public void addMagn(Magn m) {
		mSensors.m.add(m);
	}
	
	public void addOrient(Orient o) {
		mSensors.o.add(o);
	}
	
	public void addLocation(GPSLocation l) {
		mSensors.l.add(l);
	}
	
	
	public Position getPosition(long timestamp) {
		/*
		 * TODO: Fill.
		 * TODO: Take coords from the output of the filter.
		 * TODO: Use other sensors than only GPS.
		 * TODO: Add angles.
		 */
		
		// Extract GPS location.
		if (!mSensors.l.isEmpty()) {
			// Convert in local coordinates.
			GPSLocation gps = mSensors.l.lastElement();
			Vector3 p = Convert.gpsToLocal(gps);
			
			// Calculate average speed since last GPS.
			if (mLastGPS != null) {
				double dt = (timestamp - mLastGPS.timestamp) / (double)1000;
				mPosition.speed = (p.less(mLastGPS.pos)).div(dt);
			}
			
			// Set new position.
			mPosition.pos = p;
			
			// Save last GPS.
			mLastGPS = new Position();
			mLastGPS.pos = p;
			mLastGPS.timestamp = timestamp;
	    // Extrapolate with given speed.
		} else if (mPosition.speed != null) {
			double dt = (timestamp - mPosition.timestamp) / (double)1000;
			mPosition.pos.add(mPosition.speed.times(dt));
		}
		
		mSensors.clear();
		
		// Return estimated position.
		mPosition.timestamp = timestamp;
		return mPosition;
	}

}
