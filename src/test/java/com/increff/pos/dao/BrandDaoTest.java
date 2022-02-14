package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandDaoTest extends AbstractUnitTest {

    @Autowired
    private BrandDao brandDao;

    @Before
    public void init() throws ApiException {
        insertBrandPojos();
    }

    @Test
    public void testInsert() {
        List<BrandPojo> brandListBefore = brandDao.selectAll();
        BrandPojo brandPojo = getBrandPojo();
        brandDao.insert(brandPojo);
        List<BrandPojo> brandListAfter = brandDao.selectAll();

        assertEquals(brandListBefore.size()+1, brandListAfter.size());
        assertEquals("nike",brandDao.select(brandPojo.getId()).getBrand());
        assertEquals("shoes",brandDao.select(brandPojo.getId()).getCategory());
    }

    @Test
    public void testSelect() {
        BrandPojo brandPojo = brandDao.select(brands.get(0).getId());

        assertEquals("brand0",brandPojo.getBrand());
        assertEquals("category0",brandPojo.getCategory());
    }

    @Test
    public void testSelectAll() {
        List<BrandPojo> brandList = brandDao.selectAll();

        assertEquals(2,brandList.size());
    }

    @Test
    public void testSelectByBrandCategory() {
        List<BrandPojo> brandList = brandDao.selectByBrandCategory(brands.get(0).getBrand(), brands.get(0).getCategory());

        assertEquals("brand0",brandList.get(0).getBrand());
        assertEquals("category0",brandList.get(0).getCategory());
    }

    @Test
    public void testSelectByBrand() {
        List<BrandPojo> brandList = brandDao.selectByBrand(brands.get(0).getBrand());

        assertEquals("brand0",brandList.get(0).getBrand());
    }

    @Test
    public void testSelectByCategory() {
        List<BrandPojo> brandList = brandDao.selectByCategory(brands.get(0).getCategory());

        assertEquals("category0",brandList.get(0).getCategory());
    }

    private BrandPojo getBrandPojo() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("nike");
        brandPojo.setCategory("shoes");
        return brandPojo;
    }
}
