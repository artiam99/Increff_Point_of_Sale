package com.increff.employee.service;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

public class SampleTest {

	@Test
	public void testFiles() {
		InputStream is = null;
		is = SampleTest.class.getResourceAsStream("/com/increff/employee/brand.tsv");
		assertNotNull(is);
	}

}
