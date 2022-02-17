package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandFormTest extends AbstractUnitTest {

    @Test
    public void testBrandForm() {
        String brand = "adidas";
        String category = "men";

        BrandForm brandFrom = new BrandForm();
        brandFrom.setBrand(brand);
        brandFrom.setCategory(category);

        assertEquals(brand, brandFrom.getBrand());
        assertEquals(category, brandFrom.getCategory());
    }
}
