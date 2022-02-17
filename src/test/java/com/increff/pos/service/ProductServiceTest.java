package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class ProductServiceTest extends AbstractUnitTest {

    @Before
    public void init() throws ApiException {
        insertProductPojos();
    }

    @Test()
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = getProductPojo(brandPojo);
        List<ProductPojo> productListBefore = productService.getAll();
        productService.add(productPojo);
        List<ProductPojo> productListAfter = productService.getAll();

        assertEquals(productListBefore.size() + 1, productListAfter.size());
        assertEquals(productPojo.getBarcode(), productService.get(productPojo.getId()).getBarcode());
        assertEquals(productPojo.getName(), productService.get(productPojo.getId()).getName());
        assertEquals(productPojo.getMrp(), productService.get(productPojo.getId()).getMrp(),0.001);
        assertEquals(productPojo.getBrandcategory(), productService.get(productPojo.getId()).getBrandcategory());
    }

    @Test()
    public void testAddDuplicate() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = getProductPojo(brandPojo);
        productPojo.setBarcode("1%#121");

        try {
            productService.add(productPojo);
            fail("Api Exception did not occur.");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "This barcode already exists.");
        }
    }

    @Test
    public  void testAddEmpty() throws  ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductPojo productPojo = getProductPojo(brandPojo);
        productPojo.setBarcode("");
        try {
            productService.add(productPojo);
            fail("Api Exception did not occur.");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Barcode cannot be empty.");
        }

        productPojo = getProductPojo(brandPojo);
        productPojo.setName("");
        try {
            productService.add(productPojo);
            fail("Api Exception did not occur.");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product name cannot be empty.");
        }

        productPojo = getProductPojo(brandPojo);
        productPojo.setMrp(new Double(-1.0));
        try {
            productService.add(productPojo);
            fail("Api Exception did not occur.");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "MRP must be positive.");
        }
    }

    @Test()
    public void testGetById() throws ApiException {
        ProductPojo productPojo = productService.get(products.get(0).getId());

        assertEquals(products.get(0).getBarcode(), productPojo.getBarcode());
        assertEquals(products.get(0).getBrandcategory(), productPojo.getBrandcategory());
        assertEquals(products.get(0).getMrp(), productPojo.getMrp(), 0.001);
        assertEquals(products.get(0).getName(), productPojo.getName());
    }

    @Test()
    public void testGetByBarcode() throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(products.get(0).getBarcode());
        assertEquals(products.get(0).getBarcode(), productPojo.getBarcode());
        assertEquals(products.get(0).getBrandcategory(), productPojo.getBrandcategory());
        assertEquals(products.get(0).getMrp(), productPojo.getMrp(), 0.001);
        assertEquals(products.get(0).getName(), productPojo.getName());
    }

    @Test()
    public void testGetByBarcodeNotExisting() throws ApiException {
        try {
            productService.getByBarcode("abcdefgh");
            fail("ApiException did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product with barcode: abcdefgh, does not exit.");
        }
    }

    @Test
    public void testGetAll() {
        List<ProductPojo> product_list = productService.getAll();
        assertEquals(2, product_list.size());
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo = getProductPojo(brands.get(1));
        productService.update(products.get(0).getId(), productPojo);

        assertEquals(productPojo.getBrandcategory(), productService.get(products.get(0).getId()).getBrandcategory());
        assertEquals(productPojo.getName(), productService.get(products.get(0).getId()).getName());
        assertEquals(productPojo.getMrp(), productService.get(products.get(0).getId()).getMrp(), 0.001);
    }

    @Test()
    public void testUpdateWrong() throws ApiException {
        ProductPojo productPojo = getWrongProductPojo(brands.get(1));
        try {
            productService.update(products.get(0).getId(), productPojo);
            fail("ApiException did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product name cannot be empty.");
        }

        productPojo = getWrongProductPojo(brands.get(1));
        productPojo.setName("maggie");
        try {
            productService.update(products.get(0).getId(), productPojo);
            fail("ApiException did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "MRP must be positive.");
        }
    }

    @Test()
    public void testGetNotExisting() throws ApiException {
        Integer id = new Integer(500);
        try {
            productService.get(id);
            fail("Api Exception did not occur");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product with given ID does not exit, id: " + id);
        }
    }

    @Test
    public void testNormalize() throws ApiException {
        ProductPojo productPojo = getProductPojo(brands.get(0));

        productService.normalize(productPojo);
        assertEquals("maggie", productPojo.getName());
    }

    private ProductPojo getProductPojo(BrandPojo b) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("1%#123");
        productPojo.setBrandcategory(b.getId());
        productPojo.setName("Maggie");
        productPojo.setMrp(new Double(100));
        return productPojo;
    }
    private ProductPojo getWrongProductPojo(BrandPojo b) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("1%#123");
        productPojo.setBrandcategory(b.getId());
        productPojo.setName("");
        productPojo.setMrp(new Double(-5.0));
        return productPojo;
    }
}
