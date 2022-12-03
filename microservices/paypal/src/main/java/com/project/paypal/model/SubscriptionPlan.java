package com.project.paypal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
public class SubscriptionPlan extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private String amount;


}
