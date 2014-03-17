
import java.nio.ByteBuffer;


/**
 * Packet of data to be sent on a TCP connection.
 * @author guillaume
 *
 */
public class Paquet {

	public byte[] mData;
	
	static Paquet makeDataOfLog(DataOfLog dataOfLog) {
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
		buffer.putFloat(dataOfLog.o.azi);
		buffer.putFloat(dataOfLog.o.pitch);
		buffer.putFloat(dataOfLog.o.roll);
		// local (20 bytes)
		buffer.putFloat(dataOfLog.l.lat);
		buffer.putFloat(dataOfLog.l.lon);
		buffer.putFloat(dataOfLog.l.atti);
		buffer.putFloat(dataOfLog.l.v);
		buffer.putFloat(dataOfLog.l.err);
		
		// resultat
		Paquet result = new Paquet();
		result.mData = data;
		return result;
	}
	
	/*
	 * Build packet for acceleration sensor.
	 */
	static Paquet makeAcceleration(long timestamp, float x, float y, float z) {
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
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.putFloat(z);
		
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
