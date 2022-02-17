package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;

    public void update(Integer id, InventoryForm form) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormtoInventoryPojo(form, productPojo);
        inventoryService.update(id, inventoryPojo);
    }

    @Transactional
    public InventoryData get(Integer id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.get(id);
        ProductPojo productPojo = productService.get(inventoryPojo.getProductId());
        return ConvertUtil.convertInventoryPojotoInventoryData(inventoryPojo, productPojo, brandService.get(productPojo.getBrandcategory()));
    }

    @Transactional
    public List<InventoryData> getByBarcode(InventoryForm form) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
        InventoryPojo inventoryPojo1 = ConvertUtil.convertInventoryFormtoInventoryPojo(form, productPojo);
        InventoryPojo inventoryPojo2 = inventoryService.getByProductId(inventoryPojo1);
        ProductPojo productPojo1 = productService.get(inventoryPojo2.getProductId());
        InventoryData inventoryData = ConvertUtil.convertInventoryPojotoInventoryData(inventoryPojo2, productPojo1, brandService.get(productPojo1.getBrandcategory()));
        List<InventoryData> inventoryDataList = new ArrayList<>();
        inventoryDataList.add(inventoryData);
        return inventoryDataList;
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            ProductPojo productPojo = productService.get(inventoryPojo.getProductId());
            InventoryData inventoryData = ConvertUtil.convertInventoryPojotoInventoryData(inventoryPojo, productPojo, brandService.get(productPojo.getBrandcategory()));
            inventoryDataList.add(inventoryData);
        }
        return inventoryDataList;
    }

    public List<InventoryData> search(InventoryForm inventoryForm) throws ApiException {
        if(inventoryForm.getBarcode() != "") {
            ProductPojo productPojo = productService.getByBarcode(inventoryForm.getBarcode());
            InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormtoInventoryPojo(inventoryForm, productPojo);
            InventoryPojo inventoryPojo1 = inventoryService.getByProductId(inventoryPojo);
            List<InventoryData> inventoryDataList = new ArrayList<>();
            InventoryData inventoryData = ConvertUtil.convertProductPojotoInventoryData(productPojo, inventoryPojo1, brandService.get(productPojo.getBrandcategory()));
            if (inventoryForm.getBrand() != "" && !inventoryForm.getBrand().equals(inventoryData.getBrand())) {
                return inventoryDataList;
            }
            if (inventoryForm.getCategory() != "" && !inventoryForm.getCategory().equals(inventoryData.getCategory())) {
                return inventoryDataList;
            }
            inventoryDataList.add(inventoryData);
            return inventoryDataList;
        }
        List<BrandPojo> brandPojoList = brandService.search(ConvertUtil.convertInventoryFormtoBrandPojo(inventoryForm));
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojoList) {
            List<ProductPojo> productPojoList = productService.getByBrandCategory(brandPojo.getId());
            for (ProductPojo productPojo : productPojoList) {
                InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormtoInventoryPojo(inventoryForm, productPojo);
                InventoryPojo inventoryPojo1 = inventoryService.getByProductId(inventoryPojo);
                inventoryDataList.add(ConvertUtil.convertProductPojotoInventoryData(productPojo, inventoryPojo1, brandService.get(productPojo.getBrandcategory())));
            }
        }
        return inventoryDataList;
    }
}