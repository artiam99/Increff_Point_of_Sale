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
    public InventoryPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public InventoryPojo getByProductId(InventoryPojo inventoryPojo) throws ApiException {
        return inventoryDao.selectByProductId(inventoryPojo.getProductId());
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo inventoryPojo1 = getCheck(id);
        inventoryPojo1.setQuantity(inventoryPojo.getQuantity());
        if(inventoryPojo1.getQuantity() < 0) {
            throw new ApiException("Quantity can not be negative.");
        }
        inventoryDao.update(inventoryPojo1);
    }

    @Transactional
    public InventoryPojo getCheck(Integer id) {
        InventoryPojo inventoryPojo = inventoryDao.select(id);
        return inventoryPojo;
    }
}
