package com.increff.pos.service;


import com.increff.pos.dao.BrandMasterDao;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandMasterService { // Todo - Explore intellij

    @Autowired
    private BrandMasterDao dao;

    @Transactional(rollbackOn = ApiException.class) // Todo - Use Spring Transaction.
    public void add(BrandMasterPojo p) throws ApiException {
        normalize(p);
        if (StringUtil.isEmpty(p.getBrand()) || StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("Brand or category cannot be empty.");
        }

        List<BrandMasterPojo> listP = dao.selectBrandCategory(p.getBrand(), p.getCategory());

        if (listP.size() != 0) {
            throw new ApiException("This brand and category already exist.");
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

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, BrandMasterPojo p) throws ApiException {
        normalize(p);

        if(StringUtil.isEmpty(p.getBrand()) || StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("Brand or category cannot be empty.");
        }

        List<BrandMasterPojo> listP = dao.selectBrandCategory(p.getBrand(), p.getCategory());

        if (listP.size() != 0) {
            throw new ApiException("This brand and category already exist.");
        }


        BrandMasterPojo ex = getCheck(id);
        ex.setCategory(p.getCategory());
        ex.setBrand(p.getBrand());
        dao.update(ex);
    }

    @Transactional
    public List<BrandMasterPojo> search(BrandMasterPojo p) {
        normalize(p);

        if(StringUtil.isEmpty(p.getBrand()) && StringUtil.isEmpty(p.getCategory())) {
            return dao.selectAll();
        }

        if(StringUtil.isEmpty(p.getBrand())) {
            return dao.selectCategory(p.getCategory());
        }

        if(StringUtil.isEmpty(p.getCategory())) {
            return dao.selectBrand(p.getBrand());
        }

        return dao.selectBrandCategory(p.getBrand(), p.getCategory());
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
        p.setBrand(StringUtil.toLowerCase(p.getBrand())); // Todo - add
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
    }
}
