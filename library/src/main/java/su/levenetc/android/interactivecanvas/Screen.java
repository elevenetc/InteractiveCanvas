package su.levenetc.android.interactivecanvas;

import java.net.InetAddress;

/**
 * Created by Eugene Levenetc.
 */
public class Screen {
	public final int dx;
	public final int dy;
	public final InetAddress address;

	public Screen(InetAddress address, int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		this.address = address;
	}
}
