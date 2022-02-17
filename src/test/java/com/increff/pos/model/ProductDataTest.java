package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class ProductDataTest extends AbstractUnitTest {

    @Test
    public void testProductData() {
        Integer id = 1;
        String barcode = "1%#123";
        String brand = "adidas";
        String category = "men";
        String name = "shoes";
        Double mrp = 120.01;

        ProductData productData = new ProductData();
        productData.setId(id);
        productData.setBarcode(barcode);
        productData.setBrand(brand);
        productData.setCategory(category);
        productData.setName(name);
        productData.setMrp(mrp);

        assertEquals(id, productData.getId());
        assertEquals(barcode, productData.getBarcode());
        assertEquals(brand, productData.getBrand());
        assertEquals(category, productData.getCategory());
        assertEquals(name, productData.getName());
        assertEquals(mrp, productData.getMrp());
    }
}
