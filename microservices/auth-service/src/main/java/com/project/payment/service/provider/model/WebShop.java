package com.project.payment.service.provider.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Table(name="web_shops")
@Entity
public class WebShop extends Model{

    private String name;

    private String mail;

    private String password;

    private String shopId;

    @OneToMany(mappedBy="webShop")
    private Set<PaymentMethod> paymentMethods;
}
