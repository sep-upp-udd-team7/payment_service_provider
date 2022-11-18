package com.project.paypal.service.interfaces;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.dto.ExecutePaymentDto;

public interface PaymentService {
    Payment createPayment(String amount) throws PayPalRESTException;
    boolean executePayment(ExecutePaymentDto executePaymentDto) throws PayPalRESTException;
}
