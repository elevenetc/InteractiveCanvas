package su.levenetc.android.interactivecanvas.samples.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import su.levenetc.android.interactivecanvas.InteractiveCanvas;
import su.levenetc.android.interactivecanvas.Utils;

/**
 * Created by Eugene Levenetc.
 */
public class PingPongCanvas extends InteractiveCanvas {

	private Paint blackFill = new Paint();
	private Paint greenStroke = new Paint();
	private Paint gradientFill = new Paint();
	private Player leftPlayer;
	private Player rightPlayer;
	private Ball ball;
	private long frameTime;
	private final PingPongController controller;

	public PingPongCanvas(Context context, int frameRate, int width, int height) {
		super(frameRate, width, height);
		blackFill.setStyle(Paint.Style.FILL);
		blackFill.setColor(Color.BLACK);
		blackFill.setAntiAlias(true);
		blackFill.setTextSize(Utils.getSp(context, 24));

		greenStroke.setAntiAlias(true);
		greenStroke.setStyle(Paint.Style.STROKE);
		greenStroke.setColor(Color.GREEN);

		leftPlayer = new Player(context, width, height, Player.SIDE_LEFT);
		rightPlayer = new Player(context, width, height, Player.SIDE_RIGHT);
		ball = new Ball(context, width, height);

		controller = new PingPongController(leftPlayer, rightPlayer, ball);

		gradientFill.setShader(new LinearGradient(0, 0, 0, width, Color.BLACK, Color.RED, Shader.TileMode.CLAMP));
	}

	@Override protected void onTouchEvent(int clientId, int action, int x, int y) {
		controller.onTouchEvent(action, x, y);
	}

	@Override protected void onDraw(Canvas canvas) {

		final long currentTime = System.currentTimeMillis();
		long timeDiff = 0;

		if (frameTime == 0) {
			frameTime = currentTime;
		} else {
			timeDiff = currentTime - frameTime;
			frameTime = currentTime;
		}

		canvas.drawColor(Color.BLACK);

		controller.update(canvas);

		leftPlayer.onDraw(canvas, currentTime);
		rightPlayer.onDraw(canvas, currentTime);
		ball.onDraw(canvas, timeDiff, currentTime);

		super.onDraw(canvas);
	}
}
