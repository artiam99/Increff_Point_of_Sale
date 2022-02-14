package com.increff.pos.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo productPojo) throws ApiException {
        normalize(productPojo);
        if(StringUtil.isEmpty(productPojo.getBarcode())) {
            throw new ApiException("Barcode cannot be empty.");
        }
        if(StringUtil.isEmpty(productPojo.getName())) {
            throw new ApiException("Product name cannot be empty.");
        }
        if(productPojo.getMrp() <= 0) {
            throw new ApiException("MRP must be positive.");
        }
        ProductPojo productPojo1 = productDao.selectBarcode(productPojo.getBarcode());
        if(productPojo1 != null) {
            throw new ApiException("This barcode already exists.");
        }

        productDao.insert(productPojo);
    }

    @Transactional
    public ProductPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public ProductPojo getByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productDao.selectBarcode(barcode);
        if (productPojo == null) {

            throw new ApiException("Product with barcode: " + barcode + ", does not exit.");
        }
        return productPojo;
    }

    @Transactional
    public List<ProductPojo> getByBrandCategory(int brandcategory) {
        return productDao.selectByBrandCategory(brandcategory);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo productPojo) throws ApiException {
        normalize(productPojo);
        if(StringUtil.isEmpty(productPojo.getName())) {
            throw new ApiException("Product name cannot be empty.");
        }
        if(productPojo.getMrp() <= 0) {
            throw new ApiException("MRP must be positive.");
        }
        ProductPojo exProductPojo = getCheck(id);
        exProductPojo.setBarcode(productPojo.getBarcode());
        exProductPojo.setBrandcategory(productPojo.getBrandcategory());
        exProductPojo.setName(productPojo.getName());
        exProductPojo.setMrp(productPojo.getMrp());
        productDao.update(exProductPojo);
    }

    @Transactional
    public ProductPojo getCheck(int id) throws ApiException {
        ProductPojo productPojo = productDao.select(id);
        if (productPojo == null) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return productPojo;
    }

    protected static void normalize(ProductPojo productPojo) {
        productPojo.setName(StringUtil.toLowerCase(productPojo.getName()));
    }
}
