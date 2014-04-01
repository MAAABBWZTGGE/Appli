package psc.smartdrone.asservissement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Vector;

import android.util.Log;

import psc.smartdrone.ioio.Channel;
import psc.smartdrone.ioio.SimpleIOIOService;

//import com.sun.jna.Library;
//import com.sun.jna.Native;

/** Simple example of native library declaration and usage. */
public class SimInterface {
	//static SimDLL sdll;
	//static ProgDLL pdll;
	//static KeyDLL kdll;
	//static WinDLL wdll;
	//public Tcp tcp;
	protected SimpleIOIOService ioio;
	private Vector<String[]> path;

	/*public interface SimDLL extends Library {
		SimDLL INSTANCE = (SimDLL) Native.loadLibrary(
				System.getProperty("user.dir") + "\\dll\\CRRCsimdata.dll",
				SimDLL.class);

		double GetPlaneX();

		double GetPlaneY();

		double GetPlaneZ();

		double GetPlanePhi();

		double GetPlaneTheta();

		double GetPlanePsi();

		double SetAxis(double axis, double value);
	}*/

	/*public interface ProgDLL extends Library {
		ProgDLL INSTANCE = (ProgDLL) Native.loadLibrary(
				System.getProperty("user.dir") + "\\dll\\ExecuteEx.dll",
				ProgDLL.class);

		double execute_ex_set_directory(String dir);

		double execute_ex_set_showmode(double mode);

		double execute_program_ex(String prog, String args, double mode);
	}*/

	/*public interface KeyDLL extends Library {
		KeyDLL INSTANCE = (KeyDLL) Native.loadLibrary(
				System.getProperty("user.dir") + "\\dll\\SendKeys.dll",
				KeyDLL.class);

		double DLLSendKeys(String key, double wait);

		double DLLAppActivate(String caption);
	}*/

	/*public interface WinDLL extends Library {
		WinDLL INSTANCE = (WinDLL) Native.loadLibrary(
				System.getProperty("user.dir") + "\\dll\\WindowControl.dll",
				WinDLL.class);

		double WC_DisplayWindow(double handle, double fnct);

		double WC_FindWindow(String caption);
	}*/

	public SimInterface(SimpleIOIOService _ioio) {
		if (_ioio != null)
			ioio = _ioio;
		else
			Log.e("SimInterface", "IOIO is null");
		
		//sdll = SimDLL.INSTANCE;
		resetAxis();

		//System.out.println("SimDLL LOADED PROPERLY");
		//pdll = ProgDLL.INSTANCE;
		//System.out.println("ProgDLL LOADED PROPERLY");
		//kdll = KeyDLL.INSTANCE;
		//System.out.println("KeyDLL LOADED PROPERLY");
		//wdll = WinDLL.INSTANCE;
		//System.out.println("WinDLL LOADED PROPERLY");
		//tcp = new Tcp();
		//System.out.println("COM INITIALIZED");

	}

	/*public double findHandle(String caption) {
		return wdll.WC_FindWindow(caption);
	}*/

	/*public double showWindows(double handle) {
		return wdll.WC_DisplayWindow(handle, 5);
	}*/

	public void setAxis(Channel axis, double value) {
		ioio.set_command(axis, value);
	}

	public void resetAxis() {
		for(Channel c : Channel.values())
			setAxis(c, 0);
	}

	public double getCoord(int axis) {
		//TODO: Fill
		switch (axis) {
		case 0:
			//return sdll.GetPlaneX();
		case 1:
			//return sdll.GetPlaneY();
		case 2:
			//return sdll.GetPlaneZ();
		case 3:
			//return sdll.GetPlanePhi();
		case 4:
			//return sdll.GetPlaneTheta();
		case 5:
			//return sdll.GetPlanePsi();
		default:
			return 0;
		}
	}

	/*public void setDirectory(String dir) {
		pdll.execute_ex_set_directory(dir);
	}*/

	/*public double executeProgram(String prog) {
		return pdll.execute_program_ex(prog, "", 0);
	}*/

	/*public void setShowMode(int mode) {
		pdll.execute_ex_set_showmode(mode);
	}*/

	/*public double sendKey(String key, double wait) {
		return kdll.DLLSendKeys(key, wait);
	}*/

	/*public boolean activate(String caption) {
		int maxTime = 100;
		double test = kdll.DLLAppActivate(caption);
		while (test < 1 && maxTime > 0) {
			test = kdll.DLLAppActivate(caption);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			maxTime -= 10;
		}
		return !(test < 1);
	}*/

	public boolean loadPath(String fname) {
		//TODO: Adapt
		BufferedReader reader;
		File f = new File(System.getProperty("user.dir") + "\\tools\\paths\\"
				+ fname);
		if (!f.exists() || f.isDirectory()) {
			return false;
		}
		path = new Vector<String[]>();
		try {
			reader = new BufferedReader(
					new FileReader(System.getProperty("user.dir")
							+ "\\tools\\paths\\" + fname));

			String line = null;
			line = reader.readLine().substring(1);
			int n = Integer.valueOf(line);
			for (int i = 0; i < n; i++) {
				for (int k = 0; k < 6; k++) {
					line = reader.readLine().substring(1);
					;
				}
				int nn = Integer.valueOf(line);
				String[] str = new String[nn * 3];
				for (int k = 0; k < nn * 3; k++) {
					line = reader.readLine().substring(1);
					;
					str[k] = line;
				}
				path.add(str);
			}
			reader.close();
			initialiseSimPath(fname);
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error");
			return false;
		}
	}

	public int getPathCoord(int i, int j, int c) {
		return Integer.valueOf(path.elementAt(i)[j * 3 + c]);
	}

	public int getPathLength(int i) {
		return path.elementAt(i).length / 3;
	}

	public void printPath() {
		if (path == null)
			return;
		@SuppressWarnings("unchecked")
		Vector<String[]> clone = (Vector<String[]>) path.clone();
		while (!clone.isEmpty()) {
			String[] str = clone.remove(0);
			for (int k = 0; k < str.length; k++) {
				String txt = "";
				if (k % 3 == 0)
					txt = "x";
				if (k % 3 == 1)
					txt = "y";
				if (k % 3 == 2)
					txt = "z";
				System.out.println(txt + " = " + str[k]);
			}
		}

	}

	void initialiseSimPath(String fname) throws IOException {
		File pathFrom = new File(System.getProperty("user.dir")
				+ "\\tools\\paths\\" + fname);
		File pathTo = new File(System.getProperty("user.dir")
				+ "\\tools\\dll\\path.txt");
		copyFile(pathFrom, pathTo);
		//activate("Render");
		//sendKey("L", 1);

	}

	@SuppressWarnings("resource")
	public static boolean copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();

			long count = 0;
			long size = source.size();
			while ((count += destination.transferFrom(source, count, size
					- count)) < size)
				;
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
}