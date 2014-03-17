package psc.smartdrone.filtre;


public class Local {
	float lat,lon,atti,v,err;
	
	void print(){
		System.out.print("l: "+lat+" "+lon+" "+atti+" "+v+" "+err+",");
	}
}
