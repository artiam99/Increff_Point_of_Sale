package com.increff.pos.dao;

import java.util.List;
import javax.persistence.TypedQuery;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao extends AbstractDao {

    private static String SELECT_ALL = "select p from OrderPojo p";
    private static String SELECT_ID = "select p from OrderPojo p where id=:id";

    public OrderPojo select(Integer id) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ID, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderPojo> selectAll() {

        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
        return query.getResultList();
    }
}
