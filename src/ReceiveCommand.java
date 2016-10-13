import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;

public class ReceiveCommand {
	// local address
	private String mlocalhost;
	private int mLocalPort;
	private SocketAddress mLocalAddr;

	// local address
	private String mRemotehost;
	private int mRemotePort;
	private SocketAddress mRomoteAddr;

	// the datagram socket
	private DatagramSocket mSocket;

	ReceiveCommand(String lhost, int localPort, String rhost, int remotePort) {
		mlocalhost = lhost;
		mLocalPort = localPort;
		mRemotehost = rhost;
		mRemotePort = remotePort;
	}

	public final void start() {
		// create UDP connection
		try {
			mLocalAddr = new InetSocketAddress(mlocalhost, mLocalPort);
			mSocket = new DatagramSocket(mLocalAddr);
			mRomoteAddr = new InetSocketAddress(mRemotehost, mRemotePort);
			mSocket.connect(mRomoteAddr);
			System.out.println("Connect to reeti to control single motor");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Send some string via the socket
	public final boolean sendString(final String string) {
		try {
			// Create the byte buffer
			final byte[] buffer = string.getBytes("UTF-8");
			// Create the UDP packet
			final DatagramPacket packet = new DatagramPacket(buffer,
					buffer.length);
			// And send the UDP packet
			mSocket.send(packet);
			// Print some information
			System.out.println("Sending message: " + string);
			return true;
		} catch (final IOException exc) {
			System.out.println("Sending failed");
			return false;
		}

	}

	// Receive some bytes via the socket
	public final byte[] recvBytes(final int size) {
		try {
			// Construct a byte array
			final byte[] buffer = new byte[size];
			// Construct an UDP packet
			final DatagramPacket packet = new DatagramPacket(buffer,
					buffer.length);
			// Receive the UDP packet
			mSocket.receive(packet);
			// Return the buffer now
			return Arrays.copyOf(buffer, packet.getLength());
		} catch (final IOException e){
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("Received Failed");
			return null;
		}
	}

	// Receive some string via the socket
	public final String recvString() {
		try {
			// Receive a byte buffer
			final byte[] buffer = recvBytes(4096);
			// Check the buffer content
			if (buffer != null) {
				// Construct a message
				final String message = new String(buffer, 0, buffer.length,
						"UTF-8");
				// Print some information
				System.out.println("Message received: " + message);
				// And return message
				return message;
			}
		} catch (final UnsupportedEncodingException exc) {
			// Print some information
			System.out.println("Message not received");
		}
		return null;
	}

}
