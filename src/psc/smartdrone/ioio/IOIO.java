package psc.smartdrone.ioio;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalInput.Spec;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.ClockRate;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;

public class IOIO {
	enum Channel {GAZ, LACET, TANGAGE, ROULIS};
	
	class Looper extends BaseIOIOLooper {
		//TODO: check servo PWM frequency & duty cycle <-> command relation
		//TODO: assign correct pin to each channel
		
		public final static int freqHz = 100;
		
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
		
		public float get_radio_command(Channel c) {
			switch(c) {
			case GAZ:
				return radio_gaz_value;
			case LACET:
				return radio_lacet_value;
			case TANGAGE:
				return radio_tangage_value;
			case ROULIS:
				return radio_roulis_value;
			}
			return (float) 0.;
		}
		
		public float get_battery_voltage() {
			return battery_voltage_value;
		}
		
		protected float true_voltage(float measured_voltage) {
			return (float) (5. * measured_voltage);
		}
		
		protected float command(float high_duration, boolean is_gaz) {
			float duty_cycle = high_duration * freqHz;
			if(is_gaz) {
				return (float) ((duty_cycle - 0.1) * 10);
			} else {
				return (float) ((duty_cycle - 0.15) * 20);
			}
		}
		
		protected float duty_cycle(double c, boolean is_gaz) {
			if(c > 1.0){
				c = 1.0;
			}
			if(is_gaz && c < 0.0){
				c = 0.0;
			} else if(c < -1.0) {
				c= -1.0;
			}
			if(is_gaz) {
				return (float) ((c + 1.0) / 10.0);
			}
			return (float) ((c + 3.0) / 20.0);
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
			radio_gaz = ioio_.openPulseInput(new Spec(5), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
			radio_lacet = ioio_.openPulseInput(new Spec(6), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
			radio_roulis = ioio_.openPulseInput(new Spec(7), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
			radio_tangage = ioio_.openPulseInput(new Spec(8), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
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
			//Output
			led_.write(state = !state);
			gaz_.setDutyCycle(duty_cycle(gaz_command, true));
			lacet_.setDutyCycle(duty_cycle(lacet_command, false));
			roulis_.setDutyCycle(duty_cycle(roulis_command, false));
			tangage_.setDutyCycle(duty_cycle(tangage_command, false));
			
			//Input
			
			try{
				battery_voltage_value = true_voltage(battery_voltage.getVoltage());
				radio_gaz_value = command(radio_gaz.getDuration(), true);
				radio_lacet_value = command(radio_lacet.getDuration(), false);
				radio_tangage_value = command(radio_tangage.getDuration(), false);
				radio_roulis_value = command(radio_roulis.getDuration(), false);
				Thread.sleep(50);
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
