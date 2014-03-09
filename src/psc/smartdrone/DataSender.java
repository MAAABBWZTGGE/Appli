package psc.smartdrone;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * Thread which send packets to the chosen IP/port.
 * @author guillaume
 *
 */
public class DataSender extends Thread {
	
	String mDstIP;
	int mDstPort;
	private Socket mSocket;
	private LinkedList<Paquet> mPaquets;
	
	/*
	 * Initializes an IP/port.
	 */
	public DataSender(String dstIP, int dstPort) {
		mDstIP = dstIP;
		mDstPort = dstPort;
		mPaquets = new LinkedList<Paquet>();
	}
	
	/*
	 * Add a packet to the list of packet to be sent.
	 */
	public synchronized void sendPaquet(Paquet paquet) {
		mPaquets.addLast(paquet);
	}

	/*
	 * Run the thread.
	 * Send packets each time the list is not empty.
	 * @see java.lang.Thread#run()
	 */
    public void run() {
    	//*
		try {
			mSocket = new Socket(mDstIP, mDstPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//*/
		
    	while (mSocket != null) {
    		try {
				//waitForPaquet();
				sendPaquet();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    /*
     * Close the socket.
     */
    public synchronized void close() {
		if (mSocket != null) {
	    	try {
				mSocket.close();
				mSocket = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    /*
     * Send the first packet of the list over the socket.
     */
    private synchronized void sendPaquet() throws IOException {
		if (!mPaquets.isEmpty()) {
	    	// write paquet
			if (mSocket != null) {
				OutputStream out = mSocket.getOutputStream();
		    	out.write(mPaquets.removeFirst().mData);
		    	out.flush();
			}
		}
    }

}
