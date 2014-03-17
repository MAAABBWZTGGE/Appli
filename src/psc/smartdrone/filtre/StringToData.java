
public class StringToData {
	static int getTime (String s){
		int i = 2;
		while (s.charAt(i) != ':'){
			i++;
		}
		int result = Integer.parseInt(s.substring(i-12,i-7));;
		return result;
	}
	static void stringToData (DataOfLog data, String s){
		char type = s.charAt(0);
		String temp = "";
		int time = 0;
		int n = 0;
		float x= 0,y = 0,z =0,p = 0,v = 0,e= 0;
			if (type != 'l' && type != 'p'){
				for (int i = 2; i < s.length();i++) {
					if ((s.charAt(i) != ':')&&(s.charAt(i) !=',')) {
						temp += s.charAt(i);
					}
					else if (s.charAt(i) == ':') {
						time = Integer.parseInt(temp.substring(temp.length()-12,temp.length()-7));
						temp = "";
					}
					else if (s.charAt(i) == ',') {
						if (n == 0){
							x = Float.parseFloat(temp);
							temp = "";
							n++;
						}
						else if (n == 1) {
							y = Float.parseFloat(temp);
							n++;
							temp = "";
						}
					}	
				}
				z = Float.parseFloat(temp);
			}
			else if (type == 'p') {
				for (int i = 2; i < s.length();i++) {
					if (s.charAt(i) != ':') {
						temp += s.charAt(i);
					}
					else {
						time = Integer.parseInt(temp.substring(temp.length()-12,temp.length()-7));
						temp = "";
					}
				}
				p = Float.parseFloat(temp);
			}
			else {
				int m = 0;
				for (int i = 2; i < s.length();i++) {
					if (s.charAt(i) != ':') {
						temp += s.charAt(i);
					}
					else if (s.charAt(i) == ':' && m == 0){
						time = Integer.parseInt(temp.substring(temp.length()-12,temp.length()-7));
						temp = "";
						m++;
					}
					else if (s.charAt(i) == ':' && m == 1){
						z = Float.parseFloat(temp);
						temp = "";
						m++;
					}
					else if (s.charAt(i) == ':' && m == 2){
						v = Float.parseFloat(temp);
						temp = "";
						m++;
					}
					else if (s.charAt(i) == ',') {
						if (n == 0){
							x = Float.parseFloat(temp);
							temp = "";
							n++;
						}
						else if (n == 1) {
							y = Float.parseFloat(temp);
							n++;
							temp = "";
						}
					}
				}
				e = Float.parseFloat(temp);
			}
		if (data.nb == 0) {
			switch (s.charAt(0)) {
			case 'g' : data.nb=1; data.time= time; data.g.x = x; data.g.y = y; data.g.z = z; break;
			case 'a' : data.nb=1; data.time= time; data.a.x = x; data.a.y = y; data.a.z = z; break;
			case 'm' : data.nb=1; data.time= time; data.m.x = x; data.m.y = y; data.m.z = z; break;
			case 'o' : data.nb=1; data.time= time; data.o.azi = x; data.o.pitch = y; data.o.roll = z; break;
			case 'l' : data.nb=1; data.time= time; data.l.lat = x; data.l.lon = y; data.l.atti = z; data.l.v = v; data.l.err = e; break;
			case 'p' : data.nb=1; data.time= time; data.p = p; break;
			}
		}
		else {
			switch (s.charAt(0)) {
			case 'g' : data.g.x = (data.g.x*data.nb +x)/(data.nb +1); data.g.y = (data.g.y*data.nb +y)/(data.nb +1); data.g.z = (data.g.z*data.nb +z)/(data.nb +1); data.nb+=1; break;
			case 'a' : data.a.x = (data.a.x*data.nb +x)/(data.nb +1); data.a.y = (data.a.y*data.nb +y)/(data.nb +1); data.a.z = (data.a.z*data.nb +z)/(data.nb +1); data.nb+=1; break;
			case 'm' : data.m.x = (data.m.x*data.nb +x)/(data.nb +1); data.m.y = (data.m.y*data.nb +y)/(data.nb +1); data.m.z = (data.m.z*data.nb +z)/(data.nb +1); data.nb+=1; break;
			case 'o' : data.o.azi = (data.o.azi*data.nb +x)/(data.nb +1); data.o.pitch = (data.o.pitch*data.nb +y)/(data.nb +1); data.o.roll = (data.o.roll*data.nb +z)/(data.nb +1); data.nb+=1; break;
			case 'l' : data.l.lat = (data.l.lat*data.nb +x)/(data.nb +1); data.l.lon = (data.l.lon*data.nb +y)/(data.nb +1); data.l.atti = (data.l.atti*data.nb +z)/(data.nb +1);
				data.l.v = (data.l.v*data.nb +v)/(data.nb +1); data.l.err = (data.l.err*data.nb +e)/(data.nb +1); data.nb+=1; break;
			case 'p' : data.p = (data.p*data.nb +p)/(data.nb +1); data.nb+=1; break;
			}
		}
	}
}
