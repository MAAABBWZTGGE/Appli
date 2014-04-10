package psc.smartdrone.sensor;

import java.util.Vector;

public class Sensors {
	public int time;
	public Vector<Gyro> g;
	public Vector<Accel> a;
	public Vector<Magn> m;
	public Vector<Orient> o;
	public Vector<GPSLocation> l;
	
	public Sensors() {
		g = new Vector<Gyro>();
		a = new Vector<Accel>();
		m = new Vector<Magn>();
		o = new Vector<Orient>();
		l = new Vector<GPSLocation>();
	}

}
