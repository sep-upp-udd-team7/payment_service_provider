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

import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction createTransaction(RequestDto request, Acquirer acquirer) {
        Transaction t = new Transaction();
        t.setId(UUID.randomUUID().toString());
        t.setAmount(request.getAmount());
        t.setMerchantOrderId(request.getMerchantOrderId());
        t.setMerchantTimestamp(request.getMerchantTimestamp());
        t.setSuccessURL(request.getSuccessUrl());
        t.setErrorURL(request.getErrorUrl());
        t.setFailedURL(request.getFailedUrl());
        t.setStatus(TransactionStatus.CREATED);
//        t.setAcquirerBankAccount(acquirer.getId());
        t.setAcquirer(acquirer);
        return t;
    }
}
