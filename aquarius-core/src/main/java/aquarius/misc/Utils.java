package aquarius.misc;

import java.text.DecimalFormat;

/**
 * some helper methods
 * @author skgchxngsxyz-opensuse
 *
 */
public class Utils {
	private Utils() {}

	/**
	 * show memory
	 * @param message
	 * print this message before printing memory
	 */
	public final static void showMemory(String message) {
		MemoryInfo info = getMemoryInfo();
		System.err.println(message);
		System.err.println(info);
		System.err.println();
	}

	public final static MemoryInfo getMemoryInfo() {
		System.gc();
		return new MemoryInfo();
	}

	public static class MemoryInfo {
		private final static DecimalFormat f = new DecimalFormat("#,###KB");
		private final static DecimalFormat f2 = new DecimalFormat("##.#");

		public final long total;
		public final long free;
		public final long max;
		public final long used;
		public final double ratio;

		private MemoryInfo() {
			this.total = Runtime.getRuntime().totalMemory() / 1024;
			this.free = Runtime.getRuntime().freeMemory() / 1024;
			this.max = Runtime.getRuntime().maxMemory() / 1024;
			this.used = this.total - this.free;
			this.ratio = this.used * 100 / (double) this.total;
		}

		@Override
		public String toString() {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("total memory: " + f.format(this.total));
			sBuilder.append(System.lineSeparator());
			sBuilder.append("free  memory: " + f.format(this.free));
			sBuilder.append(System.lineSeparator());
			sBuilder.append("used  memory: " + f.format(this.used) + " (" + f2.format(this.ratio) + "%)");
			sBuilder.append(System.lineSeparator());
			sBuilder.append("max   memory: " + f.format(this.max));
			return sBuilder.toString();
		}
	}

	/**
	 * propagate exception
	 * @param e
	 * @return
	 * never return. 
	 * @throws RuntimeException
	 * if e is RuntimeException, rethrow it.
	 * otherwise wrapped by RuntimeException.
	 */
	public final static boolean propagate(Exception e) throws RuntimeException {
		if(e instanceof RuntimeException) {
			throw (RuntimeException) e;
		}
		throw new RuntimeException(e);
	}
}
