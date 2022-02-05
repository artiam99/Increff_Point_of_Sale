package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;


@Component
public class ProductDto {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private InventoryService inventoryService;


    @Transactional(rollbackFor = ApiException.class)
    public void add(ProductForm f) throws ApiException {

        BrandForm brandForm = ConvertUtil.convertProductFormtoBrandForm(f);
        BrandPojo b = brandDto.getByBrandCategory(brandForm);
        ProductPojo p = ConvertUtil.convertProductFormtoProductPojo(f, b);

        productService.add(p);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(0);
        inventoryPojo.setProductid(p.getId());
        inventoryService.add(inventoryPojo);
    }

    public void delete(int id) {
        productService.delete(id);
    }

    @Transactional
    public ProductData get(int id) throws ApiException {

        ProductPojo p = productService.get(id);
        BrandPojo b = brandService.get(p.getBrandcategory());

        return ConvertUtil.convertProductPojotoProductData(p, b);
    }

    @Transactional
    public List<ProductData> getAll() throws ApiException{
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<>();
        for(ProductPojo p: list){

            ProductData d = ConvertUtil.convertProductPojotoProductData(p, brandService.get(p.getBrandcategory()));
            list2.add(d);
        }

        return list2;
    }

    public void update(int id, ProductForm f) throws ApiException {

        BrandForm brandForm = ConvertUtil.convertProductFormtoBrandForm(f);
        BrandPojo b = brandDto.getByBrandCategory(brandForm);
        ProductPojo p = ConvertUtil.convertProductFormtoProductPojo(f,b);

        productService.update(id, p);
    }

    public List<ProductData> searchProductData(ProductForm f) throws ApiException  {

        if(f.getBarcode() != "")
        {
            ProductPojo p = productService.getByBarcode(f.getBarcode());

            List<ProductData> list = new ArrayList<>();

            ProductData pd = ConvertUtil.convertProductPojotoProductData(p, brandService.get(p.getBrandcategory()));

            if(f.getBrand() != "" && !f.getBrand().equals(pd.getBrand()))
            {
                return list;
            }

            if(f.getCategory() != "" && !f.getCategory().equals(pd.getCategory()))
            {
                return list;
            }

            list.add(pd);

            return list;
        }

        List<BrandPojo> brandPojo = brandService.search(ConvertUtil.convertProductFormtoBrandPojo(f));

        List<ProductData> list2 = new ArrayList<>();

        for(BrandPojo p: brandPojo)
        {
            List<ProductPojo> list1 = productService.getByBrandCategory(p.getId());

            for(ProductPojo p1: list1)
            {
                list2.add(ConvertUtil.convertProductPojotoProductData(p1, brandService.get(p1.getBrandcategory())));
            }
        }

        return list2;
    }
}
