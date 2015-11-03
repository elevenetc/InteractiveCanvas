package su.levenetc.android.interactivecanvas.samples.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Created by Eugene Levenetc.
 */
public class Player {

	public static final byte SIDE_LEFT = 0;
	public static final byte SIDE_RIGHT = 1;
	private final int loseRectWidth;

	private Paint playerPaint = new Paint();
	private Paint losePaint = new Paint();
	private int side;
	public Rect bounds = new Rect();
	private int downX;
	private int downY;
	private int startDragTop;
	private LinearInterpolator loseAlphaInterpolator;

	public Player(Context context, int canvasWidth, int canvasHeight, int side) {
		this.side = side;
		playerPaint.setColor(Color.WHITE);
		playerPaint.setStyle(Paint.Style.FILL);
		playerPaint.setAntiAlias(true);

		loseRectWidth = (int) su.levenetc.android.interactivecanvas.samples.utils.Utils.dpToPx(context, 250);


		bounds.set(0, 0, (int) su.levenetc.android.interactivecanvas.samples.utils.Utils.dpToPx(context, 50), (int) su.levenetc.android.interactivecanvas.samples.utils.Utils.dpToPx(context, 150));

		if (side == SIDE_LEFT) {
			losePaint.setShader(
					new LinearGradient(0, 0, loseRectWidth, 0, Color.RED, Color.TRANSPARENT, Shader.TileMode.CLAMP)
			);
		} else {
			bounds.offsetTo(canvasWidth - bounds.width(), 0);
			losePaint.setShader(
					new LinearGradient(canvasWidth - loseRectWidth, 0, canvasWidth, 0, Color.TRANSPARENT, Color.RED, Shader.TileMode.CLAMP)
			);
		}
	}

	public boolean contains(int x, int y) {
		return bounds.contains(x, y);
	}

	public boolean intersectsWith(Rect rect) {
		return Rect.intersects(rect, bounds);
	}

	public void onDraw(Canvas canvas, long currentTime) {

		canvas.drawRect(bounds, playerPaint);
		drawLoseEffect(canvas, currentTime);
	}

	private void drawLoseEffect(Canvas canvas, long currentTime) {
		if (loseAlphaInterpolator == null) return;
		final int alpha = loseAlphaInterpolator.getValue(currentTime);
		if (alpha >= 0) {

			losePaint.setAlpha(alpha);

			if (side == SIDE_LEFT) {
				canvas.drawRect(0, 0, loseRectWidth, canvas.getHeight(), losePaint);
			} else {
				int width = canvas.getWidth();
				canvas.drawRect(width - loseRectWidth, 0, width, canvas.getHeight(), losePaint);
			}

			if (alpha == 0) loseAlphaInterpolator = null;
		}
	}

	public void startDrag(int downX, int downY) {
		this.downX = downX;
		this.downY = downY;
		startDragTop = bounds.top;
	}

	public void stopDrag() {

	}

	public void onDrag(int y) {
		bounds.offsetTo(bounds.left, startDragTop + y - downY);
	}

	public void showLose() {
		loseAlphaInterpolator = new LinearInterpolator(255, 0, 300);
	}
}
