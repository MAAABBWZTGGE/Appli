package psc.smartdrone.sensor;

public class Orient {
	public float azimuth, pitch, roll;

	public Orient() {}
	public Orient(float _azimuth, float _pitch, float _roll) {
		azimuth = _azimuth; pitch = _pitch; roll = _roll;
	}
	
	void print() {
		System.out.print("o: " + azimuth + " " + pitch + " " + roll + ",");
	}
}
