package com.increff.pos.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.increff.pos.model.*;
import com.increff.pos.util.GeneratePDF;
import com.increff.pos.util.GenerateXML;
import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.increff.pos.dto.OrderDto;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


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

    @ApiOperation(value = "Generates invoice")
    @RequestMapping(value = "/invoice/{id}",method = RequestMethod.POST)
    public void generateInvoice(@PathVariable int id, @RequestBody OrderItemForm[] orderItemForms, HttpServletResponse response)
            throws ApiException, ParserConfigurationException, TransformerException, FOPException, IOException {
        List<BillData> list = dto.generateInvoice(id, orderItemForms);
        GenerateXML.createXml(list);
        byte[] encodedBytes = GeneratePDF.createPDF();
        GeneratePDF.createResponse(response, encodedBytes);
    }

    @ApiOperation(value = "Get sales data")
    @RequestMapping(path = "/sales", method = RequestMethod.POST)
    public List<SalesData> sales(@RequestBody SalesForm f) throws ApiException {
        return dto.sales(f);
    }
}
