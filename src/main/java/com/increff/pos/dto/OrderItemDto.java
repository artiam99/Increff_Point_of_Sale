package com.increff.pos.dto;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public List<OrderItemData> get(int orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            BrandPojo brandPojo = brandService.get(productPojo.getBrandcategory());
            orderItemDataList.add(ConvertUtil.convertOrderItemPojotoOrderItemData(orderItemPojo, productPojo, brandPojo));
        }
        return orderItemDataList;
    }
}