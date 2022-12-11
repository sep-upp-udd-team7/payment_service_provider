package com.project.paypal.service.interfaces;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.dto.CreatePaymentDto;
import com.project.paypal.dto.ExecutePaymentDto;

public interface PaymentService {
    Payment createPayment(CreatePaymentDto createPaymentDto) throws PayPalRESTException;
    boolean executePayment(ExecutePaymentDto executePaymentDto) throws PayPalRESTException;
    boolean cancelPayment(String transactionId);
}
