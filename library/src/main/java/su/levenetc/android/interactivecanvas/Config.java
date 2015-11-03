package su.levenetc.android.interactivecanvas;

/**
 * Created by Eugene Levenetc.
 */
public class Config {
	/**
	 * 0 int - clientId
	 * 1 int - action {@link android.view.MotionEvent.ACTION_DOWN and others}
	 * 2 int - x in pixels
	 * 3 int - y in pixels
	 */
	public static final int TOUCH_METADATA_SIZE = 4;

	/**
	 * 0 int - x screen shift int pixels
	 * 1 int - y screen shift int pixels
	 */
	public static final int PICTURE_METADATA_SIZE = 2;
}
