package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class SalesFormTest extends AbstractUnitTest {

    @Test
    public void testSalesForm() {
        String startDate = "2022-01-01";
        String endDate = "2022-02-02";
        String brand = "adidas";
        String category = "men";

        SalesForm salesForm = new SalesForm();
        salesForm.setStartDate(startDate);
        salesForm.setEndDate(endDate);
        salesForm.setBrand(brand);
        salesForm.setCategory(category);

        assertEquals(startDate, salesForm.getStartDate());
        assertEquals(endDate, salesForm.getEndDate());
        assertEquals(brand, salesForm.getBrand());
        assertEquals(category, salesForm.getCategory());
    }
}
