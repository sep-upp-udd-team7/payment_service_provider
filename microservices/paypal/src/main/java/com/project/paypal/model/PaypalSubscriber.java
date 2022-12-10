package com.project.paypal.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name="paypal-subscribers")
@Entity
public class PaypalSubscriber extends Model{

    private String shopId;

    private String clientSecret;

    private String clientId;
}
