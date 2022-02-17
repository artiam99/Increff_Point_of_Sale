package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import java.util.List;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    private InventoryDto inventoryDto;

    @Before
    public void init() throws ApiException {
        insertInventoryPojos();
    }

    @Test()
    public void testGetById() throws ApiException {
        int id = inventories.get(0).getId();
        InventoryData inventoryData = inventoryDto.get(id);

        assertEquals(products.get(0).getBarcode(), inventoryData.getBarcode());
        assertEquals(brands.get(0).getBrand(), inventoryData.getBrand());
        assertEquals(brands.get(0).getCategory(), inventoryData.getCategory());
        assertEquals(products.get(0).getName(), inventoryData.getName());
        assertEquals(inventories.get(0).getQuantity(), inventoryData.getQuantity(), 0.001);
    }

    @Test()
    public void testGetByBarcode() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = products.get(0);
        InventoryForm inventoryForm = getInventoryForm(productPojo, brandPojo);
        List<InventoryData> inventoryDataList = inventoryDto.getByBarcode(inventoryForm);

        assertEquals(1, inventoryDataList.size());
    }

    @Test()
    public void testGetAll() throws ApiException {
        List<InventoryData> inventoryDataList = inventoryDto.getAll();

        assertEquals(2, inventoryDataList.size());
    }

    @Test()
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = products.get(0);
        InventoryForm inventoryForm = getInventoryForm(productPojo, brandPojo);
        Integer id = inventories.get(0).getId();
        inventoryDto.update(id, inventoryForm);

        assertEquals(inventoryForm.getBarcode(), inventoryDto.get(id).getBarcode());
        assertEquals(inventoryForm.getBrand(), inventoryDto.get(id).getBrand());
        assertEquals(inventoryForm.getCategory(), inventoryDto.get(id).getCategory());
        assertEquals(inventoryForm.getName(), inventoryDto.get(id).getName());
        assertEquals(inventoryForm.getQuantity(), inventoryDto.get(id).getQuantity(), 0.001);
    }

    @Test()
    public void testSearch() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = products.get(0);
        InventoryForm inventoryForm = getInventoryForm(productPojo, brandPojo);
        List<InventoryData> inventoryDataList = inventoryDto.search(inventoryForm);

        assertEquals(1, inventoryDataList.size());

        inventoryForm.setBarcode("");
        inventoryDataList = inventoryDto.search(inventoryForm);

        assertEquals(1, inventoryDataList.size());

    }

    private InventoryForm getInventoryForm(ProductPojo productPojo, BrandPojo brandPojo) {
        InventoryForm invenotyForm = new InventoryForm();
        invenotyForm.setBarcode(productPojo.getBarcode());
        invenotyForm.setBrand(brandPojo.getBrand());
        invenotyForm.setCategory(brandPojo.getCategory());
        invenotyForm.setName(productPojo.getName());
        invenotyForm.setQuantity(new Integer(10));
        return invenotyForm;
    }
}
