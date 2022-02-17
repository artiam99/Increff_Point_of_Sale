package com.increff.pos.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.increff.pos.spring.AbstractUnitTest;

public class MessageDataTest extends AbstractUnitTest {

    @Test
    public void testInventoryData() {
        String message = "Hello";

        MessageData inventoryData = new MessageData();
        inventoryData.setMessage(message);

        assertEquals(message, inventoryData.getMessage());
    }
}