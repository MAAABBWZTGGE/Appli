package psc.smartdrone.sensor;

import java.nio.ByteBuffer;


/**
 * Packet of sensor data to be sent on a TCP connection.
 * @author guillaume
 *
 */
public class Paquet {

	public byte[] mData;
	
	/*public static Paquet makeDataOfLog(DataOfLog dataOfLog) {
		byte[] data = new byte[79];
		
		// size
		data[0] = 0;
		data[1] = (byte) (data.length - 2);
		
		// dataoflog
		data[2] = 10;

		// conversion -> bytes
		ByteBuffer buffer = ByteBuffer.wrap(data, 3, data.length - 3);
		// time stamp (8 bytes)
		buffer.putLong(dataOfLog.time);
		// gyro (12 bytes)
		buffer.putFloat(dataOfLog.g.x);
		buffer.putFloat(dataOfLog.g.y);
		buffer.putFloat(dataOfLog.g.z);
		// accel (12 bytes)
		buffer.putFloat(dataOfLog.a.x);
		buffer.putFloat(dataOfLog.a.y);
		buffer.putFloat(dataOfLog.a.z);
		// magn (12 bytes)
		buffer.putFloat(dataOfLog.m.x);
		buffer.putFloat(dataOfLog.m.y);
		buffer.putFloat(dataOfLog.m.z);
		// orient (12 bytes)
		buffer.putFloat(dataOfLog.o.azimuth);
		buffer.putFloat(dataOfLog.o.pitch);
		buffer.putFloat(dataOfLog.o.roll);
		// local (20 bytes)
		buffer.putFloat(dataOfLog.l.lat);
		buffer.putFloat(dataOfLog.l.lon);
		buffer.putFloat(dataOfLog.l.alt);
		buffer.putFloat(dataOfLog.l.speed);
		buffer.putFloat(dataOfLog.l.accuracy);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}*/
	
	/*
	 * Build packet for acceleration sensor.
	 */
	public static Paquet makeAcceleration(long timestamp, Accel a) {
		byte[] data = new byte[23];
		
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
		buffer.putFloat(a.x);
		buffer.putFloat(a.y);
		buffer.putFloat(a.z);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}

	/*
	 * Build packet for gyro sensor.
	 */
	public static Paquet makeGyroscope(long timestamp, Gyro g) {
		byte[] data = new byte[23];
		
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
		buffer.putFloat(g.x);
		buffer.putFloat(g.y);
		buffer.putFloat(g.z);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}

	/*
	 * Build packet for orientation sensor.
	 */
	public static Paquet makeOrientation(long timestamp, Orient o) {
		byte[] data = new byte[23];
		
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
		buffer.putFloat(o.azimuth);
		buffer.putFloat(o.pitch);
		buffer.putFloat(o.roll);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}

	/*
	 * Build packet for magnetic field sensor.
	 */
	public static Paquet makeMagneticField(long timestamp, Magn m) {
		byte[] data = new byte[23];
		
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
		buffer.putFloat(m.x);
		buffer.putFloat(m.y);
		buffer.putFloat(m.z);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}

	/*
	 * Build packet for location sensor.
	 */
	public static Paquet makeLocation(long timestamp, GPSLocation l) {
		byte[] data = new byte[31];
		
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
		buffer.putFloat(l.lat);
		buffer.putFloat(l.lon);
		buffer.putFloat(l.alt);

		buffer.putFloat(l.speed);
		buffer.putFloat(l.accuracy);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}
	
	public static Paquet makeCommand(float timeUp) {
		byte[] data = new byte[2+1+4];
		data[0] = 0;
		data[1] = (byte) (data.length - 2);
		data[2] = 6;
		ByteBuffer buffer = ByteBuffer.wrap(data, 3, 4);
		buffer.putFloat(timeUp);
		Paquet ret = new Paquet();
		ret.mData = data;
		return ret;
		
	}
	
}
