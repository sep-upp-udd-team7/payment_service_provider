package com.project.auth_service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="payment_methods")
@Getter
@Setter
public class PaymentMethod extends Model{

    private String name;

    @ManyToMany(mappedBy = "paymentMethods")
    private Set<WebShop> webShops;

}
