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
    public List<InventoryData> getId(InventoryForm form) throws ApiException {

        ProductPojo p = productService.getByBarcode(form.getBarcode());
        InventoryPojo i = ConvertUtil.convertInventoryFormtoInventoryPojo(form, p);

        InventoryPojo i2 = inventoryService.getByProductid(i);
        ProductPojo p1 = productService.get(i2.getProductid());

        InventoryData d = ConvertUtil.convertInventoryPojotoInventoryData(i2, p1, brandService.get(p1.getBrandcategory()));

        List<InventoryData> list = new ArrayList<>();

        list.add(d);

        return list;
    }

    public List<InventoryData> getAll() throws ApiException {

        List<InventoryPojo> list = inventoryService.getAll();

        List<InventoryData> list2 = new ArrayList<>();
        for (InventoryPojo p : list) {

            ProductPojo p1 = productService.get(p.getProductid());

            InventoryData d = ConvertUtil.convertInventoryPojotoInventoryData(p, p1, brandService.get(p1.getBrandcategory()));
            list2.add(d);
        }

        return list2;
    }

    public List<InventoryData> searchInventoryData(InventoryForm f) throws ApiException {

        if (f.getBarcode() != "") {
            ProductPojo p = productService.getByBarcode(f.getBarcode());
            InventoryPojo i = ConvertUtil.convertInventoryFormtoInventoryPojo(f, p);
            InventoryPojo ip = inventoryService.getByProductid(i);

            List<InventoryData> list = new ArrayList<>();

            InventoryData id = ConvertUtil.convertProductPojotoInventoryData(p, ip, brandService.get(p.getBrandcategory()));

            if (f.getBrand() != "" && !f.getBrand().equals(id.getBrand())) {
                return list;
            }

            if (f.getCategory() != "" && !f.getCategory().equals(id.getCategory())) {
                return list;
            }

            list.add(id);

            return list;
        }

        List<BrandPojo> brandPojo = brandService.search(ConvertUtil.convertInventoryFormtoBrandPojo(f));

        List<InventoryData> list2 = new ArrayList<>();

        for (BrandPojo p : brandPojo) {
            List<ProductPojo> list1 = productService.getByBrandCategory(p.getId());

            for (ProductPojo p1 : list1) {
                InventoryPojo i = ConvertUtil.convertInventoryFormtoInventoryPojo(f, p1);
                InventoryPojo ip = inventoryService.getByProductid(i);
                list2.add(ConvertUtil.convertProductPojotoInventoryData(p1, ip, brandService.get(p1.getBrandcategory())));
            }
        }

        return list2;
    }
}
