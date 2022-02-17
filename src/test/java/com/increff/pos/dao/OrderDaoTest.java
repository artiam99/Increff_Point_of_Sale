package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;
import java.util.List;
import com.increff.pos.pojo.OrderPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderDaoTest extends AbstractUnitTest {

    @Autowired
    private OrderDao orderDao;

    @Before
    public void init() throws ApiException {
        insertOrderPojos();
    }

    @Test
    public void testInsert() throws ApiException {
        OrderPojo orderPojo = getOrderPojo();
        List<OrderPojo> orderListBefore = orderDao.selectAll();
        orderDao.insert(orderPojo);
        List<OrderPojo> orderListAfter = orderDao.selectAll();

        assertEquals(orderListBefore.size() + 1, orderListAfter.size());
        assertEquals(orderPojo.getDatetime(), orderDao.select(orderPojo.getId()).getDatetime());
        assertEquals(orderPojo.getInvoice(), orderDao.select(orderPojo.getId()).getInvoice());
    }

    @Test
    public void testSelect() {
        OrderPojo orderPojo = orderDao.select(orders.get(0).getId());

        assertEquals(orders.get(0).getDatetime(), orderPojo.getDatetime());
        assertEquals(orders.get(0).getInvoice(), orderPojo.getInvoice());
    }

    @Test
    public void testSelectAll() {
        List<OrderPojo> orderList = orderDao.selectAll();

        assertEquals(2,orderList.size());
    }

    private OrderPojo getOrderPojo() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime("03-02-2022");
        orderPojo.setInvoice(false);
        return orderPojo;
    }
}
