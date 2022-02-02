package com.increff.pos.service;


import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService { // Todo - Explore intellij

    @Autowired
    private BrandDao dao;

    @Transactional(rollbackOn = ApiException.class) // Todo - Use Spring Transaction.
    public void add(BrandPojo p) throws ApiException {
        normalize(p);

        if (StringUtil.isEmpty(p.getBrand()) || StringUtil.isEmpty(p.getCategory())) {

            throw new ApiException("Brand or category cannot be empty.");
        }

        List<BrandPojo> listP = dao.selectBrandCategory(p.getBrand(), p.getCategory());

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
    public BrandPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, BrandPojo p) throws ApiException {
        normalize(p);

        if(StringUtil.isEmpty(p.getBrand()) || StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("Brand or category cannot be empty.");
        }

        List<BrandPojo> listP = dao.selectBrandCategory(p.getBrand(), p.getCategory());

        if (listP.size() != 0) {
            throw new ApiException("This brand and category already exist.");
        }

        BrandPojo ex = getCheck(id);
        ex.setCategory(p.getCategory());
        ex.setBrand(p.getBrand());
        dao.update(ex);
    }

    @Transactional
    public List<BrandPojo> search(BrandPojo p) {
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
    public BrandPojo searchBrandCategory(BrandPojo p) throws ApiException {
        normalize(p);

        if(StringUtil.isEmpty(p.getBrand())) {
            throw new ApiException("Please enter brand.");
        }

        if(StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("Please enter Category.");
        }

        List<BrandPojo> list = dao.selectBrandCategory(p.getBrand(), p.getCategory());

        if(list.size() == 0)
        {
            throw new ApiException("This Brand and category doesn't exist.");
        }

        return list.get(0);
    }

    @Transactional
    public BrandPojo getCheck(int id) throws ApiException {
        BrandPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
        return p;
    }

    protected static void normalize(BrandPojo p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand())); // Todo - add
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
    }
}
