package com.project.crypto.repository;

import com.project.crypto.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Merchant findByMerchantId(String merchantId);
}

