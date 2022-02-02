package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InventoryDto {

    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;

    public void update(int id, InventoryForm form) throws ApiException {

        ProductPojo p = productService.getByBarcode(form.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormtoInventoryPojo(form, p);

        inventoryService.update(id, inventoryPojo);
    }

    @Transactional
    public InventoryData get(int id) throws ApiException {

        InventoryPojo p = inventoryService.get(id);
        ProductPojo p1 = productService.get(p.getProductid());

        return ConvertUtil.convertInventoryPojotoInventoryData(p, p1, brandService.get(p1.getBrandcategory()));
    }

    @Transactional
    public InventoryData getId(InventoryForm form) throws ApiException {

        ProductPojo p = productService.getByBarcode(form.getBarcode());
        InventoryPojo i = ConvertUtil.convertInventoryFormtoInventoryPojo(form, p);

        InventoryPojo i2 = inventoryService.getByProductid(i);
        ProductPojo p1 = productService.get(i2.getProductid());

        return ConvertUtil.convertInventoryPojotoInventoryData(i2, p1, brandService.get(p1.getBrandcategory()));
    }

    public List<InventoryData> getAll() throws ApiException {

        List<InventoryPojo> list = inventoryService.getAll();

        List<InventoryData> list2 = new ArrayList<>();
        for(InventoryPojo p: list){

            ProductPojo p1 = productService.get(p.getProductid());

            InventoryData d = ConvertUtil.convertInventoryPojotoInventoryData(p, p1, brandService.get(p1.getBrandcategory()));
            list2.add(d);
        }

        return list2;
    }
}
