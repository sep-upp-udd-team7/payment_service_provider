package com.project.bank1.repository;

import com.project.bank1.model.Acquirer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcquirerRepository extends JpaRepository<Acquirer, Long> {
    Acquirer findByMerchantId(String merchantId);
}
