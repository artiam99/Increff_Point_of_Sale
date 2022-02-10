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
    private InventoryDao inventoryDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo inventoryPojo) throws ApiException {
        inventoryDao.insert(inventoryPojo);
    }

    @Transactional
    public void delete(int id) {
        inventoryDao.delete(id);
    }

    @Transactional
    public InventoryPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public InventoryPojo getByProductid(InventoryPojo inventoryPojo) throws ApiException {
        return inventoryDao.selectProductid(inventoryPojo.getProductid());
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo inventoryPojo1 = getCheck(id);
        inventoryPojo1.setQuantity(inventoryPojo.getQuantity());
        if(inventoryPojo1.getQuantity() < 0) {
            throw new ApiException("Quantity can not be negative.");
        }
        inventoryDao.update(inventoryPojo1);
    }

    @Transactional
    public InventoryPojo getCheck(int id) {
        InventoryPojo inventoryPojo = inventoryDao.select(id);
        return inventoryPojo;
    }
}
