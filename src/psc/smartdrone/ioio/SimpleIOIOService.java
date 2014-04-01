package psc.smartdrone.ioio;

import psc.smartdrone.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalInput.Spec;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIOFactory;
import ioio.lib.api.IOIO;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.ClockRate;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;


public class SimpleIOIOService extends IOIOService {
	//TODO: assign correct pin to each channel
	//TODO: check gaz command

	
	public final static String LOG_ID = "IOIOService";
	
	protected static SimpleIOIOService m_Instance = null;
	public static SimpleIOIOService getInstance() {
		return m_Instance;
	}
	//PWM chosen frequency
	public final static int freqHz = 100;
	
	private IOIO ioio_;
	private boolean abort_ = false;
	private Handler uiHandler;
	
	//Switching
	private boolean controlling = false; //true if the IOIO has control, false if the receptor controls
	private PulseInput input_control;
	private DigitalOutput control_switcher_1;
	private DigitalOutput control_switcher_2;
	private DigitalOutput control_switcher_3;
	private DigitalOutput control_switcher_4;
	
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
	private AnalogInput battery_voltage;
	private float radio_gaz_value = 0.f;
	private float radio_lacet_value = 0.f ;
	private float radio_roulis_value = 0.f;
	private float radio_tangage_value = 0.f;
	private float battery_voltage_value = 0.f;

	/** Helper functions */
	
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
		return (high_duration - 0.0015f) * 2000.f;
	}
	
	protected float duty_cycle(double c, boolean is_gaz) {
		if (c > 1.0)
			c = 1.0;
		
		if (is_gaz && c < 0.0)
			c = 0.0;
		else if(c < -1.0)
			c = -1.0;

		if (is_gaz)
			return (float) ((c + 1.0) / 10.0);

		return (float) ((c + 3.0) / 20.0);
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new BaseIOIOLooper() {

			@Override
			protected void setup() throws ConnectionLostException,
					InterruptedException {
				//Radio switch input borne 1   1 high -> 2, 3, 4 low = IOIO control
				//Switchs out digital 2, 3, 4
				//PWM IN 6, 7, 10, 11
				//PWM outs 13, 14, 18
				led_ = ioio_.openDigitalOutput(IOIO.LED_PIN);
				//gaz_ = ioio_.openPwmOutput(1, freqHz);
				lacet_ = ioio_.openPwmOutput(2, freqHz);
				roulis_ = ioio_.openPwmOutput(3, freqHz);
				tangage_ = ioio_.openPwmOutput(4, freqHz);
				
				input_control = ioio_.openPulseInput(new Spec(1), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
				control_switcher_1 = ioio_.openDigitalOutput(2);
				control_switcher_2 = ioio_.openDigitalOutput(3);
				control_switcher_3 = ioio_.openDigitalOutput(4);
				//control_switcher_4 = ioio_.openDigitalOutput(5);
				
				radio_gaz = ioio_.openPulseInput(new Spec(6), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
				radio_lacet = ioio_.openPulseInput(new Spec(7), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
				radio_roulis = ioio_.openPulseInput(new Spec(10), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
				radio_tangage = ioio_.openPulseInput(new Spec(11), ClockRate.RATE_2MHz, PulseMode.POSITIVE, false);
				//battery_voltage = ioio_.openAnalogInput(9);
			}

			@Override
			public void loop() throws ConnectionLostException,
					InterruptedException {
				
				//Led: flashing when app started, fix when controlling
				if(controlling) {
					led_.write(true);
				} else {
					if(((System.currentTimeMillis() / 500) % 2) == 0) {
						led_.write(true);
					} else {
						led_.write(false);
					}
				}
				
				//Switch
				boolean switch_read = input_control.getDuration() > 0.0015;
				if(controlling != switch_read) {
					if(switch_read){
						Log.i(LOG_ID, "Taking control");
					} else {
						Log.i(LOG_ID, "Releasing control");
					}
				}
				controlling = switch_read;
				control_switcher_1.write(!controlling);
				control_switcher_2.write(!controlling);
				control_switcher_3.write(!controlling);
				//control_switcher_4.write(!controlling);
				if(controlling) {
					//Output
					//gaz_.setDutyCycle(duty_cycle(gaz_command, true));
					lacet_.setDutyCycle(duty_cycle(lacet_command, false));
					roulis_.setDutyCycle(duty_cycle(roulis_command, false));
					tangage_.setDutyCycle(duty_cycle(tangage_command, false));
				} else {
					//gaz_.setDutyCycle(duty_cycle(0, true));
					lacet_.setDutyCycle(duty_cycle(0, false));
					roulis_.setDutyCycle(duty_cycle(0, false));
					tangage_.setDutyCycle(duty_cycle(0, false));
				}
				
				//Input
				try{
					//battery_voltage_value = true_voltage(battery_voltage.getVoltage());
					radio_gaz_value = command(radio_gaz.getDuration(), true);
					radio_lacet_value = command(radio_lacet.getDuration(), false);
					radio_tangage_value = command(radio_tangage.getDuration(), false);
					radio_roulis_value = command(radio_roulis.getDuration(), false);
				} catch (InterruptedException e) {
				}
				Thread.sleep(50);
			}
		};
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		m_Instance = this;
		Log.d(LOG_ID, "onStart()");
		super.onStart(intent, startId);
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		if (intent != null && intent.getAction() != null
				&& intent.getAction().equals("stop")) {
			// User clicked the notification. Need to stop the service.
			nm.cancel(0);
			stopSelf();
		} else {
			// Service starting. Create a notification.
			Notification notification = new Notification(
					R.drawable.ic_launcher, "IOIO service running",
					System.currentTimeMillis());
			notification
					.setLatestEventInfo(this, "IOIO Service", "Click to stop",
							PendingIntent.getService(this, 0, new Intent(
									"stop", null, this, this.getClass()), 0));
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			nm.notify(0, notification);
		}
		return START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}