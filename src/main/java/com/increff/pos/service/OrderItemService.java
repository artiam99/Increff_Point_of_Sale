package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao dao;

    @Transactional
    public void add(List<OrderItemPojo> l) {
        for(OrderItemPojo p : l) {
            dao.insert(p);
        }
    }

    @Transactional
    public List<OrderItemPojo> getByOrderId(int id) throws ApiException {
        return check(id);
    }

    @Transactional
    public List<OrderItemPojo> getList(List<Integer> ids) {
        return dao.selectByOrderIdList(ids);
    }

    @Transactional
    public List<OrderItemPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional
    public void deleteByOrderId(int id) {
        dao.deleteByOrderId(id);
    }

    @Transactional
    public List<OrderItemPojo> check(int id) throws ApiException {

        List<OrderItemPojo> newP = dao.selectByOrderId(id);
        if (newP.isEmpty()) {
            throw new ApiException("Order item don't exist - orderId : " + id);
        }
        return newP;
    }
}
