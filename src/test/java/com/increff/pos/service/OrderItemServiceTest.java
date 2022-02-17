package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import com.increff.pos.pojo.OrderItemPojo;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderItemServiceTest extends AbstractUnitTest {

    @Before
    public void init() throws ApiException {
        insertOrderItemPojos();
    }

    @Test()
    public void testAdd() throws ApiException {
        OrderItemPojo orderItemPojo = getOrderItemPojo();
        List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
        list.add(orderItemPojo);
        List<OrderItemPojo> orderItemListBefore = orderItemService.getAll();
        orderItemService.add(list);
        List<OrderItemPojo> orderItemListAfter = orderItemService.getAll();

        assertEquals(orderItemListBefore.size() + 1, orderItemListAfter.size());
    }

    @Test()
    public void testGet() throws ApiException {
        Integer id = orderItems.get(0).getOrderId();

        assertEquals(orderItems.get(0).getOrderId(), orderItemService.getByOrderId(id).get(0).getOrderId());
        assertEquals(orderItems.get(0).getProductId(), orderItemService.getByOrderId(id).get(0).getProductId());
        assertEquals(orderItems.get(0).getQuantity(), orderItemService.getByOrderId(id).get(0).getQuantity());
        assertEquals(orderItems.get(0).getSellingPrice(), orderItemService.getByOrderId(id).get(0).getSellingPrice(), 0.001);
    }

    @Test()
    public void testGetNotExisting() throws ApiException {
        Integer id = new Integer(500);
        try {
            orderItemService.getByOrderId(id);
            fail("Api Exception did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order item doesn't exist - orderId : " + id);
        }
    }

    @Test()
    public void testGetAll() throws ApiException {
        List<BrandPojo> brandList = brandService.getAll();
        assertEquals(2, brandList.size());
    }

    @Test()
    public void testDelete() throws ApiException {
        OrderItemPojo orderItemPojo = getOrderItemPojo();
        List<OrderItemPojo> orderItemListBefore = orderItemService.getAll();
        orderItemService.deleteByOrderId(orderItemPojo.getOrderId());
        List<OrderItemPojo> orderItemListAfter = orderItemService.getAll();

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
