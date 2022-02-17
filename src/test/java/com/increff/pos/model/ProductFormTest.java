package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class ProductFormTest extends AbstractUnitTest {

    @Test
    public void testProductForm() {
        String barcode = "1%#123";
        String brand = "adidas";
        String category = "men";
        String name = "shoes";
        Double mrp = 120.01;

        ProductForm productForm = new ProductForm();
        productForm.setBarcode(barcode);
        productForm.setBrand(brand);
        productForm.setCategory(category);
        productForm.setName(name);
        productForm.setMrp(mrp);

        assertEquals(barcode, productForm.getBarcode());
        assertEquals(brand, productForm.getBrand());
        assertEquals(category, productForm.getCategory());
        assertEquals(name, productForm.getName());
        assertEquals(mrp, productForm.getMrp());
    }
}
