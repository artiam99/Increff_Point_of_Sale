package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderDataTest extends AbstractUnitTest {

    @Test
    public void testOrderData() {
        Integer id = 1;
        String datetime = "2022-01-01";
        Double billAmount = 100.9;
        boolean invoice = false;

        OrderData orderData = new OrderData();
        orderData.setId(id);
        orderData.setDatetime(datetime);
        orderData.setBillAmount(billAmount);
        orderData.setInvoice(invoice);

        assertEquals(id, orderData.getId());
        assertEquals(datetime, orderData.getDatetime());
        assertEquals(billAmount, orderData.getBillAmount());
        assertEquals(invoice, orderData.getInvoice());
    }
}
