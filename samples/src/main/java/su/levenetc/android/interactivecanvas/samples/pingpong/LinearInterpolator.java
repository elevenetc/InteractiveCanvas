package su.levenetc.android.interactivecanvas.samples.pingpong;

/**
 * Created by Eugene Levenetc.
 */
public class LinearInterpolator {

	private int from;
	private int to;
	private long duration;
	private long elapsedTime;
	private long startTime = -1;

	public LinearInterpolator(int from, int to, long duration) {
		this.from = from;
		this.to = to;
		this.duration = duration;
	}

	public int getValue(long currentTime) {
		if (startTime == -1) startTime = currentTime;
		float t = (currentTime - startTime) / (float) duration;
		if (t > 1) return to;
		return (int) ((1 - t) * from + t * to);
	}
}
