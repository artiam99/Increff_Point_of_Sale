package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;

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

        List<OrderItemData> list = new ArrayList<>();

        for(OrderItemPojo p: orderItemPojoList) {

            ProductPojo pp = productService.get(p.getProductId());
            BrandPojo bp = brandService.get(pp.getBrandcategory());
            list.add(ConvertUtil.convertOrderItemPojotoOrderItemData(p, pp, bp));
        }

        return list;
    }

}