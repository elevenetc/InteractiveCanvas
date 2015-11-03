package su.levenetc.android.interactivecanvas.samples.utils;

import android.util.Log;

import su.levenetc.android.interactivecanvas.IInteractiveCanvasLogger;

/**
 * Created by Eugene Levenetc.
 */
public class Logger implements IInteractiveCanvasLogger {

	public Logger() {

	}

	@Override public void logException(String msg) {
		Log.i("InteractiveCanvas", msg);
	}

	@Override public void logException(Object msg) {
		Log.i("InteractiveCanvas", msg.toString());
	}

	@Override public void logException(Class src, Throwable throwable) {
		Log.e("InteractiveCanvas", src.getSimpleName() + ": " + throwable.toString());
	}
}
