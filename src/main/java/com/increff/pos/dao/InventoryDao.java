package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import com.increff.pos.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {

    private static String SELECT_ID = "select p from InventoryPojo p where id=:id";
    private static String SELECT_PRODUCT_ID = "select p from InventoryPojo p where productid=:productid";
    private static String SELECT_ALL = "select p from InventoryPojo p";

    public InventoryPojo select(Integer id) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ID, InventoryPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public InventoryPojo selectByProductId(Integer productid) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_PRODUCT_ID, InventoryPojo.class);
        query.setParameter("productid", productid);
        return getSingle(query);
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
        return query.getResultList();
    }
}