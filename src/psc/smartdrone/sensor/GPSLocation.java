package psc.smartdrone.sensor;

public class GPSLocation {
	public double time;
	public float lat, lon, alt, speed, accuracy;

	public GPSLocation() {}
	public GPSLocation(double t, float _lat, float _lon, float _alt, float _speed, float _accuracy) {
		time = t;
		lat = _lat; lon = _lon; alt = _alt; speed = _speed; accuracy = _accuracy;
	}
	
	void print() {
		System.out.print("l:[" + time + "ms] " + lat + " " + lon + " " + alt + " " + speed + " " + accuracy + ",");
	}
}
