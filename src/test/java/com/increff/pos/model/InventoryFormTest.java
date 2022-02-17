package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class InventoryFormTest extends AbstractUnitTest {

    @Test
    public void testInventoryForm() {
        String barcode = "1%#123";
        String brand = "adidas";
        String name = "shoes";
        Integer quantity = 10;

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setBrand(brand);
        inventoryForm.setName(name);
        inventoryForm.setQuantity(quantity);

        assertEquals(barcode, inventoryForm.getBarcode());
        assertEquals(brand, inventoryForm.getBrand());
        assertEquals(name, inventoryForm.getName());
        assertEquals(quantity, inventoryForm.getQuantity());
    }
}
