package psc.smartdrone;

import java.nio.ByteBuffer;

//import android.util.Log;

/**
 * Packet of data to be sent on a TCP connection.
 * @author guillaume
 *
 */
public class Paquet {

	public byte[] mData;
	
	/*
	 * Build packet for acceleration sensor.
	 */
	static Paquet makeAcceleration(long timestamp, float x, float y, float z) {
		byte[] data = new byte[23];
		
		/*
		Log.d("acceleration", timestamp + " : " + x + ", " + y + ", " + z);
		//*/
		
		// size
		data[0] = 0;
		data[1] = (byte) (data.length - 2);
		
		// acceleration
		data[2] = 0;
		
		// conversion -> bytes
		ByteBuffer buffer = ByteBuffer.wrap(data, 3, 20);
		// time stamp
		buffer.putLong(timestamp);
		// x, y, z
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.putFloat(z);
		
		/*
		String content = new String();
		for (int i = 0 ; i < data.length ; ++i)
			content = content + String.format("%02X", data[i]) + ",";
		Log.d("acceleration", content);
		//*/
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}

	/*
	 * Build packet for gyro sensor.
	 */
	static Paquet makeGyroscope(long timestamp, float x, float y, float z) {
		byte[] data = new byte[23];
		
		/*
		Log.d("gyroscope", timestamp + " : " + x + ", " + y + ", " + z);
		//*/
		
		// size
		data[0] = 0;
		data[1] = (byte) (data.length - 2);
		
		// gyroscope
		data[2] = 1;
		
		// conversion -> bytes
		ByteBuffer buffer = ByteBuffer.wrap(data, 3, 20);
		// time stamp
		buffer.putLong(timestamp);
		// x, y, z
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.putFloat(z);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}

	/*
	 * Build packet for orientation sensor.
	 */
	static Paquet makeOrientation(long timestamp, float x, float y, float z) {
		byte[] data = new byte[23];
		
		/*
		Log.d("gyroscope", timestamp + " : " + x + ", " + y + ", " + z);
		//*/
		
		// size
		data[0] = 0;
		data[1] = (byte) (data.length - 2);
		
		// orientation
		data[2] = 4;
		
		// conversion -> bytes
		ByteBuffer buffer = ByteBuffer.wrap(data, 3, 20);
		// time stamp
		buffer.putLong(timestamp);
		// x, y, z
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.putFloat(z);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}

	/*
	 * Build packet for magnetic field sensor.
	 */
	static Paquet makeMagneticField(long timestamp, float x, float y, float z) {
		byte[] data = new byte[23];
		
		/*
		Log.d("gyroscope", timestamp + " : " + x + ", " + y + ", " + z);
		//*/
		
		// size
		data[0] = 0;
		data[1] = (byte) (data.length - 2);
		
		// magnetic field
		data[2] = 5;
		
		// conversion -> bytes
		ByteBuffer buffer = ByteBuffer.wrap(data, 3, 20);
		// time stamp
		buffer.putLong(timestamp);
		// x, y, z
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.putFloat(z);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}

	/*
	 * Build packet for location sensor.
	 */
	static Paquet makeLocation(long timestamp, float lat, float lon, float alt, float speed, float accuracy) {
		byte[] data = new byte[31];
		
		/*
		Log.d("location", timestamp + " : " + lat + ", " + lon + ", " + alt + " ; " + speed + ", " + accuracy);
		//*/
		
		// size
		data[0] = 0;
		data[1] = (byte) (data.length - 2);
		
		// GPS
		data[2] = 2;
		
		// conversion -> bytes
		ByteBuffer buffer = ByteBuffer.wrap(data, 3, 28);
		// time stamp
		buffer.putLong(timestamp);
		// position
		buffer.putFloat(lat);
		buffer.putFloat(lon);
		buffer.putFloat(alt);

		buffer.putFloat(speed);
		buffer.putFloat(accuracy);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}
	
}
