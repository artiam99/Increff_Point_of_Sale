package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao dao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public void add(OrderPojo p){
        dao.insert(p);
    }

    public void checkInventoryAvailability(List<OrderItemForm> o) throws ApiException {

        for(OrderItemForm i:o) {
            int orderQuantity = i.getQuantity();
            ProductPojo p = productService.getByBarcode(i.getBarcode());
            InventoryPojo iP = inventoryService.getByProductid(ConvertUtil.convertProductPojotoInventoryPojo(p));

            if(orderQuantity > iP.getQuantity()){
                throw new ApiException("Required quantity: " + orderQuantity + " of " + i.getBarcode() + " doesn't exists.");
            }
        }
    }

    @Transactional
    public void delete(int id) {
        dao.delete(id);
    }

    @Transactional
    public OrderPojo get(int id){
        return dao.select(id);
    }

    @Transactional
    public List<OrderPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id, OrderPojo p) throws ApiException {
        OrderPojo newP = getCheck(id);
        newP.setDatetime(p.getDatetime());
        newP.setInvoice(p.getInvoice());
        dao.update(newP);
    }

    @Transactional
    public OrderPojo getCheck(int id) throws ApiException {
        OrderPojo p = dao.select(id);

        if (p == null) {
            throw new ApiException("Order ID does not exit");
        }
        return p;
    }

}
