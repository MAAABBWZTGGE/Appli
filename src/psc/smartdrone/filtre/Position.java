package psc.smartdrone.filtre;

import psc.smartdrone.asservissement.Vector3;

/*
 * Position de l'avion
 */
public class Position {
	
	public Position() {
	}
	
	public boolean ready() {
		return pos != null && speed != null;
	}
	
	public double x() {
		return pos.x;
	}
	public double y() {
		return pos.y;
	}
	public double z() {
		return pos.z;
	}
	
	public double phi() {
		return phi;
	}
	public double theta() {
		return theta;
	}
	public double psi() {
		return psi;
	}
	
	// Coordonnées en mètres.
	Vector3 pos;
	// Angles de rotation.
	double phi, theta, psi;
	
	// Vitesse.
	Vector3 speed;
	// Timestamp (ms).
	long timestamp;

}
