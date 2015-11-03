package su.levenetc.android.interactivecanvas;

import android.view.MotionEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Eugene Levenetc.
 */
class TouchEventsSendThread extends Thread {

	private volatile boolean connected = true;
	private DatagramSocket socket;
	private byte[] buffer = new byte[32 * Config.TOUCH_METADATA_SIZE];
	public LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
	private InetAddress hostAddress;
	private int port;
	private int clientId;

	public TouchEventsSendThread(InetAddress hostAddress, int port, int clientId) {
		this.hostAddress = hostAddress;
		this.port = port;
		this.clientId = clientId;
	}

	public void add(MotionEvent event) {
		byte[] array = new byte[32 * Config.TOUCH_METADATA_SIZE];
		Utils.putIntTo(array, clientId, 0);
		Utils.putIntTo(array, event.getAction(), 32);
		Utils.putIntTo(array, (int) event.getX(), 64);
		Utils.putIntTo(array, (int) event.getY(), 96);
		try {
			queue.put(array);
		} catch (InterruptedException e) {
			InternalLogger.logException(getClass(), e);
		}
	}

	@Override public void run() {

		try {
			socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			packet.setAddress(hostAddress);
			packet.setPort(port);

			while (connected) {
				byte[] touchData = queue.take();
				packet.setData(touchData);
				socket.send(packet);

			}
		} catch (InterruptedException | IOException e) {
			InternalLogger.logException(getClass(), e);
		} finally {
			closeSocket();
		}

	}

	@Override public void interrupt() {
		super.interrupt();

		closeSocket();
	}

	private void closeSocket() {
		connected = false;
		if (socket != null && !socket.isClosed()) {
			socket.close();
			socket = null;
		}
	}
}
