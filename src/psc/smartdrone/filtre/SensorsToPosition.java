package psc.smartdrone.filtre;

import psc.smartdrone.sensor.*;

/*
 * Convert sensors data to position, using filter.
 */
public class SensorsToPosition {
	
	Sensors mSensors;
	
	public SensorsToPosition() {
		mSensors = new Sensors();
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
	
	
	public Position getPosition() {
		//TODO: Fill. Must take coords from the output of the filter
	}

}
