package org.omancode.util;

/**
 * Static utility class of string related functions.
 * 
 * @author Oliver Mannion
 * @version $Revision$
 */
public final class StringUtil {

	private StringUtil() {
		// static util class
	}

	/**
	 * Construct a function call string, eg: "func(1,2,3)".
	 * 
	 * @param functionName
	 *            function names
	 * @param args
	 *            args
	 * @return function call string
	 */
	public static String functionCall(String functionName, Object... args) {
		StringBuilder sb = new StringBuilder(64);

		sb.append(functionName).append("(");
		int i = 0;
		for (; i < args.length - 1; i++) {
			sb.append(args[i].toString()).append(", ");
		}
		sb.append(args[i].toString()).append(")");
		return sb.toString();
	}

	/**
	 * Wrap the string in double quotes.
	 * 
	 * @param str
	 *            input string
	 * @return s wrapped in double quotes.
	 */
	public static String doublequote(String str) {
		return "\"" + str + "\"";
	}
}
