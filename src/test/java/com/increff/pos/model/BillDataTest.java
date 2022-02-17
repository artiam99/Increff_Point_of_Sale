package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class BillDataTest extends AbstractUnitTest {

    @Test
    public void testBillData() {
        Integer id = 1;
        String barcode = "1%#123";
        String brand = "adidas";
        String name = "shoes";
        Integer quantity = 10;
        Double sellingPrice = 120.01;

        BillData billData = new BillData();
        billData.setId(id);
        billData.setBarcode(barcode);
        billData.setBrand(brand);
        billData.setName(name);
        billData.setQuantity(quantity);
        billData.setSellingPrice(sellingPrice);

        assertEquals(id, billData.getId());
        assertEquals(barcode, billData.getBarcode());
        assertEquals(brand, billData.getBrand());
        assertEquals(name, billData.getName());
        assertEquals(quantity, billData.getQuantity());
        assertEquals(sellingPrice, billData.getSellingPrice());
    }
}
