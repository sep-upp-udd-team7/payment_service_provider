package com.project.paypal.repository;

import com.project.paypal.model.LocalTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalTransactionRepository extends EntityRepository<LocalTransaction> {
    @Query("SELECT l FROM LocalTransaction l WHERE l.transactionId=?1")
    LocalTransaction getByTransactionId(String transactionId);
}
