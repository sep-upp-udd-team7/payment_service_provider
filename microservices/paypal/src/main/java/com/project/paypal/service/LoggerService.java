package com.project.paypal.service;

import com.project.paypal.utils.LogData;
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

    public void logError(LogData logData){
        logger.error(logData.toString());
    }

    public void logInfo(LogData logData){
        logger.info(logData.toString());
    }

    public void logWarning(LogData logData){
        logger.warn(logData.toString());
    }

}
