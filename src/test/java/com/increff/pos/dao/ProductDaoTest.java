package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;
import java.util.List;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class ProductDaoTest extends AbstractUnitTest {

    @Autowired
    private ProductDao productDao;

    @Before
    public void init() throws ApiException {
        insertProductPojos();
    }

    @Test
    public void testInsert() throws ApiException {
        List<ProductPojo> productListBefore = productDao.selectAll();
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = getProductPojo(brandPojo);
        productDao.insert(productPojo);
        List<ProductPojo> productListAfter = productDao.selectAll();

        assertEquals(productListBefore.size() + 1, productListAfter.size());
        assertEquals(productPojo.getBarcode(), productDao.select(productPojo.getId()).getBarcode());
        assertEquals(productPojo.getName(), productDao.select(productPojo.getId()).getName());
        assertEquals(productPojo.getMrp(), productDao.select(productPojo.getId()).getMrp(),0.001);
        assertEquals(productPojo.getBrandcategory(), productDao.select(productPojo.getId()).getBrandcategory());
    }

    @Test
    public void testSelect() {
        ProductPojo productPojo = productDao.select(products.get(0).getId());

        assertEquals(products.get(0).getBarcode(), productPojo.getBarcode());
        assertEquals(products.get(0).getBrandcategory(), productPojo.getBrandcategory());
        assertEquals(products.get(0).getName(), productPojo.getName());
        assertEquals(products.get(0).getMrp(), productPojo.getMrp(), 0.001);
    }

    @Test
    public void testSelectBarcode() {
        ProductPojo productPojo = productDao.selectBarcode(products.get(0).getBarcode());

        assertEquals(products.get(0).getBarcode(), productPojo.getBarcode());
        assertEquals(products.get(0).getBrandcategory(), productPojo.getBrandcategory());
        assertEquals(products.get(0).getName(), productPojo.getName());
        assertEquals(products.get(0).getMrp(), productPojo.getMrp(), 0.001);
    }

    @Test
    public void testSelectBrandCategoy() {
        List<ProductPojo> productPojoList = productDao.selectByBrandCategory(products.get(0).getBrandcategory());

        for(ProductPojo p: productPojoList)
        {
            System.out.println(p.getBrandcategory());
            System.out.println(p.getName());
        }

        assertEquals(1, productPojoList.size(), 0.001);
    }

    @Test
    public void testSelectAll() {
        List<ProductPojo> productList = productDao.selectAll();

        assertEquals(2,productList.size());
    }

    private ProductPojo getProductPojo(BrandPojo b) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("1%#123");
        productPojo.setBrandcategory(b.getId());
        productPojo.setName("Maggie");
        productPojo.setMrp(new Double(100));
        return productPojo;
    }
}