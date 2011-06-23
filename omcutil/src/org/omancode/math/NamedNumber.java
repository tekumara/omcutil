package org.omancode.math;

/**
 * A number with a name.
 * 
 * @author Oliver Mannion
 * @version $Revision$
 */
public class NamedNumber extends Number {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5106968455392845611L;
	private final Number number;
	private final String name;

	/**
	 * Create a {@link NamedNumber} from a int.
	 * 
	 * @param name
	 *            name
	 * @param value
	 *            int value
	 */
	public NamedNumber(String name, int value) {
		this(name, Integer.valueOf(value));
	}

	/**
	 * Create a {@link NamedNumber} from a double.
	 * 
	 * @param name
	 *            name
	 * @param value
	 *            double value
	 */
	public NamedNumber(String name, double value) {
		this(name, new Double(value));
	}

	/**
	 * Create a {@link NamedNumber} from a Number.
	 * 
	 * @param name
	 *            name
	 * @param number
	 *            number
	 */
	public NamedNumber(String name, Number number) {
		this.number = number;
		this.name = name;
	}

	/**
	 * Get name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	@Override
	public int intValue() {
		return number.intValue();
	}

	@Override
	public long longValue() {
		return number.longValue();
	}

	@Override
	public float floatValue() {
		return number.floatValue();
	}

	@Override
	public double doubleValue() {
		return number.doubleValue();
	}

	@Override
	public String toString() {
		return name + ": " + number.toString();
	}

}
