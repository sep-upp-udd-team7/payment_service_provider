package com.project.crypto.model;

public class CreateOrderResponse {
    private long id;
    private String status;
    private String payment_url;

    public CreateOrderResponse(long id, String status, String payment_url) {
        this.id = id;
        this.status = status;
        this.payment_url = payment_url;
    }

    public CreateOrderResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }
}
