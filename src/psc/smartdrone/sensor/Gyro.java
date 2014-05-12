package psc.smartdrone.sensor;

public class Gyro {
	public double time;
	public float x, y, z;
	
	public Gyro() {}
	public Gyro(double t, float _x, float _y, float _z) {
		time = t;
		x = _x; y = _y; z = _z;
	}

	void print() {
		System.out.print("g:[" + time + "ms] " + x + " " + y + " " + z + ",");
	}
}
