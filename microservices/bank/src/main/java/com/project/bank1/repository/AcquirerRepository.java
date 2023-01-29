package com.project.bank1.repository;

import com.project.bank1.model.Acquirer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import static org.hibernate.loader.Loader.SELECT;

public interface AcquirerRepository extends JpaRepository<Acquirer, Long> {
    Acquirer findByMerchantId(String merchantId);


    @Query("SELECT a from Acquirer a where a.shopId=?1")
    Acquirer getByShopId(String shopId);
}
