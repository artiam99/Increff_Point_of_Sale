package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import java.util.List;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderItemDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderItemDto orderItemDto;

    @Before
    public void init() throws ApiException {
        insertOrderItemPojos();
    }

    @Test()
    public void testGetByOrderId() throws ApiException {
        int id = orders.get(0).getId();
        List<OrderItemData> orderItemDataList = orderItemDto.get(id);

        assertEquals(1, orderItemDataList.size());
    }
}
