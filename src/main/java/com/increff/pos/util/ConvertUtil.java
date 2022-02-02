package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;

public class ConvertUtil {
    public static BrandData convertBrandPojotoBrandData(BrandPojo p) {
        BrandData d = new BrandData();
        d.setCategory(p.getCategory());
        d.setBrand(p.getBrand());
        d.setId(p.getId());
        return d;
    }

    public static BrandPojo convertBrandFormtoBrandPojo(BrandForm f) {
        BrandPojo b = new BrandPojo();
        b.setCategory(f.getCategory());
        b.setBrand(f.getBrand());
        return b;
    }

    public static BrandForm convertProductFormtoBrandForm(ProductForm f){
        BrandForm b = new BrandForm();
        b.setBrand(f.getBrand());
        b.setCategory(f.getCategory());
        return b;
    }

    public static ProductPojo convertProductFormtoProductPojo(ProductForm f,BrandPojo b){
        ProductPojo p = new ProductPojo();
        p.setName(f.getName());
        p.setMrp(f.getMrp());
        p.setBarcode(f.getBarcode());
        p.setBrandcategory(b.getId());
        return p;
    }

    public static ProductData convertProductPojotoProductData(ProductPojo p, BrandPojo b){
        ProductData d = new ProductData();
        d.setBrand(b.getBrand());
        d.setCategory(b.getCategory());
        d.setId(p.getId());
        d.setName(p.getName());
        d.setMrp(p.getMrp());
        d.setBarcode(p.getBarcode());
        return d;
    }

    public static InventoryPojo convertInventoryFormtoInventoryPojo(InventoryForm f, ProductPojo p) {
        InventoryPojo i = new InventoryPojo();
        i.setProductid(p.getId());
        i.setQuantity(f.getQuantity());
        return i;
    }

    public static InventoryData convertInventoryPojotoInventoryData(InventoryPojo i, ProductPojo p, BrandPojo b) {

        InventoryData d = new InventoryData();
        d.setId(i.getId());
        d.setName(p.getName());
        d.setBrand(b.getBrand());
        d.setBarcode(p.getBarcode());
        d.setQuantity(i.getQuantity());
        return d;
    }
}
