package com.increff.pos.pojo;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"barcode"})})
public class ProductPojo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String barcode;
    @Column(nullable = false)
    private Integer brandcategory;
    private String name;
    @Column(nullable = false)
    private Double mrp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getBrandcategory() {
        return brandcategory;
    }

    public void setBrandcategory(Integer brandcategory) {
        this.brandcategory = brandcategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }
}
