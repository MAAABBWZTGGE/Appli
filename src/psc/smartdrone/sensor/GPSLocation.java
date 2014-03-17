package psc.smartdrone.sensor;

public class GPSLocation {
	public float lat, lon, alt, speed, accuracy;

	public GPSLocation() {}
	public GPSLocation(float _lat, float _lon, float _alt, float _speed, float _accuracy) {
		lat = _lat; lon = _lon; alt = _alt; speed = _speed; accuracy = _accuracy;
	}
	
	void print() {
		System.out.print("l: " + lat + " " + lon + " " + alt + " " + speed + " " + accuracy + ",");
	}
}
