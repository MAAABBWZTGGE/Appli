package psc.smartdrone.asservissement;

import psc.smartdrone.ioio.Channel;

public class Program {

	Cross cross;
	SimInterface sim;
	
	double spd;
	double speed;
	double X;
	double Y;
	double Z;
	double pX;
	double pY;
	double pZ;
	double theta;
	double phi;
	double psi;
	long time;
	double dt;
	double rate;
	double[] vars = new double[10]; 
	double kp;
	double kd;
	double ki;

	public Program(SimInterface sim) {
		this.sim = sim;
	}

	public void initialisation() {
		sim.loadPath("loopBis.txt");

		cross = new Cross(sim, 1, 100);

		spd = 1;
		speed = 0;
		time = System.currentTimeMillis();
		sim.updateCoords(time);
		X = sim.getCoord(0);
		Y = sim.getCoord(1);
		Z = sim.getCoord(2);
		pX = X;
		pY = Y;
		pZ = Z;
		theta = sim.getCoord(3);
		phi = sim.getCoord(4);
		psi = sim.getCoord(5);
		dt = 0;
		rate = 50.0 / 1000.0;
		kp = 60;
		kd = 1;
		ki = 50; //182
		
		sim.setAxis(Channel.GAZ, spd); //mettre moteur a 53%
	}

	public void mainLoop() {
		dt = (System.currentTimeMillis() - time) / 1000.0;
		if (dt < rate)
			return;
		time = System.currentTimeMillis();

		cross.update();
		updateCoords(time);
		if (speed == 0)
			return;

		/*
		Vector3 dir = new Vector3(
				Math.cos((psi+90) * Math.PI / 180),
				Math.sin((psi+90) * Math.PI / 180),
				0);
		Vector3 ldir = new Vector3(
				0,
				Math.cos((theta+270) * Math.PI / 180),
				Math.sin((theta+270) * Math.PI / 180));
		Vector3 hdir = new Vector3(
				Math.cos((phi+90) * Math.PI / 180),
				0,
				Math.sin((phi+90) * Math.PI / 180));
		/*/
		Vector3 dir = new Vector3(
				-Math.sin(psi * Math.PI / 180),
				Math.cos(psi * Math.PI / 180),
				0);
		Vector3 ldir = new Vector3(
				0,
				Math.sin(theta * Math.PI / 180),
				-Math.cos(theta * Math.PI / 180));
		Vector3 hdir = new Vector3(
				-Math.sin(phi * Math.PI / 180),
				0,
				Math.cos(phi * Math.PI / 180));
	    //*/
		
		
		//pd(0,dir,new Vector3(-1,0,0),2,kp,kd,ki);
		//pd(0,dir, new Vector3(cross.getCoord(0)-X,cross.getCoord(1)-Y,cross.getCoord(2)-Z),2,0.05,10);
		

		//if (-sim.getCoord(2) <= 8)
		if (-Z <= 8)
			pd(
					0,
					dir,
					new Vector3(
							cross.getCoord(0) - X,
							cross.getCoord(1) - Y,
							cross.getCoord(2) - Z),
					2,
					kp,
					kd,
					ki);
		
		double kRot = 0.1;
		Vector3 proj = new Vector3(
				-cross.getCoord(0) + X,
				-cross.getCoord(1) + Y,
				0);
		
		//double thetaCons = (180 / Math.PI) * Math.atan(2*speed*kRot*dir.scalar(proj) / (9.81*dir.norm()*proj.norm()));
		double thetaCons = (180 / Math.PI) * Math.atan(2*speed*kRot*dir.normScalar(proj) / 9.81);

		pd(
				1,
				ldir,
				new Vector3(
						0,
						Math.cos(-thetaCons * Math.PI / 180),
						Math.sin(-thetaCons * Math.PI / 180)),
				0,
				30,
				0.0005,
				1);
		
		System.out.println(speed);
		pd(
				2,
				hdir,
				new Vector3(
						Math.sqrt(Math.pow(-cross.getCoord(0) + X, 2) + Math.pow(-cross.getCoord(1) + Y, 2)),
						0,
						cross.getCoord(2) + Z),
				1,
				kp,
				kd,
				ki);
	}
	
	public void pd(int id, Vector3 dir, Vector3 cDir, int axis, double kp, double kd, double ki) {
		double diff = (180 / Math.PI) * Math.atan2(dir.normScalar(cDir), dir.normVectorial(cDir));
		
		if (vars[2*id] == 0)
			vars[2*id] = diff;
		
		double vDiff = (diff - vars[2*id]) / dt;
		vars[2*id] = diff;
		double iDiff = vars[2*id+1];
		vars[2*id+1] += diff * dt;
		
		double value = (kp*diff + kd*vDiff + ki*iDiff) / Math.max(1, Math.pow(speed, 1.5));
		if (axis != 3)
			value = Math.min(Math.max(value, -0.5), 0.5);
		else
			value = Math.min(Math.max(value, 0), 1);
		
		switch(axis) {
		case 0:
			sim.setAxis(Channel.ROULIS, value);
			break;
		case 1:
			sim.setAxis(Channel.TANGAGE, value);
			break;
		case 2:
			sim.setAxis(Channel.LACET, value);
			break;
		case 3:
			sim.setAxis(Channel.GAZ, value);
			break;
		}

	}

	public void updateCoords(long timestamp) {
		sim.updateCoords(timestamp);
		X = sim.getCoord(0);
		Y = sim.getCoord(1);
		Z = sim.getCoord(2);
		theta = sim.getCoord(3);
		phi = sim.getCoord(4);
		psi = sim.getCoord(5);
		speed = Math.sqrt(Math.pow(X - pX, 2) + Math.pow(Y - pY, 2) + Math.pow(Z - pZ, 2)) / dt;
		pX = X;
		pY = Y;
		pZ = Z;
	}
	
	public double angleDiff(double angle1, double angle2) {
		double a = angle1;
		double b = angle2;

		double c = a - b;
		double d = a + (360 - b);
		double e = b + (360 - a);

		double f = Math.min(Math.abs(c), Math.min(Math.abs(d), Math.abs(e)));
		if (f == Math.abs(c))
			return -c;
		if (f == Math.abs(d))
			return -d;
		return e;
	}

	/*
	public void keyPressed(KeyEvent e) {
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("O")) {
			sim.setAxis(3, .6);
		}
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("L")) {
			sim.setAxis(3, 0);
		}
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("K")) {
			sim.setAxis(0, .5);
		}
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("M")) {
			sim.setAxis(0, -.5);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("K")) {
			sim.setAxis(2, 0);
		}
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("M")) {
			sim.setAxis(2, 0);
		}
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("O")) {
			sim.setAxis(3, 0);
		}
		
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
			kp+=1;
			System.out.println(kp);
		}
		if (KeyEvent.getKeyText(e.getKeyCode()).equals("Q")) {
			kp-=1;
			System.out.println(kp);
		}
	}*/
}
