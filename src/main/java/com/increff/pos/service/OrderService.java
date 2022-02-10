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
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public void add(OrderPojo orderPojo){
        orderDao.insert(orderPojo);
    }

    public void checkInventoryAvailability(List<OrderItemForm> orderItemFormList) throws ApiException {
        for(OrderItemForm orderItemForm:orderItemFormList) {
            int orderQuantity = orderItemForm.getQuantity();
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            InventoryPojo inventoryPojo = inventoryService.getByProductid(ConvertUtil.convertProductPojotoInventoryPojo(productPojo));
            if(orderQuantity > inventoryPojo.getQuantity()) {
                throw new ApiException("Required quantity: " + orderQuantity + " of " + orderItemForm.getBarcode() + " doesn't exists.");
            }
        }
    }

    @Transactional
    public void delete(int id) {
        orderDao.delete(id);
    }

    @Transactional
    public OrderPojo get(int id){
        return orderDao.select(id);
    }

    @Transactional
    public List<OrderPojo> getAll(){
        return orderDao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id, OrderPojo orderPojo) throws ApiException {
        OrderPojo newOrderPojo = getCheck(id);
        newOrderPojo.setDatetime(orderPojo.getDatetime());
        newOrderPojo.setInvoice(orderPojo.getInvoice());
        orderDao.update(newOrderPojo);
    }

    @Transactional
    public OrderPojo getCheck(int id) throws ApiException {
        OrderPojo orderPojo = orderDao.select(id);
        if (orderPojo == null) {
            throw new ApiException("Order ID does not exit");
        }
        return orderPojo;
    }
}
