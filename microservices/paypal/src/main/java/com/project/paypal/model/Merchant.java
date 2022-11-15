package com.project.paypal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "merchants")
public class Merchant extends Model {

    @Column(name = "merchantId")
    private String merchantId;

    @Column(name = "clientId")
    private String clientId;

    @Column(name = "clientSecret")
    private String clientSecret;

    @OneToMany(mappedBy = "merchant",fetch = FetchType.LAZY)
    private Set<LocalTransaction> transactions;

}
