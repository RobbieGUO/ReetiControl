package Main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;

import cereprocTts.CereprocHandle;

import urbi.UClient;
import urbi.urbijava;

public class Main {

	public static UClient client;
	// IP address is used to send commands to reeti
	private static final String IP = "127.0.0.1";

	// Lock the client.send() function
	public static final Object taskLock = new Object();

	// Process command line arguments
	private static final String voice_name = "/home/reeti/workspace/Archiv/cereproc/javalib/cerevoice_heather_3.2.0_48k.voice";
	private static final String license_name = "/home/reeti/workspace/Archiv/cereproc/javalib/license.lic";

	static {
		// The cerevoice_eng library must be on the path,
		// specify with eg:
		// java -Djava.library.path=/path/to/library/
		// System.setProperty("java.library.path",
		// "/home/alvaro/Documentos/Tesis/cerevoice_sdk_3.2.0_linux_x86_64_python26_10980_academic/cerevoice_eng");

		try { // Adding Library path
			addDir("/home/reeti/workspace/Archiv/cerevoice_sdk_3.2.0_linux_i686_python26_10980_academic/cerevoice_eng/javalib");
			System.loadLibrary("cerevoice_eng");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addDir(String s) throws IOException {
		try {
			// This enables the java.library.path to be modified at runtime
			// From a Sun engineer at
			// http://forums.sun.com/thread.jspa?threadID=707176
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[]) field.get(null);
			for (int i = 0; i < paths.length; i++) {
				if (s.equals(paths[i])) {
					return;
				}
			}
			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = s;
			field.set(null, tmp);
			System.setProperty("java.library.path",
					System.getProperty("java.library.path")
							+ File.pathSeparator + s);
		} catch (IllegalAccessException e) {
			throw new IOException(
					"Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			throw new IOException(
					"Failed to get field handle to set library path");
		}
	}

	public static void main(String[] args) {

		// UDP settings (int localPort)
		ReceiveCommand recmd = new ReceiveCommand(1256);
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
			DatagramPacket dataPackage = recmd.getDatagramPacket();
			if (message.indexOf("Global.servo") != -1) {
				// send the motor command message to reeti
				synchronized (Main.taskLock) {
					client.send(message);
				}
			} else {
				CereprocHandle voice = new CereprocHandle(voice_name,
						license_name, message, recmd, dataPackage);
				voice.start();
			}
		}
	}
}
