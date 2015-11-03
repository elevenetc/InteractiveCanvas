package su.levenetc.android.interactivecanvas;

/**
 * Created by Eugene Levenetc.
 */
public class InternalLogger {

	private static IInteractiveCanvasLogger logger;

	public static void setLogger(IInteractiveCanvasLogger logger) {
		InternalLogger.logger = logger;
	}

	public static void logException(Class src, Throwable e) {
		if (logger != null) logger.logException(src, e);
	}
}
