package aquarius.misc;

import java.text.DecimalFormat;

/**
 * some helper methods
 * @author skgchxngsxyz-opensuse
 *
 */
public class Utils {
	private final static DecimalFormat f = new DecimalFormat("#,###KB");
	private final static DecimalFormat f2 = new DecimalFormat("##,#");

	/**
	 * show memory
	 * @param message
	 * print this message before printing memory
	 */
	public final static void showMemory(String message) {
		long total = Runtime.getRuntime().totalMemory() / 1024;
		long free = Runtime.getRuntime().freeMemory() / 1024;
		long max = Runtime.getRuntime().maxMemory() / 1024;
		long used = total - free;
		double ratio = used * 100 / (double) total;

		System.err.println(message);

		System.err.println("total memory: " + f.format(total));
		System.err.println("free  memory: " + f.format(free));
		System.err.println("used  memory: " + f.format(used) + " (" + f2.format(ratio) + "%)");
		System.err.println("max   memory: " + f.format(max));
		System.err.println();
	}
}
