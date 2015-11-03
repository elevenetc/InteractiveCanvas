package su.levenetc.android.interactivecanvas;

import android.graphics.Picture;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Eugene Levenetc.
 */
public class PictureSenderThread extends Thread {

	private volatile boolean connected = true;
	private DatagramSocket socket;
	private final byte[] buffer = new byte[Params.BUFFER_SIZE];
	private final Screen[] screens;
	private final int port;
	private final LinkedBlockingQueue<byte[]> picturesQueue = new LinkedBlockingQueue<>();
	private final ByteArrayOutputStream os = new ExtendedBAOS(Params.BUFFER_SIZE, Config.PICTURE_METADATA_SIZE * 32);

	public PictureSenderThread(Screen[] screens, int port) {
		this.port = port;
		this.screens = screens;
	}

	public void addPicture(Picture picture) {
		if (!connected) return;
		os.reset();
		picture.writeToStream(os);
		try {
			byte[] raw = os.toByteArray();
			picturesQueue.put(raw);
		} catch (InterruptedException e) {
			InternalLogger.logException(getClass(), e);
		}
	}

	@Override public void run() {
		socket = null;
		try {

			socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			while (connected) {

				byte[] raw = picturesQueue.take();

				for (Screen screen : screens) {

					Utils.putIntTo(raw, screen.dx, 0);
					Utils.putIntTo(raw, screen.dy, 32);

					packet.setAddress(screen.address);
					packet.setPort(port);
					packet.setData(raw);
					socket.send(packet);
				}
			}

		} catch (Exception e) {
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
