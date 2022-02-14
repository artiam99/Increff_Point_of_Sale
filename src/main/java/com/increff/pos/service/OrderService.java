package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Transactional
    public void add(OrderPojo orderPojo){
        orderDao.insert(orderPojo);
    }

    @Transactional
    public OrderPojo get(int id){
        return orderDao.select(id);
    }

    @Transactional
    public List<OrderPojo> getAll(){
        return orderDao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id, OrderPojo orderPojo) throws ApiException {
        OrderPojo newOrderPojo = getCheck(id);
        newOrderPojo.setDatetime(orderPojo.getDatetime());
        newOrderPojo.setInvoice(orderPojo.getInvoice());
        orderDao.update(newOrderPojo);
    }

    @Transactional
    public OrderPojo getCheck(int id) throws ApiException {
        OrderPojo orderPojo = orderDao.select(id);
        if (orderPojo == null) {
            throw new ApiException("Order ID does not exit.");
        }
        return orderPojo;
    }
}
