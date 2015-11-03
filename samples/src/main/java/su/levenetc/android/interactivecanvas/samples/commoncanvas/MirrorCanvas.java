package su.levenetc.android.interactivecanvas.samples.commoncanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import su.levenetc.android.interactivecanvas.InteractiveCanvas;
import su.levenetc.android.interactivecanvas.samples.utils.Utils;

/**
 * Created by Eugene Levenetc.
 */
public class MirrorCanvas extends InteractiveCanvas {

	private final DrawController controller;
	private final int radius;
	private final List<Marker> markers = new ArrayList<>();
	private final Paint paint = new Paint();
	private final Path path = new Path();
	private final int[] markersColors;

	public MirrorCanvas(Context context, int fps, int width, int height, int[] markersColors) {
		super(fps, width, height);
		this.markersColors = markersColors;
		controller = new DrawController();
		radius = (int) Utils.dpToPx(context, 30);

		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(Utils.dpToPx(context, 2));
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
	}

	@Override protected void onTouchEvent(int clientId, int action, int x, int y) {
		controller.onTouchEvent(action, x, y);
		if (action == MotionEvent.ACTION_DOWN) {
			markers.add(new Marker(x, y, radius, markersColors[clientId]));
		}
	}

	@Override protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		path.reset();
		for (int i = 0; i < markers.size(); i++) {
			Marker marker = markers.get(i);
			if (i == 0) path.moveTo(marker.x, marker.y);
			path.lineTo(marker.x, marker.y);
			marker.onDraw(canvas, paint);
		}
		paint.setColor(Color.WHITE);
		canvas.drawPath(path, paint);

		super.onDraw(canvas);
	}

	private static class Marker {

		private final int size;
		private int color;
		private final int x;
		private final int y;
		private int rotation;

		public Marker(int x, int y, int size, int color) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.color = color;
		}

		void onDraw(Canvas canvas, Paint paint) {
			paint.setColor(color);
			canvas.save();
			canvas.translate(x, y);
			canvas.rotate(rotation);
			canvas.drawRect(-size / 2, -size / 2, size / 2, size / 2, paint);
			canvas.restore();
			rotation++;
		}
	}
}