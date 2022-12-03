package com.project.paypal.service.interfaces;

import com.paypal.api.payments.Plan;
import com.paypal.base.rest.PayPalRESTException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface SubscriptionService {
    Plan createSubscriptionPlan() throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException;
}
