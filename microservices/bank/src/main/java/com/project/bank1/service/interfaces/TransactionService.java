package com.project.bank1.service.interfaces;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.RequestDto;
import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Transaction;

public interface TransactionService {
    void save(Transaction transaction);

    Transaction createTransaction(RequestDto request, Acquirer acquirer);
}
