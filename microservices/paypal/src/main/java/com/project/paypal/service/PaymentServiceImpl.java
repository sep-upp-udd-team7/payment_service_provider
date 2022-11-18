package com.project.paypal.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.dto.ExecutePaymentDto;
import com.project.paypal.model.LocalTransaction;
import com.project.paypal.model.TransactionStatus;
import com.project.paypal.repository.LocalTransactionRepository;
import com.project.paypal.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Integer transactionIdLength = 16;
    private static final String cancelURL = "http://localhost:4200/";
    private static final String returnURL = "http://localhost:4200/";
    private final LocalTransactionRepository transactionRepository;
    private final APIContext apiContext;

    @Override
    public Payment createPayment(String price) throws PayPalRESTException {

        Payment payment = createPaypalPayment(price);
        LocalTransaction localTransaction = createLocalTransaction(price);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelURL + "?transaction_id=" + localTransaction.getTransactionId());
        redirectUrls.setReturnUrl(returnURL + "?transaction_id=" + localTransaction.getTransactionId());
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    private LocalTransaction createLocalTransaction(String price) {
        LocalTransaction localTransaction = new LocalTransaction();

        localTransaction.setAmount(Double.parseDouble(price));
        localTransaction.setStatus(TransactionStatus.PENDING);
        localTransaction.setTransactionId(generateTransactionId(transactionIdLength));
        transactionRepository.save(localTransaction);

        return localTransaction;
    }

    private Payment createPaypalPayment(String price) {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        Double total = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Testing our paypal");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("PAYPAL");

        Payment payment = new Payment();
        payment.setIntent("SALE");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        return payment;
    }

    @Override
    public boolean executePayment(ExecutePaymentDto executePaymentDto) throws PayPalRESTException {
        boolean executed=false;
        Payment payment = new Payment();
        payment.setId(executePaymentDto.getPaymentId());
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(executePaymentDto.getPayerId());
        payment=payment.execute(apiContext, paymentExecute);
        if (payment.getState().equals("approved")) {
            LocalTransaction transaction = transactionRepository.getByTransactionId(executePaymentDto.getTransactionId());
            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setPayerId(executePaymentDto.getPayerId());
            transactionRepository.save(transaction);
            executed=true;
        }
        return executed;
    }

    private String generateTransactionId(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
}
