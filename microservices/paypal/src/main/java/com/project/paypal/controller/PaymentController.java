package com.project.paypal.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.dto.CreatePaymentDto;
import com.project.paypal.dto.ExecutePaymentDto;
import com.project.paypal.dto.PaypalRedirectUrlDto;
import com.project.paypal.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment")
    public ResponseEntity<PaypalRedirectUrlDto> createPayment(@RequestBody CreatePaymentDto createPaymentDto) {
        try {
            Payment payment = paymentService.createPayment(createPaymentDto.getAmount());

            System.out.println(payment);
            System.out.println(payment.getBillingAgreementTokens());
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    PaypalRedirectUrlDto dto=new PaypalRedirectUrlDto();
                    dto.setUrl(link.getHref());
                    return new ResponseEntity<PaypalRedirectUrlDto>(dto, HttpStatus.OK);
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new PaypalRedirectUrlDto(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/execute")
    public ResponseEntity<?> executePayment(@RequestBody ExecutePaymentDto dto) {

        try {
            boolean executed = paymentService.executePayment(dto);
            if (executed) {
                return new ResponseEntity<Boolean>(HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
    }
}
