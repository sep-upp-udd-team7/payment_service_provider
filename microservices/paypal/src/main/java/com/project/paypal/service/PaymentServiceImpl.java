package com.project.paypal.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.dto.CreatePaymentDto;
import com.project.paypal.dto.ExecutePaymentDto;
import com.project.paypal.model.LocalTransaction;
import com.project.paypal.model.TransactionStatus;
import com.project.paypal.repository.LocalTransactionRepository;
import com.project.paypal.service.interfaces.PaymentService;
import com.project.paypal.utils.LogData;
import com.project.paypal.utils.RandomCharacterGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    @Autowired
    private static Environment environment;

    private static final String cancelURL = environment.getProperty("psp.url") + "cancel-paypal-payment";
    private static final String returnURL = environment.getProperty("psp.url") + "paypal-payment-processing/";
    private final LocalTransactionRepository transactionRepository;
    private final APIContext apiContext;
    private LoggerService loggerService = new LoggerService(this.getClass());

    @Override
    public Payment createPayment(CreatePaymentDto createPaymentDto) throws PayPalRESTException {

        Payment payment = createPaypalPayment(createPaymentDto.getAmount());
        LocalTransaction localTransaction = createLocalTransaction(createPaymentDto.getAmount(),createPaymentDto.getTransactionId());

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelURL + "?transaction_id=" + localTransaction.getTransactionId()+"&"+"shop_id="+createPaymentDto.getShopId());
        redirectUrls.setReturnUrl(returnURL + "?transaction_id=" + localTransaction.getTransactionId()+"&"+"shop_id="+createPaymentDto.getShopId());
        payment.setRedirectUrls(redirectUrls);
        apiContext.setMaskRequestId(true);

        return payment.create(apiContext);
    }

    private LocalTransaction createLocalTransaction(String price,String transactionId) {
        LocalTransaction localTransaction = new LocalTransaction();

        localTransaction.setAmount(Double.parseDouble(price));
        localTransaction.setStatus(TransactionStatus.PENDING);
        localTransaction.setTransactionId(transactionId);
        localTransaction.setCurrency("USD");
        transactionRepository.save(localTransaction);

        String message=String.format("Local transaction with id:%s and price:%s created",transactionId,price);
        loggerService.logInfo(new LogData(message));
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
    public boolean executePayment(ExecutePaymentDto dto) throws PayPalRESTException {
        boolean executed=false;
        Payment payment = new Payment();
        payment.setId(dto.getPaymentId());
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(dto.getPayerId());
        payment=payment.execute(apiContext, paymentExecute);
        if (payment.getState().equals("approved")) {
            LocalTransaction transaction = transactionRepository.getByTransactionId(dto.getTransactionId());
            transaction.setStatus(TransactionStatus.CONFIRMED);
            transaction.setPayerId(dto.getPayerId());
            transaction.setPayerMail(payment.getPayer().getPayerInfo().getEmail());
            transaction.setMerchantMail(payment.getTransactions().get(0).getPayee().getEmail());
            transaction.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency());
            transaction.setDescription(payment.getTransactions().get(0).getDescription());
            transactionRepository.save(transaction);
            String message=String.format("Transaction with id %s is successfully executed by payer %s",dto.getTransactionId(),dto.getPayerId());
            loggerService.logInfo(new LogData(message));
            executed=true;
        }
        return executed;
    }

    @Override
    public boolean cancelPayment(String transactionId) {
        LocalTransaction transaction=transactionRepository.getByTransactionId(transactionId);
        boolean isSuccessful=false;
        if (transaction!=null){
            String message=String.format("Transaction with id %s is successfully canceled",transactionId);
            loggerService.logInfo(new LogData(message));
            transaction.setStatus(TransactionStatus.CANCELED);
            transactionRepository.save(transaction);
            isSuccessful=true;
        }
        return isSuccessful;
    }


}
