package com.increff.pos.dao;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao extends AbstractDao {

    private static String SELECT_ALL = "select p from OrderItemPojo p";
    private static String SELECT_BY_ORDER_ID = "select p from OrderItemPojo p where orderId=:orderId";
    private static String DELETE_BY_ORDER_ID = "delete from OrderItemPojo p where orderId=:orderId";

    public List<OrderItemPojo> selectByOrderId(int orderId){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER_ID,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }

    public List<OrderItemPojo> selectAll() {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL, OrderItemPojo.class);
        return query.getResultList();
    }

    public int deleteByOrderId(int orderId) {
        Query query = em().createQuery(DELETE_BY_ORDER_ID);
        query.setParameter("orderId", orderId);
        return query.executeUpdate();
    }
}
