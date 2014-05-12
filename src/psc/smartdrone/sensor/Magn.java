package psc.smartdrone.sensor;

public class Magn {
	public double time;
	public float x, y, z;
	
	public Magn() {}
	public Magn(double t, float _x, float _y, float _z) {
		time = t;
		x = _x; y = _y; z = _z;
	}

	void print() {
		System.out.print("m:[" + time + "s] " + x + " " + y + " " + z + ",");
	}
}
