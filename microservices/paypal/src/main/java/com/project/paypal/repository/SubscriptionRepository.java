package com.project.paypal.repository;

import com.project.paypal.model.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends EntityRepository<Subscription> {

    @Query("SELECT s FROM Subscription s WHERE s.token=?1")
    public Subscription getSubscriptionByToken(String token);
}
