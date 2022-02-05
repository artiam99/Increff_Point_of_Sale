package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao extends AbstractDao {

    private static String selectAll = "select p from OrderItemPojo p";
    private static String selectByOrderId = "select p from OrderItemPojo p where orderId=:orderId";
    private static String deleteByOrderId = "delete from OrderItemPojo p where orderId=:orderId";
    private static String selectByOrderIdList = "select p from OrderItemPojo p where orderId IN:orderIds";

    @PersistenceContext
    private EntityManager ordIt;

    @Transactional
    public void insert(OrderItemPojo p) {
        ordIt.persist(p);
    }

    public List<OrderItemPojo> selectByOrderId(int orderId){
        TypedQuery<OrderItemPojo> query = getQuery(selectByOrderId,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }

    public List<OrderItemPojo> selectAll() {
        TypedQuery<OrderItemPojo> query = getQuery(selectAll, OrderItemPojo.class);
        return query.getResultList();
    }

    public int deleteByOrderId(int orderId) {
        Query query = ordIt.createQuery(deleteByOrderId);
        query.setParameter("orderId", orderId);
        return query.executeUpdate();
    }

    public List<OrderItemPojo> selectByOrderIdList(List<Integer> orderIds){
        TypedQuery<OrderItemPojo> query = getQuery(selectByOrderIdList,OrderItemPojo.class);
        query.setParameter("orderIds" , orderIds);
        return query.getResultList();
    }

    public void update(OrderItemPojo p) {
    }
}
