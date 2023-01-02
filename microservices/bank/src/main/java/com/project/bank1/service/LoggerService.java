package com.project.bank1.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerService {

    private final Logger logger;

    public LoggerService(Class<?> parentClass) {
        this.logger = LogManager.getLogger(parentClass);
    }

    public void validateAcquirer(String merchantId, String merchantOrderId) {
        logger.info("Validating acquirer. Merchant ID: {} with order ID: {}", merchantId, merchantOrderId);
    }

    public void invalidMerchantCredentials(String merchantId) {
        logger.error("Merchant's credentials are incorrect (ID: {}) or merchant is not registered", merchantId);
    }
}
