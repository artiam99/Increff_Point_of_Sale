package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandMasterPojo;

@Repository
public class BrandMasterDao extends AbstractDao {

    private static String delete_id = "delete from BrandMasterPojo p where id=:id";
    private static String select_id = "select p from BrandMasterPojo p where id=:id";
    private static String select_brand_category = "select p from BrandMasterPojo p where brand=:brand and category=:category";
    private static String select_all = "select p from BrandMasterPojo p";

    @PersistenceContext
    private EntityManager brnd;

    @Transactional
    public void insert(BrandMasterPojo p) {
        brnd.persist(p);
    }

    public int delete(int id) {
        Query query = brnd.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public BrandMasterPojo select(int id) {
        TypedQuery<BrandMasterPojo> query = getQuery(select_id, BrandMasterPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public BrandMasterPojo selectBrandCategory(String brand, String category) {
        TypedQuery<BrandMasterPojo> query = getQuery(select_brand_category, BrandMasterPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public List<BrandMasterPojo> selectAll() {
        TypedQuery<BrandMasterPojo> query = getQuery(select_all, BrandMasterPojo.class);
        return query.getResultList();
    }

    public void update(BrandMasterPojo p) {
    }
}
