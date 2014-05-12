package psc.smartdrone.sensor;

public class Accel {
	public double time;
	public float x, y, z;
	
	public Accel() {}
	public Accel(double t, float _x, float _y, float _z) {
		time = t;
		x = _x; y = _y; z = _z;
	}

	void print() {
		System.out.print("a:[" + time + "s] " + x + " " + y + " " + z + ",");
	}
}
