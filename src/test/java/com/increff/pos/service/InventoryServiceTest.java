package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class InventoryServiceTest extends AbstractUnitTest {

    @Before
    public void init() throws ApiException {
        insertInventoryPojos();
    }

    @Test()
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = getProductPojo(brandPojo);
        productService.add(productPojo);
        InventoryPojo inventoryPojo = getInventoryPojo(productPojo);
        List<InventoryPojo> inventoryListBefore = inventoryService.getAll();
        inventoryService.add(inventoryPojo);
        List<InventoryPojo> inventoryListAfter = inventoryService.getAll();

        assertEquals(inventoryListBefore.size() + 1, inventoryListAfter.size());
        assertEquals(inventoryPojo.getProductId(), inventoryService.get(inventoryPojo.getId()).getProductId());
        assertEquals(inventoryPojo.getQuantity(), inventoryService.get(inventoryPojo.getId()).getQuantity());
    }

    @Test()
    public void testGet() throws ApiException {
        Integer id = inventories.get(0).getId();
        InventoryPojo inventoryPojo = inventoryService.get(id);

        assertEquals(inventories.get(0).getProductId(), inventoryPojo.getProductId());
        assertEquals(inventories.get(0).getQuantity(), inventoryPojo.getQuantity());
    }

    @Test()
    public void testGetAll() throws ApiException {
        List<InventoryPojo> inventoryList = inventoryService.getAll();

        assertEquals(2, inventoryList.size());
    }

    @Test()
    public void testByProductId() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(products.get(0).getId());
        inventoryPojo = inventoryService.getByProductId(inventoryPojo);

        assertEquals(inventoryPojo.getProductId(), inventoryService.get(inventoryPojo.getId()).getProductId());
        assertEquals(inventoryPojo.getQuantity(), inventoryService.get(inventoryPojo.getId()).getQuantity());
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = getProductPojo(brandPojo);
        productService.add(productPojo);
        InventoryPojo inventoryPojo = getInventoryPojo(productPojo);
        inventoryService.update(inventories.get(0).getId(), inventoryPojo);

        assertEquals(inventoryPojo.getQuantity(), inventoryService.get(inventories.get(0).getId()).getQuantity(), 0.001);
    }

    @Test
    public void testWrongUpdate() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = getProductPojo(brandPojo);
        productService.add(productPojo);
        InventoryPojo inventoryPojo = getInventoryPojo(productPojo);
        inventoryPojo.setQuantity(new Integer(-10));

        try {
            inventoryService.update(inventories.get(0).getId(), inventoryPojo);
            fail("ApiException did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Quantity can not be negative.");
        }
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
