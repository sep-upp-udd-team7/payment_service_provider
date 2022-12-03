package com.project.paypal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
public class Subscription extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "description")
    private String description;

    @Column(name = "subscriber_id")
    private String subscriberId;


}
