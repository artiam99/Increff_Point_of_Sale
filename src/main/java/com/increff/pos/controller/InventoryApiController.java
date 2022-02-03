package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.dto.InventoryDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/api/inventory")
public class InventoryApiController {

    @Autowired
    private InventoryDto dto;

    @ApiOperation(value = "Gets a Inventory by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException {

        return dto.get(id);
    }

    @ApiOperation(value = "Gets a Inventory by Barcode")
    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public List<InventoryData> getId(@RequestBody InventoryForm f) throws ApiException {

        return dto.getId(f);
    }

    @ApiOperation(value = "Gets list of all Inventories")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {

        return dto.getAll();
    }

    @ApiOperation(value = "Updates a Inventory")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {

        dto.update(id, f);
    }
}
