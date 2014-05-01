package psc.smartdrone.filtre;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import psc.smartdrone.android.DataSender;
import psc.smartdrone.sensor.*;

/**test thread of the filter
 * 
 * @author wei
 *
 */

/*public class ReadLog {
	public static void main (String[] args) {
		String ipAdress = "127.0.0.1";
		DataSender dataSender = new DataSender(ipAdress, 6157);
		dataSender.start();
		
		DataOfLog data = new DataOfLog();
		Vector<DataOfLog> vect= new Vector<DataOfLog>();
		String s = "";
		try {
			FileInputStream log = new FileInputStream("log1325377971224~-646968989.txt");
			DataInputStream dr = new DataInputStream(log);
			try {
				int i = dr.read();
				while ( i != -1){
					s = (char) i+ dr.readLine();
					if (data.nb==0){
						StringToData.stringToData(data, s);
					}
					else if (StringToData.getTime(s)-data.time < 100){
						StringToData.stringToData(data, s);
					}
					else {
						StringToData.stringToData(data, s);
						vect.add(data);
						dataSender.sendPaquet(Paquet.makeDataOfLog(data));
						data = new DataOfLog();
					}
					i = dr.read();
				}
			dr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		double[] signal = new double[vect.size()];
		for (int i =0; i<vect.size(); i++) {
			signal[i] = (double) vect.elementAt(i).a.x;
		}
		Filtre filtreRC = new Filtre();
		filtreRC.N= vect.size();
		filtreRC.input = new double[filtreRC.N];
		filtreRC.input = signal;
		filtreRC.result = new double[filtreRC.N];
		filtreRC.filtrer(filtreRC.input, 0.006, filtreRC.result);
		for (int i = 0; i< filtreRC.N; i++){
			System.out.println(filtreRC.result[i]+" "+filtreRC.input[i]);
		}
		dataSender.close();
	}
}
*/