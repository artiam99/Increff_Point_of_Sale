package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.dto.ProductDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/product")
public class ProductApiController {

    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Adds a Product")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form) throws ApiException {
        dto.add(form);
    }


    @ApiOperation(value = "Deletes a Product")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets a Product by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Products")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {

        return dto.getAll();
    }

    @ApiOperation(value = "Updates a Product")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {

        dto.update(id, f);
    }
}
