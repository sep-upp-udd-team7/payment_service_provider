package com.project.paypal.service.interfaces;

import com.paypal.api.payments.Agreement;
import com.paypal.base.rest.PayPalRESTException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface SubscriptionService {
    Agreement createSubscription(String price) throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException;
    boolean executeSubscription(String token) throws PayPalRESTException;
    boolean cancelSubscription(String token);
}
