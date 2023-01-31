package com.project.crypto.repository;

import com.project.crypto.model.CryptoOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoOrderRepository extends JpaRepository<CryptoOrder, Long> {
    CryptoOrder findByOrderId(String orderId);
}
