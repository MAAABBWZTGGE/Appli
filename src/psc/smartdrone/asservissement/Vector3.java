package psc.smartdrone.asservissement;

public class Vector3 {
	public double x;
	public double y;
	public double z;
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double scalar(Vector3 v) {
		return x*v.x + y*v.y + z*v.z;
	}
	
	public Vector3 vectorial(Vector3 v) {
		return new Vector3(
				y*v.z - z*v.y,
				z*v.x - x*v.z,
				x*v.y - y*v.x);
	}
	
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public double normScalar(Vector3 v) {
		return scalar(v) / (norm() * v.norm());
	}
	
	public double normVectorial(Vector3 v) {
		return vectorial(v).norm() / (norm() * v.norm());
	}
}
