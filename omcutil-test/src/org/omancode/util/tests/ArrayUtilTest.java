package org.omancode.util.tests;

import static org.junit.Assert.*;


import org.junit.BeforeClass;
import org.junit.Test;
import org.omancode.util.ArrayUtil;

public class ArrayUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testTranspose() {
		Object[][] array =
				new Object[][] { new Object[] { 0, 1, 2, 3, 4 },
						new Object[] { 5, 6, 7, 8, 9 },
						new Object[] { 10, 11, 12, 13, 14 } };

		Object[][] arrayT =
			new Object[][] { new Object[] { 0, 5, 10},
					new Object[] { 1, 6, 11},
					new Object[] { 2, 7, 12}, 
					new Object[] { 3, 8, 13}, 
					new Object[] { 4, 9, 14}};
		
		assertArrayEquals(arrayT, ArrayUtil.transpose(array));
		assertArrayEquals(array, ArrayUtil.transpose(arrayT));
		

	}
	
}