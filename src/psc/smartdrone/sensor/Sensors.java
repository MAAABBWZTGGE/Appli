package psc.smartdrone.sensor;

public class Sensors {
	public int time;
	public Gyro[] g;
	public Accel[] a;
	public Magn[] m;
	public Orient[] o;
	public GPSLocation[] l;
	
	public Sensors() {
	}
	
	public Sensors(int i) {
		g = new Gyro[i];
		a = new Accel[i];
		m = new Magn[i];
		o = new Orient[i];
		l = new GPSLocation[i];
	}

}
