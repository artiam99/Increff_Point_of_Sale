package com.increff.pos.model;

public class ProductForm {

    private String barcode;
    private int brandcategory;
    private String name;
    private double mrp;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getBrandcategory() {
        return brandcategory;
    }

    public void setBrandcategory(int brandcategory) {
        this.brandcategory = brandcategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }
}
