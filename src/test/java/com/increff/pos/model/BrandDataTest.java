package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandDataTest extends AbstractUnitTest {

    @Test
    public void testBrandData() {
        Integer id = 1;
        String brand = "adidas";
        String category = "men";

        BrandData brandData = new BrandData();
        brandData.setId(id);
        brandData.setBrand(brand);
        brandData.setCategory(category);

        assertEquals(id, brandData.getId());
        assertEquals(brand, brandData.getBrand());
        assertEquals(category, brandData.getCategory());
    }
}
