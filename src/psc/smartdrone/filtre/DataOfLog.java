
public class DataOfLog {
	int time;
	int nb = 0;
	Gyro g = new Gyro();
	Acce a = new Acce();
	Magn m = new Magn();
	Orient o = new Orient();
	Local l = new Local();
	float p = 0;
	
	void print() {
		System.out.print(time+":: ");
		g.print();
		a.print();
		m.print();
		o.print();
		l.print();
		System.out.println();
	}
}
