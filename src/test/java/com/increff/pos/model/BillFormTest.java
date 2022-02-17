package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class BillFormTest extends AbstractUnitTest {

    @Test
    public void testBillForm() {
        String barcode = "1%#123";
        String brand = "adidas";
        String name = "shoes";
        Integer quantity = 10;
        Double sellingPrice = 120.01;

        BillForm billForm = new BillForm();
        billForm.setBarcode(barcode);
        billForm.setBrand(brand);
        billForm.setName(name);
        billForm.setQuantity(quantity);
        billForm.setSellingPrice(sellingPrice);

        assertEquals(barcode, billForm.getBarcode());
        assertEquals(brand, billForm.getBrand());
        assertEquals(name, billForm.getName());
        assertEquals(quantity, billForm.getQuantity());
        assertEquals(sellingPrice, billForm.getSellingPrice());
    }
}
