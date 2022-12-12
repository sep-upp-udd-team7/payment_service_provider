package com.project.bank1.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name="banks")
@Entity
@Getter
@Setter
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String bankUrl;

    @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Acquirer> acquirers = new HashSet<>();

}
