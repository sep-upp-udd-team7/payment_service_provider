package com.project.paypal.repository;

import com.project.paypal.model.SubscribedWebShop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribedWebShopRepository extends EntityRepository<SubscribedWebShop> {


    @Query("SELECT s from SubscribedWebShop s where s.shopId=?1")
    SubscribedWebShop getByShopId(String shopId);
}
