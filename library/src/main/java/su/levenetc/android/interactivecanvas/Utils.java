package su.levenetc.android.interactivecanvas;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Eugene Levenetc.
 */
public class Utils {

	public static void putIntTo(byte[] newArray, int i, int offset) {
		newArray[offset] = (byte) ((i >> 24) & 0xFF);
		newArray[1 + offset] = (byte) ((i >> 16) & 0xFF);
		newArray[2 + offset] = (byte) ((i >> 8) & 0xFF);
		newArray[3 + offset] = (byte) (i & 0xFF);
	}

	public static int byteArrayToInt(byte[] b, int offset) {
		return b[3 + offset] & 0xFF | (b[2 + offset] & 0xFF) << 8 | (b[1 + offset] & 0xFF) << 16 | (b[offset] & 0xFF) << 24;
	}

	public static float getSp(Context context, float sp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
	}

}
