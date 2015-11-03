package su.levenetc.android.interactivecanvas;

import android.graphics.Picture;
import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Eugene Levenetc.
 */
public class PicturesReceiverThread extends Thread {

	private volatile boolean connected = true;
	private DatagramSocket socket;
	private final ReceiverView receiverView;
	private final Runnable postViewRunnable;
	private final int port;
	private final byte[] buffer = new byte[Params.BUFFER_SIZE];
	private final ByteArrayInputStream is = new ByteArrayInputStream(buffer);
	private final LinkedBlockingQueue<PictureWrapper> picturesQueue;

	public PicturesReceiverThread(
			int port,
			ReceiverView receiverView,
			Runnable postViewRunnable,
			LinkedBlockingQueue<PictureWrapper> picturesQueue
	) {
		this.port = port;
		this.receiverView = receiverView;
		this.postViewRunnable = postViewRunnable;
		this.picturesQueue = picturesQueue;
	}

	@Override public void run() {

		while (connected) {
			try {
				socket = new DatagramSocket(port);
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				while (connected) {
					is.reset();
					socket.receive(packet);
					is.skip(Config.PICTURE_METADATA_SIZE * 32);

					final int dx = Utils.byteArrayToInt(buffer, 0);
					final int dy = Utils.byteArrayToInt(buffer, 32);
					picturesQueue.put(new PictureWrapper(dx, dy, Picture.createFromStream(is)));
					receiverView.post(postViewRunnable);
				}

			} catch (Throwable e) {
				InternalLogger.logException(getClass(), e);
			} finally {
				closeSocket();
			}
		}
	}

	private void closeSocket() {
		connected = false;
		if (socket != null && !socket.isClosed()) {
			socket.close();
			socket = null;
		}
	}

	@Override public void interrupt() {
		super.interrupt();
		closeSocket();
	}
}
