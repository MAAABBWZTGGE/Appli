
package psc.smartdrone.filtre;
import psc.smartdrone.sensor.Sensors;

// *output of the filter
// * 
// * @author wei
// *

public class Output {
	double rc = 0.005;
	public Sensors s;
	
	void sensorToInput(int i, double[] input) {
		switch (i) {
		case 0:
			for (int j = 0; j < s.a.size(); j++)
				input[j] = (double) s.a.elementAt(j).x;
			break;
		case 1:
			for (int j = 0; j < s.a.size(); j++)
				input[j] = (double) s.a.elementAt(j).y;
			break;
		case 2:
			for (int j = 0; j < s.a.size(); j++)
				input[j] = (double) s.a.elementAt(j).z;
			break;
		case 3:
			for (int j = 0; j < s.g.size(); j++)
				input[j] = (double) s.g.elementAt(j).x;
			break;
		case 4:
			for (int j = 0; j < s.g.size(); j++)
				input[j] = (double) s.g.elementAt(j).y;
			break;
		case 5:
			for (int j = 0; j < s.g.size(); j++)
				input[j] = (double) s.g.elementAt(j).z;
			break;
		case 6:
			for (int j = 0; j < s.m.size(); j++)
				input[j] = (double) s.m.elementAt(j).x;
			break;
		case 7:
			for (int j = 0; j < s.m.size(); j++)
				input[j] = (double) s.m.elementAt(j).y;
			break;
		case 8:
			for (int j = 0; j < s.m.size(); j++)
				input[j] = (double) s.m.elementAt(j).z;
			break;
		case 9:
			for (int j = 0; j < s.o.size(); j++)
				input[j] = (double) s.o.elementAt(j).azimuth;
			break;
		case 10:
			for (int j = 0; j < s.o.size(); j++)
				input[j] = (double) s.o.elementAt(j).pitch;
			break;
		case 11:
			for (int j = 0; j < s.o.size(); j++)
				input[j] = (double) s.o.elementAt(j).roll;
			break;
		case 12:
			for (int j = 0; j < s.l.size(); j++)
				input[j] = (double) s.l.elementAt(j).alt;
			break;
		case 13:
			for (int j = 0; j < s.l.size(); j++)
				input[j] = (double) s.l.elementAt(j).lat;
			break;
		case 14:
			for (int j = 0; j < s.l.size(); j++)
				input[j] = (double) s.l.elementAt(j).lon;
			break;
		case 15:
			for (int j = 0; j < s.l.size(); j++)
				input[j] = (double) s.l.elementAt(j).speed;
			break;
		}
	}
	
	void resultToSensors(int i, double[] result) {
		
		switch (i) {
		case 0:
			for (int j = 0; j < s.a.size(); j++)
				s.a.elementAt(j).x = (float) result[j];
			break;
		case 1:
			for (int j = 0; j < s.a.size(); j++)
				s.a.elementAt(j).y = (float) result[j];
			break;
		case 2:
			for (int j = 0; j < s.a.size(); j++)
				s.a.elementAt(j).z = (float) result[j];
			break;
		case 3:
			for (int j = 0; j < s.g.size(); j++)
				s.g.elementAt(j).x = (float) result[j];
			break;
		case 4:
			for (int j = 0; j < s.g.size(); j++)
				s.g.elementAt(j).y = (float) result[j];
			break;
		case 5:
			for (int j = 0; j < s.g.size(); j++)
				s.g.elementAt(j).z = (float) result[j];
			break;
		case 6:
			for (int j = 0; j < s.m.size(); j++)
				s.m.elementAt(j).x = (float) result[j];
			break;
		case 7:
			for (int j = 0; j < s.m.size(); j++)
				s.m.elementAt(j).y = (float) result[j];
			break;
		case 8:
			for (int j = 0; j < s.m.size(); j++)
				s.m.elementAt(j).z = (float) result[j];
			break;
		case 9:
			for (int j = 0; j < s.o.size(); j++)
				s.o.elementAt(j).azimuth = (float) result[j];
			break;
		case 10:
			for (int j = 0; j < s.o.size(); j++)
				s.o.elementAt(j).pitch = (float) result[j];
			break;
		case 11:
			for (int j = 0; j < s.o.size(); j++)
				s.o.elementAt(j).roll = (float) result[j];
			break;
		case 12:
			for (int j = 0; j < s.l.size(); j++)
				s.l.elementAt(j).alt = (float) result[j];
			break;
		case 13:
			for (int j = 0; j < s.l.size(); j++)
				s.l.elementAt(j).lat = (float) result[j];
			break;
		case 14:
			for (int j = 0; j < s.l.size(); j++)
				s.l.elementAt(j).lon = (float) result[j];
			break;
		case 15:
			for (int j = 0; j < s.l.size(); j++)
				s.l.elementAt(j).speed = (float) result[j];
			break;
		}
	}
	
	public Sensors getSensors () {
		for (int i = 0 ; i < 16 ; i++) {
			Filtre filtreRC = new Filtre();
			sensorToInput(i, filtreRC.input);
			filtreRC.filtrer(filtreRC.input, rc, filtreRC.result);
			resultToSensors(i, filtreRC.result);
		}
		return s;
	}
}