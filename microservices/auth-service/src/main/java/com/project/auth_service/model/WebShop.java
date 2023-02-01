package com.project.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Table(name="web_shops")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebShop extends Model{

    private String name;

    private String mail;

    private String password;

    private String shopId;

    private String shopSecret;

    private String successUrl;

    private String cancelUrl;

    private String returnUrl;

    @Column(name = "using_2fa")
    private Boolean using2FA;

    @Column(name = "two_factor")
    private String twoFAsecret;

    @ManyToMany
    @JoinTable(
            name = "web_shop_payment_method",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private Set<PaymentMethod> paymentMethods;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;
}
