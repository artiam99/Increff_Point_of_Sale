package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao{

    private static String SELECT_ID = "select p from ProductPojo p where id=:id";
    private static String SELECT_BRAND_CATEGORY = "select p from ProductPojo p where brandcategory=:brandcategory";
    private static String SELECT_BARCODE = "select p from ProductPojo p where barcode=:barcode";
    private static String SELECT_ALL = "select p from ProductPojo p";

    public ProductPojo select(int id) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public ProductPojo selectBarcode(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public List<ProductPojo> selectByBrandCategory(int brandcategory) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BRAND_CATEGORY, ProductPojo.class);
        query.setParameter("brandcategory", brandcategory);
        return query.getResultList();
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
        return query.getResultList();
    }
}
