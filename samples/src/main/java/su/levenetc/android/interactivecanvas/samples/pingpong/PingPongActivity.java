package su.levenetc.android.interactivecanvas.samples.pingpong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import java.net.InetAddress;

import su.levenetc.android.interactivecanvas.ITouchEventHandler;
import su.levenetc.android.interactivecanvas.InternalLogger;
import su.levenetc.android.interactivecanvas.PictureReceiver;
import su.levenetc.android.interactivecanvas.ReceiverView;
import su.levenetc.android.interactivecanvas.Screen;
import su.levenetc.android.interactivecanvas.samples.R;
import su.levenetc.android.interactivecanvas.samples.utils.Logger;
import su.levenetc.android.interactivecanvas.samples.utils.Utils;

public class PingPongActivity extends AppCompatActivity {

	private static final InetAddress HOST_ADDRESS = Utils.getAddress("192.168.56.101");
	private static final InetAddress[] CLIENT_ADDRESSES = new InetAddress[]{
			HOST_ADDRESS,
			Utils.getAddress("192.168.56.102"),
			Utils.getAddress("192.168.56.103"),
			Utils.getAddress("192.168.56.104")
	};

	private static final int FRAME_RATE = 1000 / 30;
	private static final int PICTURES_PORT = 55880;
	private static final int TOUCH_EVENTS_PORT = 55881;

	private PingPongCanvas hostCanvas;
	private PictureReceiver pictureReceiver;
	private ReceiverView receiverView;

	@Override protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_pingpong);

		InternalLogger.setLogger(new Logger());

		receiverView = (ReceiverView) findViewById(R.id.receiver_view);
		receiverView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override public void onGlobalLayout() {
				receiverView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				config(receiverView.getWidth(), receiverView.getHeight());
			}
		});
	}

	private void config(final int screenWidth, final int screenHeight) {

		final Screen[] screens = new Screen[CLIENT_ADDRESSES.length];

		for (int i = 0; i < screens.length; i++) {
			screens[i] = new Screen(CLIENT_ADDRESSES[i], screenWidth * i * -1, 0);

			//one device - one client
			if (Utils.isCurrentIp(CLIENT_ADDRESSES[i].getHostAddress())) startClient(i);
		}

		if (Utils.isCurrentIp(HOST_ADDRESS.getHostAddress())) {
			startHost(screens, screenWidth, screenHeight);
		}

		receiverView.config(screenWidth * screens.length, screenHeight);
		receiverView.setTouchHandler(new ITouchEventHandler() {
			@Override public void handleTouchEvent(MotionEvent event) {
				if (pictureReceiver != null) pictureReceiver.handleTouchEvent(event);
			}
		});
	}

	private void startHost(Screen[] screens, int screenWidth, int screenHeight) {
		hostCanvas = new PingPongCanvas(
				PingPongActivity.this,
				FRAME_RATE, screenWidth * screens.length,
				screenHeight
		);
		hostCanvas.config(screens, PICTURES_PORT, TOUCH_EVENTS_PORT);
		hostCanvas.start();
	}

	private void startClient(int clientId) {
		pictureReceiver = new PictureReceiver(clientId);
		pictureReceiver.config(PICTURES_PORT, HOST_ADDRESS, TOUCH_EVENTS_PORT);
		pictureReceiver.setReceiverView(receiverView);
		pictureReceiver.start();
	}

	@Override protected void onDestroy() {
		destroyHost();
		destroyClient();
		super.onDestroy();
	}

	private void destroyClient() {
		if (pictureReceiver != null) {
			pictureReceiver.destroy();
			pictureReceiver = null;
		}
	}

	private void destroyHost() {
		if (hostCanvas != null) {
			hostCanvas.destroy();
			hostCanvas = null;
		}
	}


}
