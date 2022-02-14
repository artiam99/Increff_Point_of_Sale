package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;
import java.util.List;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderItemDaoTest extends AbstractUnitTest {

    @Autowired
    private OrderItemDao orderItemDao;

    @Before
    public void init() throws ApiException {
        insertOrderItemPojos();
    }

    @Test
    public void testInsert() throws ApiException {
        OrderItemPojo orderItemPojo = getOrderItemPojo();
        List<OrderItemPojo> orderItemListBefore = orderItemDao.selectAll();
        orderItemDao.insert(orderItemPojo);
        List<OrderItemPojo> orderItemListAfter = orderItemDao.selectAll();

        assertEquals(orderItemListBefore.size() + 1, orderItemListAfter.size());
    }

    @Test
    public void testSelectByOrderId() {
        List<OrderItemPojo> orderItemList = orderItemDao.selectByOrderId(orderItems.get(0).getOrderId());

        assertEquals(1, orderItemList.size());
    }

    @Test
    public void testSelectAll() {
        List<OrderItemPojo> orderItemList = orderItemDao.selectAll();

        assertEquals(2,orderItemList.size());
    }

    @Test
    public void testDelete() throws ApiException {
        List<OrderItemPojo> orderItemListBefore = orderItemDao.selectAll();
        orderItemDao.deleteByOrderId(orderItems.get(0).getOrderId());
        List<OrderItemPojo> orderItemListAfter = orderItemDao.selectAll();

        assertEquals(orderItemListBefore.size() - 1, orderItemListAfter.size());
    }

    private OrderItemPojo getOrderItemPojo() {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orders.get(1).getId());
        orderItemPojo.setProductId(products.get(0).getId());
        orderItemPojo.setQuantity(20);
        orderItemPojo.setSellingPrice(1200.51);
        return orderItemPojo;
    }
}
