package com.project.auth_service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Table(name="web_shops")
@Entity
@Getter
@Setter
public class WebShop extends Model{

    private String name;

    private String mail;

    private String password;

    private String shopId;

    private String shopSecret;

    private String successUrl;

    private String cancelUrl;

    private String returnUrl;

    @ManyToMany
    @JoinTable(
            name = "web_shop_payment_method",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private Set<PaymentMethod> paymentMethods;
}
