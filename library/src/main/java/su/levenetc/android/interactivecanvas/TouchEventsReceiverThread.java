package su.levenetc.android.interactivecanvas;

import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Eugene Levenetc.
 */
class TouchEventsReceiverThread extends Thread {

	private volatile boolean connected = true;
	private final byte[] buffer = new byte[32 * Config.TOUCH_METADATA_SIZE];
	private final ByteArrayInputStream is = new ByteArrayInputStream(buffer);
	private final int port;
	private final InteractiveCanvas interactiveCanvas;
	private DatagramSocket socket;

	public TouchEventsReceiverThread(int port, InteractiveCanvas interactiveCanvas) {
		this.port = port;
		this.interactiveCanvas = interactiveCanvas;
	}

	@Override public void run() {
		try {
			socket = new DatagramSocket(port);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			while (connected) {
				is.reset();
				socket.receive(packet);

				final int clientId = Utils.byteArrayToInt(buffer, 0);
				final int action = Utils.byteArrayToInt(buffer, 32);
				final int x = Utils.byteArrayToInt(buffer, 64);
				final int y = Utils.byteArrayToInt(buffer, 96);

				interactiveCanvas.onTouchEvent(clientId, action, x, y);
			}
		} catch (IOException e) {
			InternalLogger.logException(getClass(), e);
		} finally {
			closeSocket();
		}

	}

	@Override public void interrupt() {
		closeSocket();
		super.interrupt();
	}

	private void closeSocket() {
		connected = false;
		if (socket != null && !socket.isClosed()) {
			socket.close();
			socket = null;
		}
	}
}
