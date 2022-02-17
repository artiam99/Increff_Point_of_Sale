package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderItemDataTest extends AbstractUnitTest {

    @Test
    public void testOrderItemData() {
        Integer id = 1;
        String barcode = "1%#123";
        String brand = "adidas";
        String name = "shoes";
        Integer quantity = 10;
        Double sellingPrice = 120.01;

        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setId(id);
        orderItemData.setBarcode(barcode);
        orderItemData.setBrand(brand);
        orderItemData.setName(name);
        orderItemData.setQuantity(quantity);
        orderItemData.setSellingPrice(sellingPrice);

        assertEquals(id, orderItemData.getId());
        assertEquals(barcode, orderItemData.getBarcode());
        assertEquals(brand, orderItemData.getBrand());
        assertEquals(name, orderItemData.getName());
        assertEquals(quantity, orderItemData.getQuantity());
        assertEquals(sellingPrice, orderItemData.getSellingPrice());
    }
}
