package com.project.paypal.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.dto.CreatePaymentDto;
import com.project.paypal.dto.ExecutePaymentDto;
import com.project.paypal.dto.PaypalRedirectUrlDto;
import com.project.paypal.service.LoggerService;
import com.project.paypal.service.interfaces.PaymentService;
import com.project.paypal.utils.LogData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private LoggerService loggerService = new LoggerService(this.getClass());

    @PostMapping("/create-payment")
    public ResponseEntity<PaypalRedirectUrlDto> createPayment(@RequestBody CreatePaymentDto createPaymentDto) {
        try {

            String message=String.format("Shop with id %s request to create payment with id %s and amount %s",createPaymentDto.getShopId(),createPaymentDto.getTransactionId(),createPaymentDto.getAmount());
            loggerService.logInfo(new LogData(message));
            Payment payment = paymentService.createPayment(createPaymentDto);

            System.out.println(payment);
            System.out.println(payment.getBillingAgreementTokens());
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    message=String.format("URL %s for executing transaction is successfully created",link.getHref());
                    loggerService.logInfo(new LogData(message));
                    PaypalRedirectUrlDto dto = new PaypalRedirectUrlDto();
                    dto.setUrl(link.getHref());
                    return new ResponseEntity<PaypalRedirectUrlDto>(dto, HttpStatus.OK);
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        String message=String.format("Unable to generate paypal payment and payment url for transaction %s",createPaymentDto.getTransactionId());
        loggerService.logError(new LogData(message));
        return new ResponseEntity<>(new PaypalRedirectUrlDto(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/execute")
    public ResponseEntity<?> executePayment(@RequestBody ExecutePaymentDto dto) {

        String message=String.format("Transaction with id %s need to be executed by payer %s",dto.getTransactionId(),dto.getPayerId());
        loggerService.logInfo(new LogData(message));
        try {
            boolean executed = paymentService.executePayment(dto);
            if (executed) {

                return new ResponseEntity<Boolean>(HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        message=String.format("Unable to execute transaction because id %s is not valid",dto.getTransactionId());
        loggerService.logError(new LogData(message));
        return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("cancel-payment/{id}")
    public ResponseEntity<?> cancelPayment(@PathVariable("id") String transactionId) {
        if (paymentService.cancelPayment(transactionId)) {
            return new ResponseEntity<Boolean>(HttpStatus.OK);
        } else {
            String message=String.format("Unable to cancel transaction because transaction id %s is not valid",transactionId);
            loggerService.logError(new LogData(message));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
