package psc.smartdrone.asservissement;

import psc.smartdrone.sensor.Orient;

/*
 * Class to convert all coordinates to a local map, in meters.
 * Given an origin (latitude, longitude), the axis are X toward East, Y toward North.
 * No correction is made to altitude given by GPS.
 */
public class Convert {
	
	// WGS84 Wikipedia, ellipsoid model of Earth (in meters).
	static final double mLongRadius = 6378137.0;
	static final double mShortRadius = 6356752.314245179497563967;
	//static final double mEarthRadius = (mLongRadius + mShortRadius) / 2;

	// Origin of local coordinates.
	static double mLat0, mLon0;
	static boolean mHasOrigin = false;

	/*
	public static void test() {
		Log.d("mLongRadius", String.valueOf(mLongRadius));
		Log.d("mShortRadius", String.valueOf(mShortRadius));
		// 0.081819190842622
		final double eccentricity = Math.sqrt(1 - Math.pow(mShortRadius / mLongRadius, 2));
		Log.d("mEccentricity", String.valueOf(eccentricity));
		
		double lat = 45;
		double lon = 5;
		double alt = 0;
		
		setOrigin(lat, lon);
		Vector3 tmp = gpsToLocal(lat + 0.01, lon + 0.01, alt);
	}
	//*/
	
	// Set origin of coordinates.
	public static void setOrigin(double lat, double lon) {
		mLat0 = lat * Math.PI / 180.0;
		mLon0 = lon * Math.PI / 180.0;
		mHasOrigin = true;
	}
	
	/*
	 * Convert GPS to local coordinates.
	 * We don't simply use the spherical coordinates because GPS coordinates are "geodetic".
	 * Error between the two systems is around 0.5% on the X axis.
	 * Altitude is not modified
	 */
	public static Vector3 gpsToLocal(double lat, double lon, double alt) {
		if (!mHasOrigin)
			setOrigin(lat, lon);
		
		lat *= Math.PI / 180.0;
		lon *= Math.PI / 180.0;

		double tan2Lat = Math.tan(lat); tan2Lat *= tan2Lat;
		double ratio = mShortRadius / mLongRadius;
		double ratio2 = ratio * ratio;

		//double x = mEarthRadius * Math.cos(lat) * (lon - mLon0);
		double x = (mLongRadius / Math.sqrt(1 + tan2Lat * ratio2)) * (lon - mLon0);

		//double y = mEarthRadius * (lat - mLat0);
		double y = (mShortRadius * ratio) * Math.pow((1 + tan2Lat) / (1 + tan2Lat * ratio2), 1.5)
				 * (lat - mLat0);
		
		return new Vector3(x, y, alt);
	}
	
	/*
	 * Convert vector into coordinates defined by orientation angles.
	 */
	public static Vector3 rotate(Orient o, Vector3 vect) {
		Matrix3 rotation = Matrix3.azimuth(o.azimuth)
				  .product(Matrix3.pitch(o.pitch))
				  .product(Matrix3.roll(o.roll));
		return rotation.product(vect);
	}
}
