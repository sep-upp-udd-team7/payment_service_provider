package com.project.bank1.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name="acquirers")
@Entity
@Getter
@Setter
public class Acquirer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String shopId;

    @Column(unique = true)
    private String merchantId;

    private String merchantPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "qrCodePayment")
    private Boolean qrCodePayment = false;

    private Boolean bankPayment=false;

    @OneToMany(mappedBy = "acquirer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new HashSet<>();

    private String apiKey;
}
