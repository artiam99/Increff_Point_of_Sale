package com.increff.pos.util;

import com.increff.pos.model.*;

public class TestUtil {

    public static BrandForm getBrandFormDto(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

}
