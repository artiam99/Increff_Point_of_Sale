package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.increff.pos.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

    private static String SELECT_ID = "select p from BrandPojo p where id=:id";
    private static String SELECT_BRAND_CATEGORY = "select p from BrandPojo p where brand=:brand and category=:category";
    private static String SELECT_BRAND = "select p from BrandPojo p where brand=:brand";
    private static String SELECT_CATEGORY = "select p from BrandPojo p where category=:category";
    private static String SELECT_ALL = "select p from BrandPojo p";

    public BrandPojo select(Integer id) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<BrandPojo> selectByBrandCategory(String brand, String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return query.getResultList();
    }

    public List<BrandPojo> selectByBrand(String brand) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND, BrandPojo.class);
        query.setParameter("brand", brand);
        return query.getResultList();
    }

    public List<BrandPojo> selectByCategory(String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_CATEGORY, BrandPojo.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }
}