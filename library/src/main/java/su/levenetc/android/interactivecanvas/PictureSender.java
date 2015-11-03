package su.levenetc.android.interactivecanvas;

import android.graphics.Picture;
import android.support.annotation.Nullable;

/**
 * Created by Eugene Levenetc.
 */
public class PictureSender {

	private TouchEventsReceiverThread touchEventsReceiverThread;
	private InteractiveCanvas interactiveCanvas;
	private PictureSenderThread pictureSenderThread;
	private Screen[] screens;
	private int picturesPort;
	private int touchEventsPort;

	public PictureSender() {

	}

	public void setInteractiveCanvas(InteractiveCanvas interactiveCanvas) {
		this.interactiveCanvas = interactiveCanvas;
	}

	public void addPicture(Picture picture) {
		pictureSenderThread.addPicture(picture);
	}

	public void start() {
		pictureSenderThread = new PictureSenderThread(screens, picturesPort);
		pictureSenderThread.start();

		touchEventsReceiverThread = new TouchEventsReceiverThread(touchEventsPort, interactiveCanvas);
		touchEventsReceiverThread.start();
	}

	public void destroy() {
		if (pictureSenderThread != null) pictureSenderThread.interrupt();
		if (touchEventsReceiverThread != null) touchEventsReceiverThread.interrupt();
	}

	public void config(Screen[] screens, int picturesPort, int touchEventsPort) {
		this.picturesPort = picturesPort;
		this.screens = screens;
		this.touchEventsPort = touchEventsPort;
	}


}
