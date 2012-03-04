package net.suteren.medicomp.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TemperatureFormatterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse() throws ParseException {
		TemperatureFormatter format = new TemperatureFormatter(
				Locale.getDefault());
		assertEquals((Double) 10d, format.parse("10"));
		assertEquals((Double) 36.5d, format.parse("36.5"));
		assertEquals((Double) 37.6d, format.parse("37,6"));
		assertEquals((Double) 35.6d, format.parse("35,6°C"));
		assertEquals("°C", format.getUnit());
		assertEquals((Double) 38.7d, format.parse("38.7 °C"));
		assertEquals("°C", format.getUnit());
		assertEquals((Double) 38.7d, format.parse("38.7 F"));
		assertEquals("F", format.getUnit());
	}
}
