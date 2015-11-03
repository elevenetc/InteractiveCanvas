package su.levenetc.android.interactivecanvas;

/**
 * Created by Eugene Levenetc.
 */
public interface IInteractiveCanvasLogger {
	void logException(String msg);
	void logException(Object msg);
	void logException(Class src, Throwable throwable);
}
