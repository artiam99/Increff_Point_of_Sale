package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import com.increff.employee.pojo.BrandMasterPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BrandMasterServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandMasterService service;

	@Test
	public void testAdd() throws ApiException {
		BrandMasterPojo p = new BrandMasterPojo();
		p.setBrand(" Nike ");
		service.add(p);
	}

	@Test
	public void testNormalize() {
		BrandMasterPojo p = new BrandMasterPojo();
		p.setBrand(" Nike ");
		BrandMasterService.normalize(p);
		assertEquals("nike", p.getBrand());
	}

}
