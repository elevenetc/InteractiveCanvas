package su.levenetc.android.interactivecanvas;

import android.graphics.Picture;

/**
 * Created by Eugene Levenetc.
 */
public class PictureWrapper {
	public final int dx;
	public final int dy;
	public final Picture picture;

	public PictureWrapper(int dx, int dy, Picture picture) {
		this.dx = dx;
		this.dy = dy;
		this.picture = picture;
	}
}