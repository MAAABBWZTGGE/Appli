package psc.smartdrone.sensor;

public class Gyro {
	public float x, y, z;

	public Gyro() {}
	public Gyro(float _x, float _y, float _z) {
		x = _x; y = _y; z = _z;
	}
	
	void print() {
		System.out.print("g: " + x + " " + y + " " + z + ",");
	}
}
