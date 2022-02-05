package com.increff.pos.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao extends AbstractDao {

    private static String select_All = "select p from OrderPojo p";
    private static String delete_id = "delete from OrderPojo p where id=:id";
    private static String select_id = "select p from OrderPojo p where id=:id";


    @PersistenceContext
    private EntityManager ord;

    @Transactional
    public void insert(OrderPojo p) {
        ord.persist(p);
    }

    public int delete(int id) {
        Query query = ord.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public OrderPojo select(int id) {
        TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderPojo> selectAll() {

        TypedQuery<OrderPojo> query = getQuery(select_All, OrderPojo.class);
        return query.getResultList();
    }

    public void update(OrderPojo p) {
    }
}
