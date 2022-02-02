package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@Api
@RestController
@RequestMapping(value = "/api/brand")
public class BrandApiController {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm f) throws ApiException {
        dto.addBrand(f);
    }


    @ApiOperation(value = "Deletes a Brand")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        dto.deleteBrand(id);
    }

    @ApiOperation(value = "Gets a Brand by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        return dto.getBrandData(id);
    }

    @ApiOperation(value = "Gets list of all Brands")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return dto.getAllBrand();
    }

    @ApiOperation(value = "Updates a Brand")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
        dto.updateBrand(id, f);
    }

    @ApiOperation(value = "Search by Brand & Category")
    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public List<BrandData> search(@RequestBody BrandForm f) throws ApiException {
        return dto.searchBrandData(f);
    }
}
