package com.project.crypto.dto;

public class OrderResponse {

    private long id;
    private String order_id;
    private String status;
    private double price_amount;

    public OrderResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice_amount() {
        return price_amount;
    }

    public void setPrice_amount(double price_amount) {
        this.price_amount = price_amount;
    }
}
