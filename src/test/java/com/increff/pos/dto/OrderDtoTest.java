package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import com.increff.pos.model.*;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto orderDto;
    @Autowired
    private OrderItemDto orderItemDto;

    @Before
    public void init() throws ApiException {
        insertOrderItemPojos();
    }

    @Test()
    public void testAdd() throws ApiException {
        List<OrderItemForm> orderItems = new ArrayList<>();
        for(int i = 0 ; i < 2 ; i++) {
            orderItems.add(getOrderItemForm(brands.get(1-i), products.get(1-i)));
        }
        OrderPojo orderPojo = orderDto.add(orderItems);
        List<OrderItemData> orderItemDataList = orderItemDto.get(orderPojo.getId());
        List<OrderData> orderDataList = orderDto.getAll();

        assertEquals(3, orderDataList.size());
        assertEquals(2, orderItemDataList.size());
    }

    @Test()
    public void testUpdate() throws ApiException {
        List<OrderItemForm> orderItems = new ArrayList<>();
        orderItems.add(getOrderItemForm(brands.get(0), products.get(0)));
        orderDto.update(orders.get(0).getId(), orderItems);
        List<OrderItemData> orderItemDataList = orderItemDto.get(orders.get(0).getId());

        assertEquals(orderItems.get(0).getBarcode(), orderItemDataList.get(0).getBarcode());
        assertEquals(orderItems.get(0).getBrand(), orderItemDataList.get(0).getBrand());
        assertEquals(orderItems.get(0).getName(), orderItemDataList.get(0).getName());
        assertEquals(orderItems.get(0).getQuantity(), orderItemDataList.get(0).getQuantity(), 0.001);
        assertEquals(orderItems.get(0).getSellingPrice(), orderItemDataList.get(0).getSellingPrice(), 0.001);
    }

    @Test()
    public void testGetById() throws ApiException {
        int id = orders.get(0).getId();
        OrderData orderData = orderDto.get(id);

        assertEquals(orders.get(0).getId(), orderData.getId());
        assertEquals(orders.get(0).getDatetime(), orderData.getDatetime());
        assertEquals(orders.get(0).getInvoice(), orderData.getInvoice());
    }

    @Test()
    public void testGetAll() throws ApiException {
        List<OrderData> orderDataList = orderDto.getAll();

        assertEquals(2, orderDataList.size());
    }

    @Test()
    public void testSales() throws ApiException {
        SalesForm salesForm = new SalesForm();
        salesForm.setStartDate("1950-01-01");
        salesForm.setEndDate("2022-04-04");
        salesForm.setBrand("");
        salesForm.setCategory("");
        List<SalesData> salesDataList = orderDto.sales(salesForm);

        assertEquals(2, salesDataList.size());
    }

    @Test()
    public void testGengerateInvoice() throws ApiException {
        Integer id = orders.get(0).getId();
        OrderItemForm orderItemArray[] = new OrderItemForm[1];
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode(products.get(0).getBarcode());
        orderItemForm.setBrand(brands.get(0).getBrand());
        orderItemForm.setName(products.get(0).getName());
        orderItemForm.setQuantity(1);
        orderItemForm.setSellingPrice(new Double(100));
        orderItemArray[0] = orderItemForm;
        List<BillData> billDataList = orderDto.generateInvoice(id, orderItemArray);

        assertEquals(1, billDataList.size());
    }

    private OrderItemForm getOrderItemForm(BrandPojo b, ProductPojo productPojo) throws ApiException {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode(productPojo.getBarcode());
        orderItemForm.setBrand(b.getBrand());
        orderItemForm.setName(productPojo.getName());
        orderItemForm.setQuantity(1);
        orderItemForm.setSellingPrice(new Double(100));
        return orderItemForm;
    }
}
