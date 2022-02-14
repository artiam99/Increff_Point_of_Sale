package com.increff.pos.dto;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
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
    public void add(ProductForm productForm) throws ApiException {
        BrandForm brandForm = ConvertUtil.convertProductFormtoBrandForm(productForm);
        BrandPojo brandPojo = brandDto.getByBrandCategory(brandForm);
        ProductPojo productPojo = ConvertUtil.convertProductFormtoProductPojo(productForm, brandPojo);
        productService.add(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(0);
        inventoryPojo.setProductid(productPojo.getId());
        inventoryService.add(inventoryPojo);
    }


    @Transactional
    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        BrandPojo brandPojo = brandService.get(productPojo.getBrandcategory());
        return ConvertUtil.convertProductPojotoProductData(productPojo, brandPojo);
    }

    @Transactional
    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            ProductData productData = ConvertUtil.convertProductPojotoProductData(productPojo, brandService.get(productPojo.getBrandcategory()));
            productDataList.add(productData);
        }
        return productDataList;
    }

    public void update(int id, ProductForm productForm) throws ApiException {
        BrandForm brandForm = ConvertUtil.convertProductFormtoBrandForm(productForm);
        BrandPojo brandPojo = brandDto.getByBrandCategory(brandForm);
        ProductPojo productPojo = ConvertUtil.convertProductFormtoProductPojo(productForm, brandPojo);
        productService.update(id, productPojo);
    }

    public List<ProductData> search(ProductForm productForm) throws ApiException {
        if (productForm.getBarcode() != "") {
            ProductPojo productPojo = productService.getByBarcode(productForm.getBarcode());
            List<ProductData> productDataList = new ArrayList<>();
            ProductData productData = ConvertUtil.convertProductPojotoProductData(productPojo, brandService.get(productPojo.getBrandcategory()));
            if (productForm.getBrand() != "" && !productForm.getBrand().equals(productData.getBrand())) {
                return productDataList;
            }
            if (productForm.getCategory() != "" && !productForm.getCategory().equals(productData.getCategory())) {
                return productDataList;
            }
            productDataList.add(productData);
            return productDataList;
        }
        List<BrandPojo> brandPojoList = brandService.search(ConvertUtil.convertProductFormtoBrandPojo(productForm));
        List<ProductData> productDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojoList) {
            List<ProductPojo> productPojoList = productService.getByBrandCategory(brandPojo.getId());
            for (ProductPojo productPojo : productPojoList) {
                productDataList.add(ConvertUtil.convertProductPojotoProductData(productPojo, brandService.get(productPojo.getBrandcategory())));
            }
        }
        return productDataList;
    }
}