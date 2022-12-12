package com.project.paypal.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerService {
    private final Logger logger;

    public LoggerService(Class<?> parentClass) {
        this.logger = LogManager.getLogger(parentClass);
    }

    public void test(String number) {
        logger.info("Test {}", number);
    }
}
