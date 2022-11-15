package com.project.paypal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class LocalTransaction extends Model {

    @Column(name = "transactionId", unique = true)
    private String transactionId;

    @Column(name = "payerId")
    private String payerId;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
}
