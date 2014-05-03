package psc.smartdrone.ioio;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import psc.smartdrone.android.DataSender;
import psc.smartdrone.sensor.Paquet;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalInput.Spec;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.ClockRate;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;


public class IOIOController extends BaseIOIOLooper {
	//TODO: assign correct pin to each channel
	//TODO: check gaz command

	public final static String LOG_ID = "IOIOController";
	
	//PWM chosen frequency
	public final static int freqHz = 100;

	
	//private DataSender mSender;
	//File dir;
	//FileWriter fw;
	//String logPath = "/storage/sdcard0/Documents/Logs/log.txt";
	
	
	//Switching
	private boolean controlling = false; //true if the IOIO has control, false if the receptor controls
	private PulseInput input_control;
	
	//Outputs	
	private PwmOutput gaz_;
	private PwmOutput lacet_;
	private PwmOutput roulis_;
	private PwmOutput tangage_;
	private double gaz_command = 0.0;
	private double lacet_command = 0.0;
	private double roulis_command = 0.0;
	private double tangage_command = 0.0;
	private DigitalOutput led_;

	
	//Inputs
	private PulseInput radio_gaz;
	private PulseInput radio_lacet;
	private PulseInput radio_roulis;
	private PulseInput radio_tangage;
	//private AnalogInput battery_voltage;
	private float radio_gaz_value = 0.f;
	private float radio_lacet_value = 0.f ;
	private float radio_roulis_value = 0.f;
	private float radio_tangage_value = 0.f;
	private float battery_voltage_value = 0.f;

	


	/** Helper functions */
	
	/*public void setDataSender(DataSender d) {
		mSender = d;
	}*/
	
	//Getters & setters for cammands
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
	
	//Conversion functions
	protected float true_voltage(float measured_voltage) {
		return (float) (5. * measured_voltage);
	}
	
	protected float command(float high_duration, boolean is_gaz) {
		/*float duty_cycle = high_duration * freqHz;
		if(is_gaz) {
			return (float) ((duty_cycle - 0.1) * 10);
		} else {
			return (float) ((duty_cycle - 0.15) * 20);
		}*/
		if(is_gaz) return (high_duration - 0.001f) * 1000.f;
		return (high_duration - 0.0015f) * 2000.f;
	}
	
	protected float duty_cycle(double c, boolean is_gaz) {
		//if (c > 1.0)
		//	c = 1.0;
		
		if (is_gaz && c < 0.0)
			c = 0.0;
		else if(c < -1.0)
			c = -1.0;

		if (is_gaz)
			return (float) ((c + 1.0) / 10.0);

		return (float) ((c + 3.0) / 20.0);
	}

	@Override
	protected void setup() throws ConnectionLostException, InterruptedException {
		/*dir = new File(logPath);
		try {
			fw = new FileWriter(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		//Radio switch input borne 10   10 high -> IOIO control
		//PWM IN 1, 4, 7, 12
		//PWM outs 3, 6, 11, 14
		led_ = ioio_.openDigitalOutput(IOIO.LED_PIN);
		gaz_ = ioio_.openPwmOutput(new DigitalOutput.Spec(3, DigitalOutput.Spec.Mode.OPEN_DRAIN), freqHz);
		lacet_ = ioio_.openPwmOutput(new DigitalOutput.Spec(14, DigitalOutput.Spec.Mode.OPEN_DRAIN), freqHz);
		roulis_ = ioio_.openPwmOutput(new DigitalOutput.Spec(6, DigitalOutput.Spec.Mode.OPEN_DRAIN), freqHz);
		tangage_ = ioio_.openPwmOutput(new DigitalOutput.Spec(11, DigitalOutput.Spec.Mode.OPEN_DRAIN), freqHz);
		
		input_control = ioio_.openPulseInput(new Spec(10), ClockRate.RATE_2MHz, PulseMode.POSITIVE, true);
		radio_gaz = ioio_.openPulseInput(new Spec(1), ClockRate.RATE_2MHz, PulseMode.POSITIVE, true);
		radio_lacet = ioio_.openPulseInput(new Spec(12), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
		radio_roulis = ioio_.openPulseInput(new Spec(4), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
		radio_tangage = ioio_.openPulseInput(new Spec(7), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
		//battery_voltage = ioio_.openAnalogInput(9);
	}

	@Override
	public void loop() throws ConnectionLostException, InterruptedException {		
		//Led: flashing when app started, fix when controlling
		
		controlling = input_control.getDuration() > 0.0015;
		if(controlling) {
			led_.write(false); //Light up
		} else {
			if((System.currentTimeMillis() / 200) % 2 == 0) {
				led_.write(true);
			} else {
				led_.write(false);
			}
		}
		
		//Inputs
		radio_gaz_value = command(radio_gaz.getDuration(), true);
		radio_lacet_value = command(radio_lacet.getDuration(), false);
		radio_roulis_value = command(radio_roulis.getDuration(), false);
		radio_tangage_value = command(radio_tangage.getDuration(), false);
		
		if(controlling) {
			gaz_.setDutyCycle(duty_cycle(radio_gaz_value /*gaz_command*/, true));
			lacet_.setDutyCycle(duty_cycle(radio_lacet_value/*lacet_command*/, false));
			roulis_.setDutyCycle(duty_cycle(roulis_command, false));
			tangage_.setDutyCycle(duty_cycle(radio_tangage_value/*tangage_command*/, false));
		} else {
			gaz_.setDutyCycle(duty_cycle(radio_gaz_value, true));
			lacet_.setDutyCycle(duty_cycle(radio_lacet_value, false));
			roulis_.setDutyCycle(duty_cycle(radio_roulis_value, false));
			tangage_.setDutyCycle(duty_cycle(radio_tangage_value, false));
		}
		try{
			Thread.sleep(20);
		} catch (InterruptedException e) {
		}
		
	}
	
}