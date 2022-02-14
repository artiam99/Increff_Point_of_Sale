package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import java.util.List;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;
import org.springframework.beans.factory.annotation.Autowired;

public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    @Before
    public void init() throws ApiException {
        insertBrandPojos();
    }

    @Test()
    public void testAdd() throws ApiException {
        BrandForm brandForm = getBrandForm();
        List<BrandData> brandListBefore = brandDto.getAll();
        brandDto.add(brandForm);
        List<BrandData> brandListAfter = brandDto.getAll();

        assertEquals(brandListBefore.size() + 1, brandListAfter.size());
    }

    @Test()
    public void testGetBrandData() throws ApiException {
        int id = brands.get(0).getId();
        BrandData brandData = brandDto.get(id);

        assertEquals(brands.get(0).getBrand(), brandData.getBrand());
        assertEquals(brands.get(0).getCategory(), brandData.getCategory());
    }

    @Test()
    public void testGetBrandCategory() throws ApiException {
        BrandForm brandForm = getBrandForm();
        brandForm.setBrand(brands.get(0).getBrand());
        brandForm.setCategory(brands.get(0).getCategory());
        BrandPojo brandPojo = brandDto.getByBrandCategory(brandForm);

        assertEquals(brands.get(0).getBrand(), brandPojo.getBrand());
        assertEquals(brands.get(0).getCategory(), brandPojo.getCategory());
    }

    @Test()
    public void testSearchBrandData() throws ApiException {
        BrandForm brandForm = getBrandForm();
        brandForm.setBrand(brands.get(0).getBrand());
        brandForm.setCategory(brands.get(0).getCategory());
        List<BrandData> brandDataList = brandDto.search(brandForm);

        assertEquals(1, brandDataList.size());
    }

    @Test()
    public void testGetAll() throws ApiException {
        List<BrandData> brandList = brandDto.getAll();

        assertEquals(2, brandList.size());
    }

    @Test()
    public void testUpdate() throws ApiException {
        BrandForm brandForm = getBrandForm();
        int id = brands.get(0).getId();
        brandDto.update(id, brandForm);

        assertEquals(brandForm.getBrand(), brandDto.get(id).getBrand());
        assertEquals(brandForm.getCategory(), brandDto.get(id).getCategory());
    }

    private BrandForm getBrandForm() {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("park");
        brandForm.setCategory("chocolate");
        return brandForm;
    }
}