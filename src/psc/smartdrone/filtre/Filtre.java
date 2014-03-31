package psc.smartdrone.filtre;
import psc.smartdrone.sensor.Sensors;

/**Thread of the filter by using a discret Fourier transformation and a RC-filter.
 * 
 * @author wei
 *
 */

public class Filtre {
	int N;
	double[] input,result;
	
	
	public void DFT(double[] input, double[]rDFT, double[] iDFT) {
		int N = input.length;
		
		for (int f = 0; f < N/2; f++) {
			rDFT[f] = 0;
			iDFT[f] = 0;
			
			for (int i = 0; i < N; i++) {
				double w = 2*Math.PI*(double) i/N;
				rDFT[f] += input[i]*Math.cos(f*w);
				iDFT[f] += input[i]*Math.sin(f*w);
			}
			
			rDFT[f] /= N/2;
			iDFT[f] /= N/2;
		}
	}
	
	public void invDFT(double[] output, double[]rDFT, double[] iDFT) {
		int N = output.length;
		for (int i = 0; i < N; i++) {
			output[i] = 0;
			for (int f = 0; f < N/2; f++) {
				double w = 2*Math.PI*(double) i/N;
				output[i] += rDFT[f] * Math.cos(f*w) + iDFT[f]*Math.sin(f*w);
			}
		}
	}
	
	public void initFiltreRC(double[] ReFil, double[] ImFil, double rc, int N) {
		for (int f = 0; f< N/2; f++) {
			double w = 2*Math.PI*f;
			double wrc = w*rc;
			double gain = 1.0/(1+Math.pow(wrc, 2));
			double real = 1;
			double imag = -wrc;
			ReFil[f] = real*gain;
			ImFil[f] = imag*gain;
		}
	}
	
	public void filtrer(double[] input, double rc, double [] output){
		int N = input.length;
		 
		double[] ReFil = new double[N/2];
		double[] ImFil = new double[N/2];
		this.initFiltreRC(ReFil, ImFil, rc, N);
		 
		double[] ReSig = new double[N/2];
		double[] ImSig = new double[N/2];
		DFT(input, ReSig, ImSig);
		 
		double[] ReOutput = new double[N/2];
		double[] ImOutput = new double[N/2];
		
		for(int f=0;f<N/2;f++) {
			ReOutput[f]=ReSig[f]*ReFil[f]-ImSig[f]*ImFil[f];
			ImOutput[f]=ReSig[f]*ImFil[f]+ImSig[f]*ReFil[f];
		}
		
		invDFT(output, ReOutput, ImOutput);
	}
	
}
