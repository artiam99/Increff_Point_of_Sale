package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo p) throws ApiException {

        dao.insert(p);
    }

    @Transactional
    public void delete(int id) {
        dao.delete(id);
    }

    @Transactional
    public InventoryPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public InventoryPojo getByProductid(InventoryPojo p) throws ApiException {

        return dao.selectProductid(p.getProductid());
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, InventoryPojo p) throws ApiException {

        InventoryPojo ex = getCheck(id);
        ex.setQuantity(p.getQuantity());

        if(ex.getQuantity() < 0) {

            throw new ApiException("Quantity can not be negative.");
        }

        dao.update(ex);
    }

    @Transactional
    public InventoryPojo getCheck(int id) {

        InventoryPojo p = dao.select(id);

        return p;
    }
}
