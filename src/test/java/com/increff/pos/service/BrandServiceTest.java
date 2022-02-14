package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandServiceTest extends AbstractUnitTest {

    @Before
    public void init() throws ApiException {
        insertBrandPojos();
    }

    // Test Add
    @Test()
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = getBrandPojo();
        List<BrandPojo> brandListBefore = brandService.getAll();
        brandService.add(brandPojo);
        List<BrandPojo> brandListAfter = brandService.getAll();

        assertEquals(brandListBefore.size() + 1, brandListAfter.size());
        assertEquals(brandPojo.getBrand(), brandService.get(brandPojo.getId()).getBrand());
        assertEquals(brandPojo.getCategory(), brandService.get(brandPojo.getId()).getCategory());
    }

    @Test()
    public void testAddDuplicate() throws ApiException {
        BrandPojo brandPojo = getBrandPojo();
        brandService.add(brandPojo);
        try {
            brandService.add(brandPojo);
            fail("Api Exception did not occur.");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "This brand and category already exists.");
        }
    }

    @Test()
    public void testAddEmpty() throws ApiException {
        BrandPojo brandPojo = getEmptyBrandPojo();
        try {
            brandService.add(brandPojo);
            fail("Api Exception did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand or category cannot be empty.");
        }
    }

    // Test Get
    @Test()
    public void testGet() throws ApiException {
        int id = brands.get(0).getId();
        brandService.get(id);

        assertEquals(brands.get(0).getBrand(), brandService.get(id).getBrand());
        assertEquals(brands.get(0).getCategory(), brandService.get(id).getCategory());
    }

    @Test()
    public void testGetNotExisting() throws ApiException {
        int id = 500;
        try {
            brandService.get(id);
            fail("Api Exception did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand with given ID does not exit, id: " + id);
        }
    }

    @Test()
    public void testGetAll() throws ApiException {
        List<BrandPojo> brandList = brandService.getAll();
        assertEquals(2, brandList.size());
    }

    @Test()
    public void testCheckIfExists() throws ApiException {
        int id = brands.get(0).getId();
        BrandPojo brandPojo = brandService.getCheck(id);

        assertEquals(brands.get(0).getBrand(), brandPojo.getBrand());
        assertEquals(brands.get(0).getCategory(), brandPojo.getCategory());
    }

    // Test Update
    @Test()
    public void testUpdate() throws ApiException {
        BrandPojo newBrandPojo = getBrandPojo();
        int id = brands.get(0).getId();
        brandService.update(id, newBrandPojo);

        assertEquals(newBrandPojo.getBrand(), brandService.get(id).getBrand());
        assertEquals(newBrandPojo.getCategory(), brandService.get(id).getCategory());
    }

    @Test()
    public void testUpdateDuplicate() throws ApiException {
        BrandPojo brandPojo = getBrandPojo();
        brandService.add(brandPojo);
        int id = brands.get(0).getId();
        try {
            brandService.update(id, brandPojo);
            fail("Api Exception did not occur.");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "This brand and category already exists.");
        }
    }

    @Test()
    public void testUpdateEmpty() throws ApiException {
        BrandPojo newBrandPojo = getEmptyBrandPojo();
        int id = brands.get(0).getId();
        try {
            brandService.update(id, newBrandPojo);
            fail("Api Exception did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand or category cannot be empty.");
        }
    }

    // Test Search
    @Test()
    public void testSearchByBrandCategory() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        List<BrandPojo> brandPojoList = brandService.search(brandPojo);

        assertEquals(brandPojoList.get(0).getBrand(), brandPojo.getBrand());
        assertEquals(brandPojoList.get(0).getCategory(), brandPojo.getCategory());

        brandPojo.setBrand("");
        brandPojoList = brandService.search(brandPojo);
        assertEquals(brandPojoList.get(0).getCategory(), brandPojo.getCategory());

        brandPojo.setBrand(brands.get(0).getBrand());
        brandPojo.setCategory("");
        brandPojoList = brandService.search(brandPojo);
        assertEquals(brandPojoList.get(0).getBrand(), brandPojo.getBrand());

        brandPojo.setBrand("");
        brandPojo.setCategory("");
        brandPojoList = brandService.search(brandPojo);
        assertEquals(2, brandPojoList.size());
    }

    @Test()
    public void testSearchBrandCategory() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        BrandPojo newBrandPojo = brandService.searchByBrandCategory(brandPojo);

        assertEquals(newBrandPojo.getBrand(), brandPojo.getBrand());
        assertEquals(newBrandPojo.getCategory(), brandPojo.getCategory());
    }

    @Test
    public void testNormalize() throws ApiException {
        BrandPojo brandPojo = getBrandPojo();
        brandService.normalize(brandPojo);

        assertEquals("park", brandPojo.getBrand());
        assertEquals("chocolate", brandPojo.getCategory());
    }

    private BrandPojo getBrandPojo() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("park");
        brandPojo.setCategory("chocolate");
        return brandPojo;
    }

    private BrandPojo getEmptyBrandPojo() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("");
        brandPojo.setCategory("");
        return brandPojo;
    }
}