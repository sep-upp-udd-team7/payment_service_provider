package com.project.bank1.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name="acquirers")
@Entity
@Getter
@Setter
public class Acquirer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String merchantId;

    private String merchantPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id")
    private Bank bank;
}
