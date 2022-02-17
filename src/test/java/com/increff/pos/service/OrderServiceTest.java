package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import com.increff.pos.pojo.OrderPojo;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderServiceTest extends AbstractUnitTest {

    @Before
    public void init() throws ApiException {
        insertOrderPojos();
    }

    @Test()
    public void testAdd() throws ApiException {
        OrderPojo orderPojo = getOrderPojo();
        List<OrderPojo> orderListBefore = orderService.getAll();
        orderService.add(orderPojo);
        List<OrderPojo> orderListAfter = orderService.getAll();

        assertEquals(orderListBefore.size() + 1, orderListAfter.size());
        assertEquals(orderPojo.getDatetime(), orderService.get(orderPojo.getId()).getDatetime());
        assertEquals(orderPojo.getInvoice(), orderService.get(orderPojo.getId()).getInvoice());
    }

    @Test()
    public void testGet() throws ApiException {
        OrderPojo orderPojo = orderService.get(orders.get(0).getId());

        assertEquals(orders.get(0).getDatetime(), orderPojo.getDatetime());
        assertEquals(orders.get(0).getInvoice(), orderPojo.getInvoice());
    }

    @Test()
    public void testGetAll() throws ApiException {
        List<OrderPojo> orderList = orderService.getAll();
        assertEquals(2, orderList.size());
    }

    @Test()
    public void testGetNotExisting() throws ApiException {
        Integer id = new Integer(500);
        try {
            orderService.update(id, new OrderPojo());
            fail("Api Exception did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order ID does not exit.");
        }
    }

    @Test()
    public void testUpdate() throws ApiException {
        OrderPojo orderPojo = getOrderPojo();
        Integer id = orders.get(0).getId();
        orderService.update(id, orderPojo);

        assertEquals(orderPojo.getDatetime(), orderService.get(id).getDatetime());
        assertEquals(orderPojo.getInvoice(), orderService.get(id).getInvoice());
    }

    private OrderPojo getOrderPojo() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime("03-02-2022");
        orderPojo.setInvoice(false);
        return orderPojo;
    }
}
