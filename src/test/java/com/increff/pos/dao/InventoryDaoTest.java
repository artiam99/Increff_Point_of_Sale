package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;
import java.util.List;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class InventoryDaoTest extends AbstractUnitTest {

    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private ProductDao productDao;

    @Before
    public void init() throws ApiException {
        insertInventoryPojos();
    }

    @Test
    public void testInsert() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = getProductPojo(brandPojo);
        productDao.insert(productPojo);
        InventoryPojo inventoryPojo = getInventoryPojo(productPojo);
        List<InventoryPojo> inventoryListBefore = inventoryDao.selectAll();
        inventoryDao.insert(inventoryPojo);
        List<InventoryPojo> inventoryListAfter = inventoryDao.selectAll();

        assertEquals(inventoryListBefore.size() + 1, inventoryListAfter.size());
        assertEquals(inventoryPojo.getProductId(), inventoryDao.select(inventoryPojo.getId()).getProductId());
        assertEquals(inventoryPojo.getQuantity(), inventoryDao.select(inventoryPojo.getId()).getQuantity());
    }

    @Test
    public void testSelect() {
        InventoryPojo inventoryPojo = inventoryDao.select(inventories.get(0).getId());

        assertEquals(inventories.get(0).getProductId(), inventoryPojo.getProductId());
        assertEquals(inventories.get(0).getQuantity(), inventoryPojo.getQuantity());
    }

    @Test
    public void testSelectProductId() {
        InventoryPojo inventoryPojo = inventoryDao.selectByProductId(inventories.get(0).getProductId());

        assertEquals(inventories.get(0).getProductId(), inventoryPojo.getProductId());
        assertEquals(inventories.get(0).getQuantity(), inventoryPojo.getQuantity());
    }

    @Test
    public void testSelectAll() {
        List<InventoryPojo> inventoryList = inventoryDao.selectAll();

        assertEquals(2,inventoryList.size());
    }

    private InventoryPojo getInventoryPojo(ProductPojo productPojo) {
        InventoryPojo invenotyPojo = new InventoryPojo();
        invenotyPojo.setProductId(productPojo.getId());
        invenotyPojo.setQuantity(new Integer(10));
        return invenotyPojo;
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
