package psc.smartdrone.ioio;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;

public class IOIO {
	enum Channel {GAZ, LACET, TANGAGE, ROULIS};
	
	class Looper extends BaseIOIOLooper {
		/** The on-board LED. */
		//TODO: check servo frequency
		//TODO: assign correct pin to each channel
		
		public final static int freqHz = 10000;
		
		//Outputs
		private DigitalOutput led_;
		private boolean state = false;
		private PwmOutput gaz_;
		private PwmOutput lacet_;
		private PwmOutput roulis_;
		private PwmOutput tangage_;
		private double gaz_command = 0.0;
		private double lacet_command = 0.0;
		private double roulis_command = 0.0;
		private double tangage_command = 0.0;
		
		//Inputs
		private PulseInput radio_gaz;
		private PulseInput radio_lacet;
		private PulseInput radio_roulis;
		private PulseInput radio_tangage;
		private AnalogInput battery_voltage;
		private float radio_gaz_value;
		private float radio_lacet_value;
		private float radio_roulis_value;
		private float radio_tangage_value;
		private float battery_voltage_value;
		
		public void set_command(Channel c, double value) {
			switch(c) {
			case GAZ:
				gaz_command = value;
				break;
			case LACET:
				lacet_command = value;
				break;
			case TANGAGE:
				tangage_command = value;
				break;
			case ROULIS:
				roulis_command = value;
				break;
			}
		}
		
		public float duty_cycle(double c, boolean is_gaz) {
			if(c > 1.0){
				c = 1.0;
			}
			if(is_gaz && c < 0.0){
				c = 0.0;
			} else if(c < -1.0) {
				c= -1.0;
			}
			if(is_gaz) {
				return (float) (c / 10.0);
			}
			return (float) ((c + 1.0) / 20.0);
		}

		/**
		 * Called every time a connection with IOIO has been established.
		 * Typically used to open pins.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
		 */
		@Override
		protected void setup() throws ConnectionLostException {
			led_ = ioio_.openDigitalOutput(0, true);
			gaz_ = ioio_.openPwmOutput(1, freqHz);
			lacet_ = ioio_.openPwmOutput(2, freqHz);
			roulis_ = ioio_.openPwmOutput(3, freqHz);
			tangage_ = ioio_.openPwmOutput(4, freqHz);
			radio_gaz = ioio_.openPulseInput(5, PulseMode.FREQ);
			radio_lacet = ioio_.openPulseInput(6, PulseMode.FREQ);
			radio_roulis = ioio_.openPulseInput(7, PulseMode.FREQ);
			radio_tangage = ioio_.openPulseInput(8, PulseMode.FREQ);
			battery_voltage = ioio_.openAnalogInput(9);
		}

		/**
		 * Called repetitively while the IOIO is connected.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
		 */
		@Override
		public void loop() throws ConnectionLostException {
			led_.write(state = !state);
			gaz_.setDutyCycle(duty_cycle(gaz_command, true));
			lacet_.setDutyCycle(duty_cycle(lacet_command, false));
			roulis_.setDutyCycle(duty_cycle(roulis_command, false));
			tangage_.setDutyCycle(duty_cycle(tangage_command, false));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * A method to create our IOIO thread.
	 * 
	 * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
	 */
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
}
