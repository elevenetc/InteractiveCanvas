package su.levenetc.android.interactivecanvas;

import android.support.annotation.Nullable;
import android.view.MotionEvent;

import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Eugene Levenetc.
 */
public class PictureReceiver implements Runnable, ITouchEventHandler {

	private ReceiverView receiverView;
	private InetAddress hostAddress;
	private int touchPort;
	private int picturesPort;
	private int clientId;
	private TouchEventsSendThread touchEventsSendThread;
	private PicturesReceiverThread picturesReceiverThread;
	private LinkedBlockingQueue<PictureWrapper> picturesQueue = new LinkedBlockingQueue<>();

	public PictureReceiver(int clientId) {
		this.clientId = clientId;
	}

	public void start() {
		picturesReceiverThread = new PicturesReceiverThread(picturesPort, receiverView, this, picturesQueue);
		picturesReceiverThread.start();
		touchEventsSendThread = new TouchEventsSendThread(hostAddress, touchPort, clientId);
		touchEventsSendThread.start();
	}

	public void destroy() {
		if (picturesReceiverThread != null) picturesReceiverThread.interrupt();
		if (touchEventsSendThread != null) touchEventsSendThread.interrupt();
	}

	public void setReceiverView(ReceiverView receiverView) {
		this.receiverView = receiverView;
	}

	@Override public void run() {
		try {
			receiverView.setPicture(picturesQueue.take());
		} catch (InterruptedException e) {
			InternalLogger.logException(getClass(), e);
		}
	}

	public void config(int picturesReceivePort, InetAddress hostAddress, int touchPort) {
		this.picturesPort = picturesReceivePort;
		this.hostAddress = hostAddress;
		this.touchPort = touchPort;
	}

	public void handleTouchEvent(MotionEvent event) {
		touchEventsSendThread.add(event);
	}

}
