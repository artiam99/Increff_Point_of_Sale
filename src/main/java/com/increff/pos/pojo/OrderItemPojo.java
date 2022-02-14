package com.increff.pos.pojo;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"orderId","productId"})})
public class OrderItemPojo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int productId;
    private int orderId;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private double sellingPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
