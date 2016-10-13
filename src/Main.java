import urbi.UClient;
import urbi.urbijava;

public class Main {
	
	private static UClient client;
	// IP address is used to send commands to reeti
	private static final String IP = "127.0.0.1";
	
	public static void main(String[] args) {
		
		// UDP settings (String lhost, int localPort, String rhost, int remotePort)
		ReceiveCommand recmd = new ReceiveCommand("134.96.240.30", 1255,
				"134.96.240.31", 1423);
		recmd.start();
		
		try {
			// Load the c++ native library.
			System.loadLibrary("urbijava");
			System.out.println("---------- URBI LIB LOADED ----------");
		} catch (java.lang.UnsatisfiedLinkError e) {
			System.out.println("ERROR LOAD URBI JAVA");
		}
		
		System.out.println("connecting ...");
		client = urbijava.connect(IP);

		System.out.println("Waiting for incoming messages...");
		
		while (true) {
			String message = recmd.recvString();
			// send the received message to reeti
			client.send(message);
		}
	}
}
