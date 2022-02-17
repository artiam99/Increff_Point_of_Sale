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
            Integer productId = productService.getByBarcode(orderItemForm.getBarcode()).getId();
            orderItemPojo.setProductId(productId);
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setProductId(productId);
            inventoryPojo = inventoryService.getByProductId(inventoryPojo);
            inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
            inventoryService.update(inventoryPojo.getId(), inventoryPojo);
            orderItemPojoList.add(orderItemPojo);
        }
        orderItemService.add(orderItemPojoList);
        return orderPojo;
    }

    public void update(Integer id, List<OrderItemForm> orderItems) throws ApiException {
        if (orderItems.size() == 0) {
            throw new ApiException("No Order item is added.");
        }
        List<OrderItemPojo> orderItemPojos = orderItemService.getAll();
        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            if (orderItemPojo.getOrderId() == id) {
                InventoryPojo inventoryPojo = new InventoryPojo();
                inventoryPojo.setProductId(orderItemPojo.getProductId());
                inventoryPojo = inventoryService.getByProductId(inventoryPojo);
                inventoryPojo.setQuantity(inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
                inventoryService.update(inventoryPojo.getId(), inventoryPojo);
            }
        }
        for (OrderItemForm orderItemForm : orderItems) {
            Integer orderQuantity = orderItemForm.getQuantity();
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            InventoryPojo inventoryPojo = inventoryService.getByProductId(ConvertUtil.convertProductPojotoInventoryPojo(productPojo));
            if (orderQuantity > inventoryPojo.getQuantity()) {
                for (OrderItemPojo orderItemPojo : orderItemPojos) {
                    if (orderItemPojo.getOrderId() == id) {
                        InventoryPojo inventoryPojo1 = new InventoryPojo();
                        inventoryPojo1.setProductId(orderItemPojo.getProductId());
                        inventoryPojo1 = inventoryService.getByProductId(inventoryPojo1);
                        inventoryPojo1.setQuantity(inventoryPojo1.getQuantity() - orderItemPojo.getQuantity());
                        inventoryService.update(inventoryPojo1.getId(), inventoryPojo1);
                    }
                }
                throw new ApiException("Required quantity: " + orderQuantity + " of " + orderItemForm.getBarcode() + " doesn't exist.");
            }
        }
        orderItemService.deleteByOrderId(id);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItems) {
            OrderItemPojo orderItemPojo = ConvertUtil.convertOrderItemFormtoOrderItemPojo(orderItemForm);
            orderItemPojo.setOrderId(id);
            Integer productId = productService.getByBarcode(orderItemForm.getBarcode()).getId();
            orderItemPojo.setProductId(productId);
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setProductId(productId);
            inventoryPojo = inventoryService.getByProductId(inventoryPojo);
            inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
            inventoryService.update(inventoryPojo.getId(), inventoryPojo);
            orderItemPojoList.add(orderItemPojo);
        }
        orderItemService.add(orderItemPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<BillData> generateInvoice(Integer id, OrderItemForm[] orderItemForms) throws ApiException {
        List<BillData> billDataList = new ArrayList<BillData>();
        Integer newId = 1;
        for (OrderItemForm orderItemForm : orderItemForms) {
            BillData item = new BillData();
            item.setBarcode(orderItemForm.getBarcode());
            item.setBrand(orderItemForm.getBrand());
            item.setName(orderItemForm.getName());
            item.setQuantity(orderItemForm.getQuantity());
            item.setSellingPrice(orderItemForm.getSellingPrice());
            item.setId(newId);
            billDataList.add(item);
            newId++;
        }
        OrderPojo orderPojo = orderService.get(id);
        orderPojo.setInvoice(true);
        orderService.update(id, orderPojo);
        return billDataList;
    }

    public OrderData get(Integer id) throws ApiException {
        OrderPojo orderPojo = orderService.get(id);
        return ConvertUtil.convertOrderPojotoOrderData(orderPojo, orderItemService.getByOrderId(orderPojo.getId()));
    }

    public List<OrderData> getAll() {
        List<OrderPojo> list = orderService.getAll();
        return list.stream()
                .map(orderPojo -> {
                    try {
                        return ConvertUtil.convertOrderPojotoOrderData(orderPojo, orderItemService.getByOrderId(orderPojo.getId()));
                    } catch (ApiException e) {
                        e.printStackTrace();
                        OrderData orderData = new OrderData();
                        return orderData;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<SalesData> sales(SalesForm salesForm) throws ApiException {
        List<OrderPojo> orderPojoList = orderService.getAll();
        HashMap<String, Pair<Integer, Double>> map = new HashMap<>();
        for (OrderPojo orderPojo : orderPojoList) {
            String orderDate = StringUtil.trimDate(orderPojo.getDatetime());
            if (StringUtil.isAfter(salesForm.getStartDate(), orderDate) && StringUtil.isAfter(orderDate, salesForm.getEndDate())) {
                List<OrderItemData> orderItemDataList = orderItemDto.get(orderPojo.getId());
                for (OrderItemData orderItemData : orderItemDataList) {
                    String barcode = orderItemData.getBarcode();
                    Integer quantity = orderItemData.getQuantity();
                    Double revenue = orderItemData.getSellingPrice() * orderItemData.getQuantity();
                    if (map.get(barcode) == null) {
                        Pair<Integer, Double> pair = new Pair<Integer, Double>(quantity, revenue);
                        map.put(barcode, pair);
                    } else {
                        Pair<Integer, Double> pair = new Pair<Integer, Double>(map.get(barcode).getKey() + quantity, map.get(barcode).getValue() + revenue);
                        map.put(barcode, pair);
                    }
                }
            }
        }
        List<SalesData> salesDataList = new ArrayList<>();
        if (map.size() == 0) {
            return salesDataList;
        }
        for (Map.Entry mapElement : map.entrySet()) {
            String barcode = (String) mapElement.getKey();
            Pair<Integer, Double> pair = (Pair<Integer, Double>) mapElement.getValue();
            Integer quantity = pair.getKey().intValue();
            Double revenue = pair.getValue().doubleValue();
            SalesData salesData = new SalesData();
            ProductPojo productPojo = productService.getByBarcode(barcode);
            BrandPojo brandPojo = brandService.get(productPojo.getBrandcategory());
            salesData.setBarcode(barcode);
            salesData.setBrand(brandPojo.getBrand());
            salesData.setCategory(brandPojo.getCategory());
            salesData.setName(productPojo.getName());
            salesData.setQuantity(quantity);
            salesData.setRevenue(revenue);
            if ((salesForm.getBrand() == "" || salesForm.getBrand().equals(salesData.getBrand())) && (salesForm.getCategory() == "" || salesForm.getCategory().equals(salesData.getCategory()))) {
                salesDataList.add(salesData);
            }
        }
        return salesDataList;
    }

    public void checkInventoryAvailability(List<OrderItemForm> orderItemFormList) throws ApiException {
        for(OrderItemForm orderItemForm:orderItemFormList) {
            Integer orderQuantity = orderItemForm.getQuantity();
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            InventoryPojo inventoryPojo = inventoryService.getByProductId(ConvertUtil.convertProductPojotoInventoryPojo(productPojo));
            if(orderQuantity > inventoryPojo.getQuantity()) {
                throw new ApiException("Required quantity: " + orderQuantity + " of " + orderItemForm.getBarcode() + " doesn't exist.");
            }
        }
    }
}