package psc.smartdrone.asservissement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Vector;

import psc.smartdrone.ioio.Channel;
import psc.smartdrone.ioio.IOIOController;


/** Simple example of native library declaration and usage. */
public class SimInterface {
	private Vector<String[]> path;
	private IOIOController mPlane;
	
	public SimInterface(IOIOController c) {
		resetAxis();
		mPlane = c;
	}

	public void setAxis(Channel axis, double value) {
		if(mPlane != null) {
			mPlane.set_command(axis, value);
		}
	}

	public void resetAxis() {
		for(Channel c : Channel.values())
			setAxis(c, 0);
	}

	public double getCoord(int axis) {
		//TODO: Fill. Must take coords from the output of the filter
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

	public boolean loadPath(String fname) {
		//TODO: Adapt path
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