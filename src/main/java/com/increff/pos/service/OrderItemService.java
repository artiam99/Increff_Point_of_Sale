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
    private OrderItemDao orderItemDao;

    @Transactional
    public void add(List<OrderItemPojo> orderItemPojoList) {
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemDao.insert(orderItemPojo);
        }
    }

    @Transactional
    public List<OrderItemPojo> getByOrderId(int id) throws ApiException {
        return check(id);
    }

    @Transactional
    public List<OrderItemPojo> getList(List<Integer> ids) {
        return orderItemDao.selectByOrderIdList(ids);
    }

    @Transactional
    public List<OrderItemPojo> getAll() {
        return orderItemDao.selectAll();
    }

    @Transactional
    public void deleteByOrderId(int id) {
        orderItemDao.deleteByOrderId(id);
    }

    @Transactional
    public List<OrderItemPojo> check(int id) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(id);
        if (orderItemPojoList.isEmpty()) {
            throw new ApiException("Order item don't exist - orderId : " + id);
        }
        return orderItemPojoList;
    }
}
