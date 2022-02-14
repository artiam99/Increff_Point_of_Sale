package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import java.util.List;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private ProductDto productDto;

    @Before
    public void init() throws ApiException {
        insertProductPojos();
    }

    @Test()
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductForm productForm = getProductForm(brandPojo);
        List<ProductData> productListBefore = productDto.getAll();
        productDto.add(productForm);
        List<ProductData> productListAfter = productDto.getAll();

        assertEquals(productListBefore.size() + 1, productListAfter.size());
    }

    @Test()
    public void testGetProductData() throws ApiException {
        int id = products.get(0).getId();
        ProductData productData = productDto.get(id);

        assertEquals(products.get(0).getBarcode(), productData.getBarcode());
        assertEquals(brands.get(0).getBrand(), productData.getBrand());
        assertEquals(brands.get(0).getCategory(), productData.getCategory());
        assertEquals(products.get(0).getName(), productData.getName());
        assertEquals(products.get(0).getMrp(), productData.getMrp(), 0.001);
    }

    @Test()
    public void testGetAll() throws ApiException {
        List<ProductData> productList = productDto.getAll();

        assertEquals(2, productList.size());
    }

    @Test()
    public void testSearchProductData() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductForm productForm = getProductForm(brandPojo);
        productForm.setBarcode(products.get(0).getBarcode());
        List<ProductData> productList = productDto.search(productForm);

        assertEquals(1, productList.size());

        productForm.setBarcode("");
        productList = productDto.search(productForm);

        assertEquals(1, productList.size());
    }

    @Test()
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = brands.get(0);
        ProductForm productForm = getProductForm(brandPojo);
        productForm.setBarcode(products.get(0).getBarcode());
        int id = products.get(0).getId();
        productDto.update(id, productForm);

        assertEquals(productForm.getBarcode(), productDto.get(id).getBarcode());
        assertEquals(productForm.getBrand(), productDto.get(id).getBrand());
        assertEquals(productForm.getCategory(), productDto.get(id).getCategory());
        assertEquals(productForm.getName(), productDto.get(id).getName());
        assertEquals(productForm.getMrp(), productDto.get(id).getMrp(), 0.001);
    }

    private ProductForm getProductForm(BrandPojo b) throws ApiException {
        ProductForm productForm = new ProductForm();
        productForm.setBarcode("1%#123");
        productForm.setBrand(b.getBrand());
        productForm.setCategory(b.getCategory());
        productForm.setName("maggie");
        productForm.setMrp(100);
        return productForm;
    }
}
