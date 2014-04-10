package psc.smartdrone.filtre;
import psc.smartdrone.sensor.Sensors;

/**output of the filter, rc the need to be determinated...
 * 
 * @author wei
 *
 */

public class Output {
	double rc = 0.005;
	Sensors sortie = new Sensors(1);
	
	void sensorToInput(int i, double[] input, Sensors s) {
		switch (i) {
		case 0:
			for (int j = 0; j < s.a.length; j++)
				input[j] = (double) s.a[j].x;
			break;
		case 1:
			for (int j = 0; j < s.a.length; j++)
				input[j] = (double) s.a[j].y;
			break;
		case 2:
			for (int j = 0; j < s.a.length; j++)
				input[j] = (double) s.a[j].z;
			break;
		case 3:
			for (int j = 0; j < s.g.length; j++)
				input[j] = (double) s.g[j].x;
			break;
		case 4:
			for (int j = 0; j < s.g.length; j++)
				input[j] = (double) s.g[j].y;
			break;
		case 5:
			for (int j = 0; j < s.g.length; j++)
				input[j] = (double) s.g[j].z;
			break;
		case 6:
			for (int j = 0; j < s.m.length; j++)
				input[j] = (double) s.m[j].x;
			break;
		case 7:
			for (int j = 0; j < s.m.length; j++)
				input[j] = (double) s.m[j].y;
			break;
		case 8:
			for (int j = 0; j < s.m.length; j++)
				input[j] = (double) s.m[j].z;
			break;
		case 9:
			for (int j = 0; j < s.o.length; j++)
				input[j] = (double) s.o[j].azimuth;
			break;
		case 10:
			for (int j = 0; j < s.o.length; j++)
				input[j] = (double) s.o[j].pitch;
			break;
		case 11:
			for (int j = 0; j < s.o.length; j++)
				input[j] = (double) s.o[j].roll;
			break;
		case 12:
			for (int j = 0; j < s.l.length; j++)
				input[j] = (double) s.l[j].alt;
			break;
		case 13:
			for (int j = 0; j < s.l.length; j++)
				input[j] = (double) s.l[j].lat;
			break;
		case 14:
			for (int j = 0; j < s.l.length; j++)
				input[j] = (double) s.l[j].lon;
			break;
		case 15:
			for (int j = 0; j < s.l.length; j++)
				input[j] = (double) s.l[j].speed;
			break;
		}
	}
	
	void resultToSensors(int i, double[] result) {
		int sum = 0;
		for (int j = 0 ; j < result.length ; j++)
			sum += result[j];
		sum /= result.length;
		
		switch (i) {
		case 0:
			sortie.a[0].x = (float) sum;
			break;
		case 1:
			sortie.a[0].y = (float) sum;
			break;
		case 2:
			sortie.a[0].z = (float) sum;
			break;
		case 3:
			sortie.g[0].x = (float) sum;
			break;
		case 4:
			sortie.g[0].y = (float) sum;
			break;
		case 5:
			sortie.g[0].z = (float) sum;
			break;
		case 6:
			sortie.m[0].x = (float) sum;
			break;
		case 7:
			sortie.m[0].y = (float) sum;
			break;
		case 8:
			sortie.m[0].z = (float) sum;
			break;
		case 9:
			sortie.o[0].azimuth = (float) sum;
			break;
		case 10:
			sortie.o[0].pitch = (float) sum;
			break;
		case 11:
			sortie.o[0].roll = (float) sum;
			break;
		case 12:
			sortie.l[0].alt = (float) sum;
			break;
		case 13:
			sortie.l[0].lat = (float) sum;
			break;
		case 14:
			sortie.l[0].lon = (float) sum;
			break;
		case 15:
			sortie.l[0].speed = (float) sum;
			break;
		}
	}
	
	void f (Sensors s) {
		for (int i = 0 ; i < 16 ; i++) {
			Filtre filtreRC = new Filtre();
			sensorToInput(i, filtreRC.input, s);
			filtreRC.filtrer(filtreRC.input, rc, filtreRC.result);
			resultToSensors(i, filtreRC.result);
		}
	}
}
