package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

    private static String delete_id = "delete from BrandPojo p where id=:id";
    private static String select_id = "select p from BrandPojo p where id=:id";
    private static String select_brand_category = "select p from BrandPojo p where brand=:brand and category=:category";
    private static String select_brand = "select p from BrandPojo p where brand=:brand";
    private static String select_category = "select p from BrandPojo p where category=:category";
    private static String select_all = "select p from BrandPojo p";


    @Transactional
    public void insert(BrandPojo brandPojo) { em().persist(brandPojo);
    }

    public int delete(int id) {
        Query query = em().createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public BrandPojo select(int id) {
        TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<BrandPojo> selectBrandCategory(String brand, String category) {
        TypedQuery<BrandPojo> query = getQuery(select_brand_category, BrandPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return query.getResultList();
    }

    public List<BrandPojo> selectBrand(String brand) {
        TypedQuery<BrandPojo> query = getQuery(select_brand, BrandPojo.class);
        query.setParameter("brand", brand);
        return query.getResultList();
    }

    public List<BrandPojo> selectCategory(String category) {
        TypedQuery<BrandPojo> query = getQuery(select_category, BrandPojo.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
        return query.getResultList();
    }

    public void update(BrandPojo brandPojo) {
    }
}
