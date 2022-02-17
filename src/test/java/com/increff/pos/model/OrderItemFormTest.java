package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderItemFormTest extends AbstractUnitTest {

    @Test
    public void testOrderItemForm() {
        String barcode = "1%#123";
        String brand = "adidas";
        String name = "shoes";
        Integer quantity = 10;
        Double sellingPrice = 120.01;

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode(barcode);
        orderItemForm.setBrand(brand);
        orderItemForm.setName(name);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setSellingPrice(sellingPrice);

        assertEquals(barcode, orderItemForm.getBarcode());
        assertEquals(brand, orderItemForm.getBrand());
        assertEquals(name, orderItemForm.getName());
        assertEquals(quantity, orderItemForm.getQuantity());
        assertEquals(sellingPrice, orderItemForm.getSellingPrice());
    }
}
