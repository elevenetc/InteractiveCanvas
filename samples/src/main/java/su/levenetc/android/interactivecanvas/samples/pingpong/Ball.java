package su.levenetc.android.interactivecanvas.samples.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by Eugene Levenetc.
 */
public class Ball {

	final int size;
	final Rect bounds = new Rect();
	final PointF vector = new PointF();
	private final Paint fillPaint = new Paint();
	private final Rect[] tail = new Rect[20];
	private static final float SPEED = 1.7f;
	private long taleUpdateTime;

	public Ball(Context context, int canvasWidth, int canvasHeight) {
		size = (int) su.levenetc.android.interactivecanvas.samples.utils.Utils.dpToPx(context, 50);
		fillPaint.setColor(Color.CYAN);
		fillPaint.setStyle(Paint.Style.FILL);
		fillPaint.setAntiAlias(true);

		bounds.set(0, 0, size, size);
		bounds.offset(canvasHeight / 2, canvasHeight / 2);

		for (int i = 0; i < tail.length; i++) tail[i] = new Rect(bounds);

		vector.set(
				(float) Math.random() * (Math.random() > 0.5f ? 1 : -1),
				(float) Math.random() * (Math.random() > 0.5f ? 1 : -1)
		);
	}

	public void setPosition(int x, int y) {
		bounds.offsetTo(x, y);
	}

	public void onDraw(Canvas canvas, long timeDiff, long currentTime) {
		updatePosition(timeDiff);

		canvas.drawRect(bounds, fillPaint);
		drawTail(canvas, timeDiff, currentTime);
	}

	private void drawTail(Canvas canvas, long timeDiff, long currentTime) {

		final int step = 255 / tail.length;
		int alpha = 255;
		for (Rect tailPoint : tail) {
			fillPaint.setAlpha(alpha);
			canvas.drawRect(bounds, fillPaint);
			canvas.drawRect(tailPoint, fillPaint);
			alpha -= step;
		}
		fillPaint.setAlpha(255);

		taleUpdateTime = 0;
		Rect first = tail[tail.length - 1];
		System.arraycopy(tail, 0, tail, 1, tail.length - 1);
		tail[0] = first;
		tail[0].offsetTo(bounds.left, bounds.top);

//		if (taleUpdateTime > 50) {
//			taleUpdateTime = 0;
//			Rect first = tail[tail.length - 1];
//			System.arraycopy(tail, 0, tail, 1, tail.length - 1);
//			tail[0] = first;
//			tail[0].offsetTo(bounds.left, bounds.top);
//		} else {
//			taleUpdateTime += timeDiff;
//		}
	}

	private void updatePosition(long timeDiff) {
		bounds.offset(
				(int) (SPEED * timeDiff * vector.x),
				(int) (SPEED * timeDiff * vector.y)
		);
	}


}
