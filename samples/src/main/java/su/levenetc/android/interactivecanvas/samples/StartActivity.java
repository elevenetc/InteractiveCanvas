package su.levenetc.android.interactivecanvas.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import su.levenetc.android.interactivecanvas.samples.commoncanvas.MirrorActivity;
import su.levenetc.android.interactivecanvas.samples.pingpong.PingPongActivity;

/**
 * Created by Eugene Levenetc.
 */
public class StartActivity extends AppCompatActivity {
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		findViewById(R.id.btn_start_mirror).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				startActivity(new Intent(StartActivity.this, MirrorActivity.class));
			}
		});

		findViewById(R.id.btn_start_pingpong).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				startActivity(new Intent(StartActivity.this, PingPongActivity.class));
			}
		});
	}
}
