package com.project.auth_service.repository;

import com.project.auth_service.model.WebShop;
import org.springframework.data.jpa.repository.Query;

public interface WebShopRepository extends EntityRepository<WebShop> {

    @Query("SELECT s from WebShop s WHERE s.shopId=?1")
    public WebShop getShopById(String id);

    @Query("SELECT s from WebShop s WHERE s.mail=?1")
    public WebShop getShopByMail(String mail);

}
