package org.omancode.util;

/**
 * Static utility class of array functions.
 * 
 * @author Oliver Mannion
 * @version $Revision$
 */
public final class ArrayUtil {

	private ArrayUtil() {
		// static util class
	}

	/**
	 * The index value when an element is not found in a list or array:
	 * <code>-1</code>. This value is returned by methods in this class and can
	 * also be used in comparisons with values returned by various method from
	 * {@link java.util.List}.
	 */
	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * Create a string of the array's contents, in the same way that
	 * {@link java.util.Arrays#toString(Object[])} does but, without enclosing
	 * square brackets.
	 * 
	 * @param array
	 *            array to convert to a string
	 * @return string representation of array
	 */
	public static String toString(Object[] array) {
		if (array == null) {
			return "null";
		}

		int iMax = array.length - 1;
		if (iMax == -1) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0;; i++) {
			builder.append(String.valueOf(array[i]));
			if (i == iMax) {
				return builder.toString();
			}
			builder.append(", ");
		}
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of String are
	 * <i>equal</i> to one another. The two arrays are considered equal if both
	 * arrays contain the same number of elements, and all corresponding pairs
	 * of elements in the two arrays are equal. Two objects <tt>e1</tt> and
	 * <tt>e2</tt> are considered <i>equal</i> if <tt>(e1==null ? e2==null
	 * : e1.equals(e2))</tt>. In other words, the two arrays are equal if they
	 * contain the same elements in the same order. Also, two array references
	 * are considered equal if both are <tt>null</tt>.
	 * <p>
	 * 
	 * @param array1
	 *            one array to be tested for equality
	 * @param array2
	 *            the other array to be tested for equality
	 * @param caseInsensitive
	 *            if true, the string arrays are compared without regard to case
	 * @return <tt>true</tt> if the two arrays are equal
	 */
	public static boolean isEquals(String[] array1, String[] array2,
			boolean caseInsensitive) {
		if (array1 == array2) {
			return true;
		}
		if (array1 == null || array2 == null) {
			return false;
		}

		int length = array1.length;
		if (array2.length != length) {
			return false;
		}

		for (int i = 0; i < length; i++) {
			String string1 = array1[i];
			String string2 = array2[i];
			if (!(string1 == null ? string2 == null : caseInsensitive ? string1
					.equalsIgnoreCase(string2) : string1.equals(string2))) {
				return false;
			}

		}

		return true;
	}

	/**
	 * Returns the first string in {@code subArray} that does not exist in
	 * {@code superArray}. Returns {@code null} if there are no complements (ie:
	 * {@code subArray} is a subset of {@code superArray}, in other words, all
	 * the strings in {@code subArray} exist in {@code superArray}.
	 * 
	 * @param superArray
	 *            array of strings to be searched
	 * @param subArray
	 *            array of strings to find
	 * @param caseInsensitive
	 *            if true, the string arrays are compared without regard to case
	 * @return {@code null} if {@code superArray} contains {@code subArray},
	 *         otherwise returns the first string in {@code subArray} that does
	 *         not exist in {@code superArray}.
	 */
	public static String firstStringComplement(String[] superArray,
			String[] subArray, boolean caseInsensitive) {

		for (int i = 0; i < subArray.length; i++) {
			if (indexOfString(superArray, subArray[i], caseInsensitive, 0) == INDEX_NOT_FOUND) {
				return subArray[i];
			}
		}

		return null;
	}

	/**
	 * Convenience method that calls
	 * {@link #indexOfString(String[], String, boolean)} with a caseInsensitive
	 * = true.
	 * 
	 * @param array
	 *            the array to search through for the object, may be
	 *            <code>null</code>
	 * @param stringToFind
	 *            the object to find, may be <code>null</code>
	 * @return the index of the object within the array starting at the index,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int indexOfString(String[] array, String stringToFind) {
		return indexOfString(array, stringToFind, true);
	}

	/**
	 * Convenience method that calls
	 * {@link #indexOfString(String[], String, boolean, int)} with a startIndex
	 * of 0.
	 * 
	 * @param array
	 *            the array to search through for the object, may be
	 *            <code>null</code>
	 * @param stringToFind
	 *            the object to find, may be <code>null</code>
	 * @param caseInsensitive
	 *            if true, the strings are compared without regard to case
	 * @return the index of the object within the array starting at the index,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int indexOfString(String[] array, String stringToFind,
			boolean caseInsensitive) {
		return indexOfString(array, stringToFind, caseInsensitive, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given object in the array starting at the given
	 * index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the
	 * array length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).
	 * </p>
	 * 
	 * @param array
	 *            the array to search through for the object, may be
	 *            <code>null</code>
	 * @param stringToFind
	 *            the object to find, may be <code>null</code>
	 * @param caseInsensitive
	 *            if true, the strings are compared without regard to case
	 * @param startIndex
	 *            the index to start searching at
	 * @return the index of the object within the array starting at the index,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int indexOfString(String[] array, String stringToFind,
			boolean caseInsensitive, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}

		int start = startIndex < 0 ? 0 : startIndex;

		if (stringToFind == null) {
			for (int i = start; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}
		} else if (caseInsensitive) {
			for (int i = start; i < array.length; i++) {
				if (stringToFind.equalsIgnoreCase(array[i])) {
					return i;
				}
			}
		} else {
			for (int i = start; i < array.length; i++) {
				if (stringToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * Set all array elements to zero.
	 * 
	 * @param array
	 *            array to zero
	 */
	public static void zeroArray(double[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = 0;
		}
	}

	/**
	 * Return the number of {@code true} elements in {@code array}.
	 * 
	 * @param array
	 *            boolean array
	 * @return number of {@code true} elements in {@code array}.
	 */
	public static double count(boolean[] array) {
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i]) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Transpose a 2D Object[][] array. eg: {0,1,2},{3,4,5} becomes
	 * {0,3},{1,4},{2,5}.
	 * 
	 * @param array
	 *            array to transpose
	 * @return transposed array
	 */
	public static Object[][] transpose(Object[][] array) {
		Object[][] result = new Object[array[0].length][array.length];

		for (int outer = 0; outer < array.length; outer++) {

			for (int inner = 0; inner < array[outer].length; inner++) {
				result[inner][outer] = array[outer][inner];
			}

		}

		return result;
	}

	/**
	 * Transpose a 2D double[][] array. eg: {0,1,2},{3,4,5} becomes
	 * {0,3},{1,4},{2,5}.
	 * 
	 * @param array
	 *            array to transpose
	 * @return transposed array
	 */
	public static double[][] transpose(double[][] array) {
		double[][] result = new double[array[0].length][array.length];

		for (int outer = 0; outer < array.length; outer++) {

			for (int inner = 0; inner < array[outer].length; inner++) {
				result[inner][outer] = array[outer][inner];
			}

		}

		return result;
	}

	/**
	 * Sum integer array.
	 * 
	 * @param array
	 *            array to sum
	 * @return sum
	 */
	public static int sum(int[] array) {
		int result = 0;

		for (int i = 0; i < array.length; i++) {
			result = result + array[i];
		}

		return result;
	}

}
