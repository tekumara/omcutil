package org.omancode.swing.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.NumberFormat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.omancode.util.swing.DoubleCellRenderer;

public class DoubleCellRendererTest {

	private static DoubleCellRenderer renderer10;
	private static DoubleCellRenderer renderer2;
	private static final double dbl20digits = 1.12345678911234567890;
	private static final double dbl5zeros = 0.0000012345;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		renderer10 = new DoubleCellRenderer(10);
		renderer2 = new DoubleCellRenderer(2);
	}

	@Test
	public void testFormatter() {
		NumberFormat formatter = renderer10.getFormatter();
		
		
		System.out.println("MaximumFractionDigits: " + formatter.getMaximumFractionDigits());
		System.out.println("MinimumFractionDigits: " + formatter.getMinimumFractionDigits());
		
		renderer10.setValue(dbl20digits);
		System.out.println(renderer10.getText());
		assertThat(renderer10.getText().length(), equalTo(12));
		
		renderer10.setValue(dbl5zeros);
		System.out.println(renderer10.getText());
		assertThat(renderer10.getText().length(), equalTo(12));
		
		//renderer = DecimalFormat.getInstance();
		
		renderer10.setValue(1.12E-19);
		System.out.println(renderer10.getText());
		
		renderer2.setValue(1.304);
		System.out.println(renderer2.getText());
		assertThat(renderer2.getText().length(), equalTo(4));
		
	}

}
