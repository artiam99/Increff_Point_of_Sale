package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {

    @Autowired
    private BrandService service;

    public void addBrand(BrandForm f) throws ApiException {
        BrandPojo p = ConvertUtil.convertBrandFormtoBrandPojo(f);
        service.add(p);
    }
    public List<BrandData> searchBrandData(BrandForm f)  {
        BrandPojo p = ConvertUtil.convertBrandFormtoBrandPojo(f);

        List<BrandPojo> list = service.search(p);
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p1 : list) {
            list2.add(ConvertUtil.convertBrandPojotoBrandData(p1));
        }
        return list2;
    }

    public BrandPojo getByBrandCategory(BrandForm f) throws ApiException {
        BrandPojo p = ConvertUtil.convertBrandFormtoBrandPojo(f);
        return service.searchBrandCategory(p);
    }

    public BrandData getBrandData(int id) throws ApiException {
        return ConvertUtil.convertBrandPojotoBrandData(service.get(id));
    }

    public List<BrandData> getAllBrand(){
        List<BrandPojo> list = service.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list2.add(ConvertUtil.convertBrandPojotoBrandData(p));
        }
        return list2;
    }

    public void updateBrand(int id,BrandForm f) throws ApiException {
        BrandPojo p = ConvertUtil.convertBrandFormtoBrandPojo(f);
        service.update(id, p);
    }

    public void deleteBrand(int id) {
        service.delete(id);
    }
}
