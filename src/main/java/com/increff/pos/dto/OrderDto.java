package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.increff.pos.model.ProductData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;


    public OrderPojo addOrder(List<OrderItemForm> orderItems) throws ApiException {

        if(orderItems.size() == 0)
        {
            throw new ApiException("No Order item is added.");
        }

        orderService.checkInventoryAvailability(orderItems);
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(StringUtil.getDateTime());
        orderService.add(orderPojo);

        List<OrderItemPojo> list = new ArrayList<>();

        for(OrderItemForm f: orderItems)
        {
            OrderItemPojo p = ConvertUtil.convertOrderItemFormtoOrderItemPojo(f);

            p.setOrderId(orderPojo.getId());

            int productId = productService.getByBarcode(f.getBarcode()).getId();

            p.setProductId(productId);

            InventoryPojo ip = new InventoryPojo();
            ip.setProductid(productId);

            ip = inventoryService.getByProductid(ip);
            ip.setQuantity(ip.getQuantity() - p.getQuantity());

            inventoryService.update(ip.getId(), ip);

            list.add(p);
        }

        orderItemService.add(list);

        return orderPojo;
    }

    public void updateOrder(int id, List<OrderItemForm> orderItems) throws ApiException {

        if(orderItems.size() == 0)
        {
            throw new ApiException("No Order item is added.");
        }

        List<OrderItemPojo> orderItemPojos = orderItemService.getAll();

        for(OrderItemPojo p: orderItemPojos)
        {
            if(p.getOrderId() == id)
            {
                InventoryPojo ip = new InventoryPojo();
                ip.setProductid(p.getProductId());

                ip = inventoryService.getByProductid(ip);
                ip.setQuantity(ip.getQuantity() + p.getQuantity());

                inventoryService.update(ip.getId(), ip);
            }
        }

        for(OrderItemForm i: orderItems) {
            int orderQuantity = i.getQuantity();
            ProductPojo p = productService.getByBarcode(i.getBarcode());
            InventoryPojo iP = inventoryService.getByProductid(ConvertUtil.convertProductPojotoInventoryPojo(p));

            if(orderQuantity > iP.getQuantity()){

                for(OrderItemPojo p1: orderItemPojos)
                {
                    if(p1.getOrderId() == id)
                    {
                        InventoryPojo ip1 = new InventoryPojo();
                        ip1.setProductid(p1.getProductId());

                        ip1 = inventoryService.getByProductid(ip1);
                        ip1.setQuantity(ip1.getQuantity() - p1.getQuantity());

                        inventoryService.update(ip1.getId(), ip1);
                    }
                }

                throw new ApiException("Required quantity: " + orderQuantity + " of " + i.getBarcode() + " doesn't exists.");
            }
        }

        orderItemService.deleteByOrderId(id);

        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(StringUtil.getDateTime());
        orderService.update(id, orderPojo);


        List<OrderItemPojo> list = new ArrayList<>();

        for(OrderItemForm f: orderItems)
        {
            OrderItemPojo p = ConvertUtil.convertOrderItemFormtoOrderItemPojo(f);

            p.setOrderId(id);

            int productId = productService.getByBarcode(f.getBarcode()).getId();

            p.setProductId(productId);

            InventoryPojo ip = new InventoryPojo();
            ip.setProductid(productId);

            ip = inventoryService.getByProductid(ip);
            ip.setQuantity(ip.getQuantity() - p.getQuantity());

            inventoryService.update(ip.getId(), ip);

            list.add(p);
        }

        orderItemService.add(list);

    }

    public OrderData get(int id) throws ApiException {
        OrderPojo orderPojo = orderService.get(id);

        return ConvertUtil.convertOrderPojotoOrderData(orderPojo, orderItemService.getByOrderId(orderPojo.getId()));
    }

    public List<OrderData> getAll() {
        List<OrderPojo> list = orderService.getAll();

        return list.stream()
                .map(o -> {
                    try {
                        return ConvertUtil.convertOrderPojotoOrderData(o, orderItemService.getByOrderId(o.getId()));
                    } catch (ApiException e) {
                        e.printStackTrace();
                        OrderData p = new OrderData();
                        return p;
                    }
                })
                .collect(Collectors.toList());
    }
}