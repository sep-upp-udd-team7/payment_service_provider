package com.project.crypto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "crypto_orders")
public class CryptoOrder extends Model{

    private long coingate_id;
    private String orderId;
    private double priceAmount;
    private String priceCurrency;
    private String title;

    @Enumerated(EnumType.STRING)
    private CryptoOrderStatus status;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    public CryptoOrder() {
    }

    public CryptoOrder(String orderId, long coingate_id, double priceAmount, String priceCurrency, String title, CryptoOrderStatus status, Merchant merchant) {
        this.orderId = orderId;
        this.coingate_id = coingate_id;
        this.priceAmount = priceAmount;
        this.priceCurrency = priceCurrency;
        this.title = title;
        this.status = status;
        this.merchant = merchant;
    }
}
