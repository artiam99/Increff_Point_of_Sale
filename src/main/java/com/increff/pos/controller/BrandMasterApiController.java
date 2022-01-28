package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandMasterData;
import com.increff.pos.model.BrandMasterForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandMasterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@Api
@RestController
public class BrandMasterApiController {

    @Autowired
    private BrandMasterService service;

    @ApiOperation(value = "Adds a Brand Master")
    @RequestMapping(path = "/api/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandMasterForm form) throws ApiException {
        BrandMasterPojo p = convert(form);
        service.add(p);
    }


    @ApiOperation(value = "Deletes a Brand Master")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
    // /api/1
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @ApiOperation(value = "Gets a Brand Master by ID")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandMasterData get(@PathVariable int id) throws ApiException {
        BrandMasterPojo p = service.get(id);
        return convert(p);
    }

    @ApiOperation(value = "Gets list of all Brand Masters")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandMasterData> getAll() {
        List<BrandMasterPojo> list = service.getAll();
        List<BrandMasterData> list2 = new ArrayList<BrandMasterData>();
        for (BrandMasterPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Updates a Brand Master")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandMasterForm f) throws ApiException {
        BrandMasterPojo p = convert(f);
        service.update(id, p);
    }


    private static BrandMasterData convert(BrandMasterPojo p) {
        BrandMasterData d = new BrandMasterData();
        d.setCategory(p.getCategory());
        d.setBrand(p.getBrand());
        d.setId(p.getId());
        return d;
    }

    private static BrandMasterPojo convert(BrandMasterForm f) {
        BrandMasterPojo p = new BrandMasterPojo();
        p.setCategory(f.getCategory());
        p.setBrand(f.getBrand());
        return p;
    }
}
