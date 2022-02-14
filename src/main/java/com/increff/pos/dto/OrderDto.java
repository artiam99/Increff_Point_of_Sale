package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemDto orderItemDto;

    public OrderPojo add(List<OrderItemForm> orderItems) throws ApiException {
        if (orderItems.size() == 0) {
            throw new ApiException("No Order item is added.");
        }
        checkInventoryAvailability(orderItems);
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(StringUtil.getDateTime());
        orderPojo.setInvoice(false);
        orderService.add(orderPojo);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItems) {
            OrderItemPojo orderItemPojo = ConvertUtil.convertOrderItemFormtoOrderItemPojo(orderItemForm);
            orderItemPojo.setOrderId(orderPojo.getId());
            int productId = productService.getByBarcode(orderItemForm.getBarcode()).getId();
            orderItemPojo.setProductId(productId);
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setProductid(productId);
            inventoryPojo = inventoryService.getByProductId(inventoryPojo);
            inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
            inventoryService.update(inventoryPojo.getId(), inventoryPojo);
            orderItemPojoList.add(orderItemPojo);
        }
        orderItemService.add(orderItemPojoList);
        return orderPojo;
    }

    public void update(int id, List<OrderItemForm> orderItems) throws ApiException {
        if (orderItems.size() == 0) {
            throw new ApiException("No Order item is added.");
        }
        List<OrderItemPojo> orderItemPojos = orderItemService.getAll();
        for (OrderItemPojo p : orderItemPojos) {
            if (p.getOrderId() == id) {
                InventoryPojo ip = new InventoryPojo();
                ip.setProductid(p.getProductId());
                ip = inventoryService.getByProductId(ip);
                ip.setQuantity(ip.getQuantity() + p.getQuantity());
                inventoryService.update(ip.getId(), ip);
            }
        }
        for (OrderItemForm i : orderItems) {
            int orderQuantity = i.getQuantity();
            ProductPojo p = productService.getByBarcode(i.getBarcode());
            InventoryPojo iP = inventoryService.getByProductId(ConvertUtil.convertProductPojotoInventoryPojo(p));
            if (orderQuantity > iP.getQuantity()) {
                for (OrderItemPojo p1 : orderItemPojos) {
                    if (p1.getOrderId() == id) {
                        InventoryPojo ip1 = new InventoryPojo();
                        ip1.setProductid(p1.getProductId());
                        ip1 = inventoryService.getByProductId(ip1);
                        ip1.setQuantity(ip1.getQuantity() - p1.getQuantity());
                        inventoryService.update(ip1.getId(), ip1);
                    }
                }
                throw new ApiException("Required quantity: " + orderQuantity + " of " + i.getBarcode() + " doesn't exist.");
            }
        }
        orderItemService.deleteByOrderId(id);
        List<OrderItemPojo> list = new ArrayList<>();
        for (OrderItemForm f : orderItems) {
            OrderItemPojo p = ConvertUtil.convertOrderItemFormtoOrderItemPojo(f);
            p.setOrderId(id);
            int productId = productService.getByBarcode(f.getBarcode()).getId();
            p.setProductId(productId);
            InventoryPojo ip = new InventoryPojo();
            ip.setProductid(productId);
            ip = inventoryService.getByProductId(ip);
            ip.setQuantity(ip.getQuantity() - p.getQuantity());
            inventoryService.update(ip.getId(), ip);
            list.add(p);
        }
        orderItemService.add(list);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<BillData> generateInvoice(int id, OrderItemForm[] orderItemForms) throws ApiException {
        List<BillData> reqBill = new ArrayList<BillData>();
        int newId = 1;
        for (OrderItemForm p : orderItemForms) {
            BillData item = new BillData();
            item.setBarcode(p.getBarcode());
            item.setBrand(p.getBrand());
            item.setName(p.getName());
            item.setQuantity(p.getQuantity());
            item.setSellingPrice(p.getSellingPrice());
            item.setId(newId);
            reqBill.add(item);
            newId++;
        }
        OrderPojo orderPojo = orderService.get(id);
        orderPojo.setInvoice(true);
        orderService.update(id, orderPojo);

        return reqBill;
    }

    public OrderData get(int id) throws ApiException {
        OrderPojo orderPojo = orderService.get(id);
        return ConvertUtil.convertOrderPojotoOrderData(orderPojo, orderItemService.getByOrderId(orderPojo.getId()));
    }

    public List<OrderData> getAll() {
        List<OrderPojo> list = orderService.getAll();
        return list.stream()
                .map(o -> {
                    try {
                        return ConvertUtil.convertOrderPojotoOrderData(o, orderItemService.getByOrderId(o.getId()));
                    } catch (ApiException e) {
                        e.printStackTrace();
                        OrderData p = new OrderData();
                        return p;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<SalesData> sales(SalesForm f) throws ApiException {
        List<OrderPojo> list = orderService.getAll();
        HashMap<String, Pair<Integer, Double>> hmap = new HashMap<>();
        for (OrderPojo p : list) {
            String orderDate = StringUtil.trimDate(p.getDatetime());
            if (StringUtil.isAfter(f.getStartDate(), orderDate) && StringUtil.isAfter(orderDate, f.getEndDate())) {
                List<OrderItemData> list2 = orderItemDto.get(p.getId());
                for (OrderItemData d : list2) {
                    String barcode = d.getBarcode();
                    int quantity = d.getQuantity();
                    double revenue = d.getSellingPrice() * d.getQuantity();
                    Integer ob1 = new Integer(quantity);
                    Double ob2 = new Double(revenue);
                    if (hmap.get(barcode) == null) {
                        Pair<Integer, Double> pair = new Pair<Integer, Double>(ob1, ob2);
                        hmap.put(barcode, pair);
                    } else {
                        Pair<Integer, Double> pair = new Pair<Integer, Double>(hmap.get(barcode).getKey() + ob1, hmap.get(barcode).getValue() + ob2);
                        hmap.put(barcode, pair);
                    }
                }
            }
        }
        List<SalesData> list3 = new ArrayList<>();
        if (hmap.size() == 0) {
            return list3;
        }
        for (Map.Entry mapElement : hmap.entrySet()) {
            String barcode = (String) mapElement.getKey();
            Pair<Integer, Double> pair = (Pair<Integer, Double>) mapElement.getValue();
            int quantity = pair.getKey().intValue();
            double revenue = pair.getValue().doubleValue();
            SalesData sd = new SalesData();
            ProductPojo pp = productService.getByBarcode(barcode);
            BrandPojo bp = brandService.get(pp.getBrandcategory());
            sd.setBarcode(barcode);
            sd.setBrand(bp.getBrand());
            sd.setCategory(bp.getCategory());
            sd.setName(pp.getName());
            sd.setQuantity(quantity);
            sd.setRevenue(revenue);
            if ((f.getBrand() == "" || f.getBrand().equals(sd.getBrand())) && (f.getCategory() == "" || f.getCategory().equals(sd.getCategory()))) {
                list3.add(sd);
            }
        }
        return list3;
    }

    public void checkInventoryAvailability(List<OrderItemForm> orderItemFormList) throws ApiException {
        for(OrderItemForm orderItemForm:orderItemFormList) {
            int orderQuantity = orderItemForm.getQuantity();
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            InventoryPojo inventoryPojo = inventoryService.getByProductId(ConvertUtil.convertProductPojotoInventoryPojo(productPojo));
            if(orderQuantity > inventoryPojo.getQuantity()) {
                throw new ApiException("Required quantity: " + orderQuantity + " of " + orderItemForm.getBarcode() + " doesn't exist.");
            }
        }
    }
}