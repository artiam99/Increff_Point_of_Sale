package com.increff.employee.service;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandMasterDao;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.util.StringUtil;

@Service
public class BrandMasterService {

    @Autowired
    private BrandMasterDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandMasterPojo p) throws ApiException {
        normalize(p);
        if(StringUtil.isEmpty(p.getBrand())) {
            throw new ApiException("brand cannot be empty");
        }
        BrandMasterPojo p1 = dao.selectBrandCategory(p.getBrand(), p.getCategory());

        if(p1 != null)
        {
            throw new ApiException("this brand and category already exist");
        }

        dao.insert(p);
    }

    @Transactional
    public void delete(int id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandMasterPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<BrandMasterPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, BrandMasterPojo p) throws ApiException {
        normalize(p);
        BrandMasterPojo ex = getCheck(id);
        ex.setCategory(p.getCategory());
        ex.setBrand(p.getBrand());
        dao.update(ex);
    }

    @Transactional
    public BrandMasterPojo getCheck(int id) throws ApiException {
        BrandMasterPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("BrandMaster with given ID does not exit, id: " + id);
        }
        return p;
    }

    protected static void normalize(BrandMasterPojo p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand()));
    }
}
