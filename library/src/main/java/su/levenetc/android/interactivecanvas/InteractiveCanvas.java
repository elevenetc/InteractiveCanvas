package su.levenetc.android.interactivecanvas;

import android.graphics.Canvas;
import android.graphics.Picture;

/**
 * Created by Eugene Levenetc.
 */
public class InteractiveCanvas {

	private final Picture picture = new Picture();
	private final PictureSender pictureSender = new PictureSender();
	private volatile boolean drawing = true;
	private int fps;
	private int width;
	private int height;

	public InteractiveCanvas(int fps, int width, int height) {
		this.fps = fps;
		this.width = width;
		this.height = height;
		pictureSender.setInteractiveCanvas(this);
	}

	public final void begin() {
		onDraw(picture.beginRecording(width, height));
	}

	protected void onDraw(Canvas canvas) {
		picture.endRecording();
		pictureSender.addPicture(picture);
	}

	protected void onTouchEvent(int clientId, int action, int x, int y) {

	}

	public final void start() {
		new Thread(new Runnable() {
			@Override public void run() {
				pictureSender.start();

				while (drawing) {
					begin();
					try {
						Thread.sleep(fps);
					} catch (InterruptedException e) {
						//ignore interruption
					}
				}

				pictureSender.destroy();
			}
		}).start();
	}

	public void destroy() {
		drawing = false;
		pictureSender.destroy();
	}

	public final void config(Screen[] screens, int picturesPort, int touchEventsPort) {
		pictureSender.config(screens, picturesPort, touchEventsPort);
	}

}