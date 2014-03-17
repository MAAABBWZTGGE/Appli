package psc.smartdrone.sensor;

public class DataOfLog {
	public int time;
	public int nb = 0;
	public Gyro g = new Gyro();
	public Accel a = new Accel();
	public Magn m = new Magn();
	public Orient o = new Orient();
	public GPSLocation l = new GPSLocation();
	public float p = 0;
	
	void print() {
		System.out.print(time + ":: ");
		g.print();
		a.print();
		m.print();
		o.print();
		l.print();
		System.out.println();
	}
}
