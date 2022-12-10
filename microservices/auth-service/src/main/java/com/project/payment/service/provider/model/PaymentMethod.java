package com.project.payment.service.provider.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="payment_methods")
@Getter
@Setter
public class PaymentMethod extends Model{

    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "web_shop_id")
    private WebShop webShop;

}
