package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class SalesDataTest extends AbstractUnitTest {

    @Test
    public void testSalesData() {
        String barcode = "1%#123";
        String brand = "adidas";
        String category = "men";
        String name = "shoes";
        Integer quantity = 10;
        Double revenue = 120.01;

        SalesData salesData = new SalesData();
        salesData.setBarcode(barcode);
        salesData.setBrand(brand);
        salesData.setCategory(category);
        salesData.setName(name);
        salesData.setQuantity(quantity);
        salesData.setRevenue(revenue);

        assertEquals(barcode, salesData.getBarcode());
        assertEquals(brand, salesData.getBrand());
        assertEquals(category, salesData.getCategory());
        assertEquals(name, salesData.getName());
        assertEquals(quantity, salesData.getQuantity());
        assertEquals(revenue, salesData.getRevenue());
    }
}
