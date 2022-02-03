package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao{

    private static String delete_id = "delete from ProductPojo p where id=:id";
    private static String select_id = "select p from ProductPojo p where id=:id";
    private static String select_brandcategory = "select p from ProductPojo p where brandcategory=:brandcategory";
    private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
    private static String select_all = "select p from ProductPojo p";

    @PersistenceContext
    private EntityManager prd;

    @Transactional
    public void insert(ProductPojo p) {
        prd.persist(p);
    }

    public int delete(int id) {
        Query query = prd.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public ProductPojo select(int id) {
        TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public ProductPojo selectBarcode(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public List<ProductPojo> selectBrandCategory(int brandcategory) {
        TypedQuery<ProductPojo> query = getQuery(select_brandcategory, ProductPojo.class);
        query.setParameter("brandcategory", brandcategory);
        return query.getResultList();
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }

    public void update(ProductPojo p) {
    }
}
