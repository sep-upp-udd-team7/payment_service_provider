package com.project.bank1.service;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.RequestDto;
import com.project.bank1.enums.TransactionStatus;
import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Transaction;
import com.project.bank1.repository.TransactionRepository;
import com.project.bank1.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {
    private LoggerService loggerService = new LoggerService(this.getClass());
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction createTransaction(RequestDto request, Acquirer acquirer) {
        loggerService.infoLog(MessageFormat.format("Creating transaction for acquirer with ID: {0}", acquirer.getId()));
        Transaction t = new Transaction();
        t.setId(UUID.randomUUID().toString());
        t.setAmount(request.getAmount());
        t.setMerchantOrderId(request.getMerchantOrderId());
        t.setMerchantTimestamp(request.getMerchantTimestamp());
        t.setSuccessURL(request.getSuccessUrl());
        t.setErrorURL(request.getErrorUrl());
        t.setFailedURL(request.getFailedUrl());
        t.setStatus(TransactionStatus.CREATED);
        t.setAcquirer(acquirer);
        loggerService.successLog(MessageFormat.format("Created transaction for acquirer with ID: {0}", acquirer.getId()));
        return t;
    }

    @Override
    public Transaction findByPaymentId(String paymentId) {
        loggerService.infoLog(MessageFormat.format("Find transaction by payment ID: {0}", paymentId));
        for (Transaction t: transactionRepository.findAll()) {
            if (t.getPaymentId().equals(paymentId)) {
                loggerService.successLog(MessageFormat.format("Found transaction with ID: {0}", t.getId()));
                return t;
            }
        }
        loggerService.errorLog(MessageFormat.format("Transaction not found by payment ID: {0}", paymentId));
        return null;
    }
}
