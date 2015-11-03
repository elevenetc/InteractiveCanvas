package su.levenetc.android.interactivecanvas.samples.pingpong;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Eugene Levenetc.
 */
public class PingPongController {

	private final Player playerLeft;
	private final Player playerRight;
	private final Ball ball;

	public PingPongController(
			Player playerLeft,
			Player playerRight,
			Ball ball
	) {
		this.playerLeft = playerLeft;
		this.playerRight = playerRight;
		this.ball = ball;
	}

	public void update(Canvas canvas) {

		final int canvasWidth = canvas.getWidth();
		final int canvasHeight = canvas.getHeight();

		if (playerLeft.intersectsWith(ball.bounds)) {
			turnBallX();
			ball.bounds.offsetTo(playerLeft.bounds.right + 1, ball.bounds.top);
		} else if (playerRight.intersectsWith(ball.bounds)) {
			turnBallX();
			ball.bounds.offsetTo(playerRight.bounds.left - ball.size - 1, ball.bounds.top);
		} else if (ball.bounds.right >= canvasWidth) {
			turnBallX();
			ball.bounds.offsetTo(canvasWidth - ball.size - 1, ball.bounds.top);
			playerRight.showLose();
		} else if (ball.bounds.left <= 0) {
			turnBallX();
			ball.bounds.offsetTo(1, ball.bounds.top);
			playerLeft.showLose();
		} else if (ball.bounds.bottom >= canvasHeight) {
			turnBallY();
			ball.bounds.offsetTo(ball.bounds.left, canvasHeight - ball.size - 1);
		} else if (ball.bounds.top <= 0) {
			turnBallY();
			ball.bounds.offsetTo(ball.bounds.left, 1);
		}
	}

	private void turnBallY() {
		ball.vector.set(ball.vector.x, ball.vector.y * -1);
	}

	private void turnBallX() {
		ball.vector.set(ball.vector.x * -1, ball.vector.y);
	}

	public void onTouchEvent(int action, int x, int y) {

		if (action == MotionEvent.ACTION_DOWN) {

			if (playerLeft.contains(x, y)) playerLeft.startDrag(x, y);
			else if (playerRight.contains(x, y)) playerRight.startDrag(x, y);

		} else if (action == MotionEvent.ACTION_MOVE) {

			if (playerLeft.contains(x, y)) playerLeft.onDrag(y);
			else if (playerRight.contains(x, y)) playerRight.onDrag(y);

		} else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {

			if (playerLeft.contains(x, y)) playerLeft.stopDrag();
			else if (playerRight.contains(x, y)) playerRight.stopDrag();

		}


	}
}
