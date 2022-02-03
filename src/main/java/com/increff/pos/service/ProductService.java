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
    private ProductDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException {
        normalize(p);

        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("barcode cannot be empty.");
        }

        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Product name cannot be empty.");
        }

        if(p.getMrp() <= 0) {
            throw new ApiException("MRP must by greater than zero.");
        }

        ProductPojo p1 = dao.selectBarcode(p.getBarcode());

        if(p1 != null)
        {
            throw new ApiException("This barcode already exists.");
        }

        dao.insert(p);
    }

    @Transactional
    public void delete(int id) {
        dao.delete(id);
    }

    @Transactional
    public ProductPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public ProductPojo getByBarcode(String barcode) throws ApiException {
        ProductPojo p = dao.selectBarcode(barcode);

        if (p == null) {

            throw new ApiException("Product with barcode: " + barcode + ", does not exit.");
        }

        return p;
    }

    @Transactional
    public List<ProductPojo> getByBrandCategory(int brandcategory) {

        return dao.selectBrandCategory(brandcategory);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException {

        normalize(p);

        ProductPojo ex = getCheck(id);

        if(!ex.getBarcode().equals(p.getBarcode()))
        {
            ProductPojo p1 = dao.selectBarcode(p.getBarcode());

            if(p1 != null)
            {
                throw new ApiException("This barcode already exists.");
            }
        }

        ex.setBarcode(p.getBarcode());
        ex.setBrandcategory(p.getBrandcategory());
        ex.setName(p.getName());
        ex.setMrp(p.getMrp());
        dao.update(ex);
    }

    @Transactional
    public ProductPojo getCheck(int id) throws ApiException {

        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }

        return p;
    }

    protected static void normalize(ProductPojo p) {

        p.setName(StringUtil.toLowerCase(p.getName()));
    }
}
