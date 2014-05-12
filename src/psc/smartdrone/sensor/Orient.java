package psc.smartdrone.sensor;

public class Orient {
	public double time;
	public float azimuth, pitch, roll;

	public Orient() {}
	public Orient(double t, float _azimuth, float _pitch, float _roll) {
		time = t;
		azimuth = _azimuth; pitch = _pitch; roll = _roll;
	}
	
	void print() {
		System.out.print("o:[" + time + "s] " + azimuth + " " + pitch + " " + roll + ",");
	}
}
