package com.increff.pos.pojo;

import javax.persistence.*;

@Entity
public class OrderPojo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String datetime;
    private boolean invoice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean getInvoice() {
        return invoice;
    }
    public void setInvoice(boolean invoice) {
        this.invoice = invoice;
    }
}
