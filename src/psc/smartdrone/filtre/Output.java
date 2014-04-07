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
	
	void sensorToInput(int i,double[] input, Sensors s){
		switch (i){
		case 0: for (int j = 0; j < s.a.length; j++) {
			input[j] = (double) s.a[j].x; 
		}
		case 1: for (int j = 0; j < s.a.length; j++) {
			input[j] = (double) s.a[j].y; 
		}
		case 2: for (int j = 0; j < s.a.length; j++) {
			input[j] = (double) s.a[j].z; 
		}
		case 3: for (int j = 0; j < s.g.length; j++) {
			input[j] = (double) s.g[j].x; 
		}
		case 4: for (int j = 0; j < s.g.length; j++) {
			input[j] = (double) s.g[j].y; 
		}
		case 5: for (int j = 0; j < s.g.length; j++) {
			input[j] = (double) s.g[j].z; 
		}
		case 6: for (int j = 0; j < s.m.length; j++) {
			input[j] = (double) s.m[j].x; 
		}
		case 7: for (int j = 0; j < s.m.length; j++) {
			input[j] = (double) s.m[j].y; 
		}
		case 8: for (int j = 0; j < s.m.length; j++) {
			input[j] = (double) s.m[j].z; 
		}
		case 9: for (int j = 0; j < s.o.length; j++) {
			input[j] = (double) s.o[j].azimuth; 
		}
		case 10: for (int j = 0; j < s.o.length; j++) {
			input[j] = (double) s.o[j].pitch; 
		}
		case 11: for (int j = 0; j < s.o.length; j++) {
			input[j] = (double) s.o[j].roll; 
		}
		case 12: for (int j = 0; j < s.l.length; j++) {
			input[j] = (double) s.l[j].alt; 
		}
		case 13: for (int j = 0; j < s.l.length; j++) {
			input[j] = (double) s.l[j].lat; 
		}
		case 14: for (int j = 0; j < s.l.length; j++) {
			input[j] = (double) s.l[j].lon; 
		}
		case 15: for (int j = 0; j < s.l.length; j++) {
			input[j] = (double) s.l[j].speed; 
		}
		}
	}
	
	void resultToSensors(int i, double[] result){
		int sum = 0;
		switch (i){
		case 0: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.a[0].x= (float) sum;
		case 1:	for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.a[0].y= (float) sum;
		case 2: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.a[0].z= (float) sum;
		case 3: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.g[0].x= (float) sum;
		case 4: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.g[0].y= (float) sum;
		case 5: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.g[0].z= (float) sum;
		case 6: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}	
				sum/=result.length;
				sortie.m[0].x= (float) sum;
		case 7: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.m[0].y= (float) sum;
		case 8: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.m[0].z= (float) sum;
		case 9: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.o[0].azimuth= (float) sum;
		case 10: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.o[0].pitch= (float) sum;
		case 11: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.o[0].roll= (float) sum;
		case 12: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.l[0].alt= (float) sum;
		case 13: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.l[0].lat= (float) sum;
		case 14: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.l[0].lon= (float) sum;
		case 15: for (int j = 0; j < result.length; j++) {
					sum += result[j]; 
				}
				sum/=result.length;
				sortie.l[0].speed= (float) sum;
		}
	}
	
	void f (Sensors s) {
		for (int i = 0; i<16 ; i++) {
			Filtre filtreRC = new Filtre();
			sensorToInput(i, filtreRC.input, s);
			filtreRC.filtrer(filtreRC.input, rc, filtreRC.result);
			resultToSensors(i,filtreRC.result);
		}
	}
}
