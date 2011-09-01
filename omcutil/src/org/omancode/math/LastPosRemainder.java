package org.omancode.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a series of doubles, in specified positions, and calculates the
 * remainder when only one unfilled position remains (the "last position").
 * 
 * @author Oliver Mannion
 * @version $Revision$
 */
public class LastPosRemainder {

	/**
	 * Total all positions will sum to. Used to calculate remainder when there
	 * is one position unfilled.
	 */
	private final double total;

	private double remainder = Double.NaN;

	/**
	 * Number of positions.
	 */
	private final int numPos;

	private List<Integer> unfilledPositions;

	private double[] values;

	/**
	 * Construct a {@link LastPosRemainder}.
	 * 
	 * @param numPos
	 *            number of positions, must be greater than 1.
	 * @param total
	 *            total that all values in all positions must sum to.
	 */
	public LastPosRemainder(int numPos, double total) {
		if (numPos < 2) {
			throw new IllegalArgumentException(
					"numPos must be greater than 1");
		}
		this.numPos = numPos;
		this.total = total;

		unfilledPositions = zeroBasedSequence(numPos);
		values = new double[numPos];
	}

	private List<Integer> zeroBasedSequence(int to) {
		List<Integer> sequence = new ArrayList<Integer>(to);
		for (int i = 0; i < to; i++) {
			sequence.add(i);
		}
		return sequence;
	}

	/**
	 * Fill a position with a value.
	 * 
	 * @param pos
	 *            position to fill
	 * @param value
	 *            value to fill
	 * @return returns the position of the last position to be filled. If more
	 *         than 1 position remains to be filled, then returns -1.
	 */
	public int fill(int pos, double value) {

		values[pos] = value;

		unfilledPositions.remove(Integer.valueOf(pos));

		if (unfilledPositions.size() > 1) {
			remainder = Double.NaN;
			return -1;
		}

		// get and remove last unfill position
		int lastPos = unfilledPositions.remove(0);

		// update last unedited value
		double currentSumOfValues = 0;

		for (int i = 0; i < values.length; i++) {
			if (i != lastPos) {
				currentSumOfValues += values[i];
			}
		}

		remainder = total - currentSumOfValues;

		/*
		 * System.err.println(String.format("last pos %d remainder %f", lastPos,
		 * remainder));
		 */

		// reset
		unfilledPositions = zeroBasedSequence(numPos);

		return lastPos;
	}

	/**
	 * Remainder for the last position. NaN if no last position (i.e: more than
	 * 1 unfilled position still exists), otherwise the remainder value for the
	 * last position.
	 * 
	 * @return remainder
	 */
	public double getRemainder() {
		return remainder;
	}

}
