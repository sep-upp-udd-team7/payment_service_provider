package com.project.crypto.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "merchants")
public class Merchant extends Model {

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "token")
    private String token;

    public Merchant() {
    }

    @OneToMany(mappedBy = "merchant",fetch = FetchType.LAZY)
    private Set<CryptoOrder> orders;
}
