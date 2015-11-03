package su.levenetc.android.interactivecanvas;

import java.io.ByteArrayOutputStream;

/**
 * Created by Eugene Levenetc.
 */
class ExtendedBAOS extends ByteArrayOutputStream {

	private final int extraBytes;

	public ExtendedBAOS(int size, int extraBytes) {
		super(size);
		this.extraBytes = extraBytes;
	}

	@Override public synchronized byte[] toByteArray() {
		byte[] newArray = new byte[count + extraBytes];
		System.arraycopy(buf, 0, newArray, extraBytes, count);
		return newArray;
	}
}
