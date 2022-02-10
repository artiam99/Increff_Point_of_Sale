package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        if (StringUtil.isEmpty(brandPojo.getBrand()) || StringUtil.isEmpty(brandPojo.getCategory())) {
            throw new ApiException("Brand or category cannot be empty.");
        }
        List<BrandPojo> brandPojoList = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if (brandPojoList.size() != 0) {
            throw new ApiException("This brand and category already exist.");
        }
        brandDao.insert(brandPojo);
    }

    @Transactional
    public void delete(int id) {
        brandDao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        if(StringUtil.isEmpty(brandPojo.getBrand()) || StringUtil.isEmpty(brandPojo.getCategory())) {
            throw new ApiException("Brand or category cannot be empty.");
        }
        List<BrandPojo> brandPojoList = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if (brandPojoList.size() != 0) {
            throw new ApiException("This brand and category already exist.");
        }
        BrandPojo brandPojo1 = getCheck(id);
        brandPojo1.setCategory(brandPojo.getCategory());
        brandPojo1.setBrand(brandPojo.getBrand());
        brandDao.update(brandPojo1);
    }

    @Transactional
    public List<BrandPojo> search(BrandPojo brandPojo) {
        normalize(brandPojo);
        if(StringUtil.isEmpty(brandPojo.getBrand()) && StringUtil.isEmpty(brandPojo.getCategory())) {
            return brandDao.selectAll();
        }
        if(StringUtil.isEmpty(brandPojo.getBrand())) {
            return brandDao.selectCategory(brandPojo.getCategory());
        }
        if(StringUtil.isEmpty(brandPojo.getCategory())) {
            return brandDao.selectBrand(brandPojo.getBrand());
        }
        return brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
    }

    @Transactional
    public BrandPojo searchBrandCategory(BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        if(StringUtil.isEmpty(brandPojo.getBrand())) {
            throw new ApiException("Please enter brand.");
        }
        if(StringUtil.isEmpty(brandPojo.getCategory())) {
            throw new ApiException("Please enter Category.");
        }
        List<BrandPojo> brandPojoList = brandDao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if(brandPojoList.size() == 0) {
            throw new ApiException("This Brand and category doesn't exist.");
        }
        return brandPojoList.get(0);
    }

    @Transactional
    public BrandPojo getCheck(int id) throws ApiException {
        BrandPojo brandPojo = brandDao.select(id);
        if (brandPojo == null) {
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
        return brandPojo;
    }

    protected static void normalize(BrandPojo brandPojo) {
        brandPojo.setBrand(StringUtil.toLowerCase(brandPojo.getBrand()));
        brandPojo.setCategory(StringUtil.toLowerCase(brandPojo.getCategory()));
    }
}
