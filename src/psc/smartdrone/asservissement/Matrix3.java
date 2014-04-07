package psc.smartdrone.asservissement;

public class Matrix3 {
	
	private double[][] m;
	
	public Matrix3() {
		m = new double[3][3];
	}
	
	public Vector3 product(Vector3 src) {
		double x = m[0][0] * src.x + m[0][1] * src.y + m[0][2] * src.z;
		double y = m[1][0] * src.x + m[1][1] * src.y + m[1][2] * src.z;
		double z = m[2][0] * src.x + m[2][1] * src.y + m[2][2] * src.z;
		
		return new Vector3(x, y, z);
	}
	
	public Matrix3 product(Matrix3 mat) {
		Matrix3 res = new Matrix3();
		
		for (int i = 0 ; i < 3 ; ++i)
			for (int j = 0 ; j < 3 ; ++j)
				res.m[i][j] = m[i][0] * mat.m[0][j] + m[i][1] * mat.m[1][j] + m[i][2] * mat.m[2][j];
		
		return res;
	}
	
	
	public static Matrix3 azimuth(double angle) {
		Matrix3 result = new Matrix3();
		result.setAzimuth(angle);
		return result;
	}
	
	public static Matrix3 pitch(double angle) {
		Matrix3 result = new Matrix3();
		result.setPitch(angle);
		return result;
	}
	
	public static Matrix3 roll(double angle) {
		Matrix3 result = new Matrix3();
		result.setRoll(angle);
		return result;
	}

	
	private void setAzimuth(double angle) {
		angle *= Math.PI / 180.0;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		
		m[0][0] = c;
		m[0][1] = s;
		m[0][2] = 0;

		m[1][0] = -s;
		m[1][1] = c;
		m[1][2] = 0;

		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = 1;
	}

	private void setPitch(double angle) {
		angle *= Math.PI / 180.0;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		
		m[0][0] = 1;
		m[0][1] = 0;
		m[0][2] = 0;

		m[1][0] = 0;
		m[1][1] = c;
		m[1][2] = s;

		m[2][0] = 0;
		m[2][1] = -s;
		m[2][2] = c;
	}

	private void setRoll(double angle) {
		angle *= Math.PI / 180.0;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		
		m[0][0] = c;
		m[0][1] = 0;
		m[0][2] = s;

		m[1][0] = 0;
		m[1][1] = 1;
		m[1][2] = 0;

		m[2][0] = -s;
		m[2][1] = 0;
		m[2][2] = c;
	}
	
}
