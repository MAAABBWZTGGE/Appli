package psc.smartdrone;

public class Vector3 {
	double x;
	double y;
	double z;
	
	
	public Vector3(double x, double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public double Scalar(Vector3 v) {
		return x*v.x+y*v.y+z*v.z;
	}
	
	public Vector3 Vectorial(Vector3 v) {
		double xx = y*v.z-z*v.y;
		double yy = z*v.x-x*v.z;
		double zz = x*v.y-y*v.x;
		return new Vector3(xx,yy,zz);
	}
	
	public double Norm() {
		return Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
	}
	
	public double normScalar(Vector3 v) {
		return Scalar(v)/(Norm()*v.Norm());
	}
	
	public double normVectorial(Vector3 v) {
		return Vectorial(v).Norm()/(Norm()*v.Norm());
	}
}
