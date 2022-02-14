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
    private BrandDto brandDto;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm brandForm) throws ApiException {
        brandDto.add(brandForm);
    }

    @ApiOperation(value = "Gets a Brand by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        return brandDto.get(id);
    }

    @ApiOperation(value = "Gets list of all Brands")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return brandDto.getAll();
    }

    @ApiOperation(value = "Updates a Brand")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm brandForm) throws ApiException {
        brandDto.update(id, brandForm);
    }

    @ApiOperation(value = "Search by Brand & Category")
    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public List<BrandData> search(@RequestBody BrandForm brandForm) throws ApiException {
        return brandDto.search(brandForm);
    }
}
