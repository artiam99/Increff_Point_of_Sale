package com.increff.pos.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.increff.pos.model.OrderData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.increff.pos.model.OrderItemForm;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@Api
@RestController
@RequestMapping(value = "/api/order")
public class OrderApiController {

    @Autowired
    private OrderDto dto;

    @ApiOperation(value = "Create Order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody OrderItemForm[] orderItemForms) throws ApiException {

        List<OrderItemForm> orderItems = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
        dto.addOrder(orderItems);
    }

    @ApiOperation(value = "Gets a Order by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public OrderData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a Order")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody OrderItemForm[] orderItemForms) throws ApiException {

        List<OrderItemForm> orderItems = new LinkedList<OrderItemForm>(Arrays.asList(orderItemForms));
        dto.updateOrder(id, orderItems);
    }
}
