package su.levenetc.android.interactivecanvas.samples.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by Eugene Levenetc.
 */
public class Utils {

	/**
	 * Omits exception. For tests purposes only
	 */
	public static InetAddress getAddress(String ip) {
		try {
			return InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static float dpToPx(Context context, float dp) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static boolean isCurrentIp(String ip) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					String s = inetAddress.getHostAddress();
					if (s.equals(ip)) {
						return true;
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return false;
	}
}