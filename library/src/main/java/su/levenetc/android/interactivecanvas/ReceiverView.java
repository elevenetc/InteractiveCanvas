package su.levenetc.android.interactivecanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Eugene Levenetc.
 */
public class ReceiverView extends View {

	private Picture picture;
	private int dx;
	private int dy;
	private Paint textPaint = new Paint();
	private Rect bounds = new Rect();
	private ITouchEventHandler touchHandler;
	private int fullCanvasWidth;
	private int fullCanvasHeight;

	public ReceiverView(Context context) {
		super(context);
		init();
	}

	public ReceiverView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setTouchHandler(ITouchEventHandler touchHandler) {
		this.touchHandler = touchHandler;
	}

	private void init() {

		setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.BLACK);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(Utils.getSp(getContext(), 24));

		setBackgroundColor(Color.GRAY);
		setWillNotDraw(false);
	}

	public void setPicture(PictureWrapper wrapper) {
		this.picture = wrapper.picture;
		this.dx = wrapper.dx;
		this.dy = wrapper.dy;
		bounds.set(0, 0, fullCanvasWidth, fullCanvasHeight);
		invalidate();
	}

	@Override public boolean onTouchEvent(MotionEvent event) {
		if (touchHandler != null) {
			event.setLocation(event.getX() - dx, event.getY() - dy);
			touchHandler.handleTouchEvent(event);
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}

	@Override protected void onDraw(Canvas canvas) {
		if (picture != null) {
			canvas.translate(dx, dy);
			canvas.drawPicture(picture, bounds);
		} else {
			final String waiting = "Waiting...";
			canvas.drawText(waiting, canvas.getWidth() / 2 - textPaint.measureText(waiting) / 2, canvas.getHeight() / 2, textPaint);
		}
	}

	public void config(int fullCanvasWidth, int fullCanvasHeight) {
		this.fullCanvasWidth = fullCanvasWidth;
		this.fullCanvasHeight = fullCanvasHeight;
	}
}
