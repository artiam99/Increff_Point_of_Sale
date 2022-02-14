package com.increff.pos.spring;

import javax.transaction.Transactional;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnitTest {

    @Autowired
    protected BrandService brandService;
    @Autowired
    protected ProductService productService;
    @Autowired
    protected InventoryService inventoryService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected OrderItemService orderItemService;

    protected List<BrandPojo> brands;
    protected List<ProductPojo> products;
    protected List<InventoryPojo> inventories;
    protected List<OrderPojo> orders;
    protected List<OrderItemPojo> orderItems;

    protected void insertBrandPojos() throws ApiException {
        brands = new ArrayList<BrandPojo>();

        for(int i=0; i<2; i++) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand"+i);
            brandPojo.setCategory("category"+i);
            brandService.add(brandPojo);
            brands.add(brandPojo);
        }
    }

    protected void insertProductPojos() throws ApiException {
        insertBrandPojos();

        products = new ArrayList<ProductPojo>();

        for(int i=0; i<2; i++) {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("1%#12" + i);
            productPojo.setBrandcategory(brands.get(i).getId());
            productPojo.setName("productPojo"+i);
            productPojo.setMrp(50);
            productService.add(productPojo);
            products.add(productPojo);
        }
    }

    protected void insertInventoryPojos() throws ApiException {
        insertProductPojos();

        inventories = new ArrayList<InventoryPojo>();

        for(int i=0; i<2; i++) {
            InventoryPojo inventory = new InventoryPojo();
            inventory.setProductid(products.get(i).getId());
            inventory.setQuantity(20);
            inventoryService.add(inventory);
            inventories.add(inventory);
        }
    }

    protected void insertOrderPojos() throws ApiException {
        orders = new ArrayList<OrderPojo>();

        for(int i=0; i<2; i++) {
            OrderPojo orderPojo = new OrderPojo();
            orderPojo.setDatetime("01-02-2022");
            orderPojo.setInvoice(false);
            orderService.add(orderPojo);
            orders.add(orderPojo);
        }
    }

    protected void insertOrderItemPojos() throws ApiException {
        insertProductPojos();
        insertOrderPojos();
        orderItems = new ArrayList<OrderItemPojo>();

        for(int i=0; i<2; i++) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setOrderId(orders.get(i).getId());
            orderItemPojo.setProductId(products.get(i).getId());
            orderItemPojo.setQuantity(10);
            orderItemPojo.setSellingPrice(1200.51);
            orderItems.add(orderItemPojo);
        }

        orderItemService.add(orderItems);
        orderItems = orderItemService.getAll();
    }
}