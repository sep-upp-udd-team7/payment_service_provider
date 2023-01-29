package com.project.paypal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subscribed_web_shops")
@Getter
@Setter
public class SubscribedWebShop extends Model{

    private String shopId;

    private String clientId;

    private String clientSecret;
}
