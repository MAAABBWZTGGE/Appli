package psc.smartdrone.sensor;

public class Accel {
	public float x, y, z;
	
	public Accel() {}
	public Accel(float _x, float _y, float _z) {
		x = _x; y = _y; z = _z;
	}

	void print() {
		System.out.print("a: " + x + " " + y + " " + z + ",");
	}
}
