package psc.smartdrone.sensor;

public class Magn {
	public float x, y, z;

	public Magn() {}
	public Magn(float _x, float _y, float _z) {
		x = _x; y = _y; z = _z;
	}
	
	void print() {
		System.out.print("m: " + x + " " + y + " " + z + ",");
	}
}
