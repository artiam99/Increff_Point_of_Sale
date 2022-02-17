package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class InventoryDataTest extends AbstractUnitTest {

    @Test
    public void testInventoryData() {
        Integer id = 1;
        String barcode = "1%#123";
        String brand = "adidas";
        String name = "shoes";
        Integer quantity = 10;

        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(id);
        inventoryData.setBarcode(barcode);
        inventoryData.setBrand(brand);
        inventoryData.setName(name);
        inventoryData.setQuantity(quantity);

        assertEquals(id, inventoryData.getId());
        assertEquals(barcode, inventoryData.getBarcode());
        assertEquals(brand, inventoryData.getBrand());
        assertEquals(name, inventoryData.getName());
        assertEquals(quantity, inventoryData.getQuantity());
    }
}
