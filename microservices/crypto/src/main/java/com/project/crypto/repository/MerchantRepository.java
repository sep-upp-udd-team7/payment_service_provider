package com.project.crypto.repository;

import com.project.crypto.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("Select m from Merchant m WHERE m.merchantId=?1")
    Merchant findByMerchantId(String merchantId);
}

