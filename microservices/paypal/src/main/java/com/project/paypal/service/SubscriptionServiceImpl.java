package com.project.paypal.service;

import com.paypal.api.payments.Currency;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.model.Subscription;
import com.project.paypal.model.SubscriptionPlan;
import com.project.paypal.model.SubscriptionStatus;
import com.project.paypal.repository.SubscriptionPlanRepository;
import com.project.paypal.repository.SubscriptionRepository;
import com.project.paypal.service.interfaces.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final APIContext apiContext;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;


    @Override
    public Agreement createSubscription() throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException {

        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.get(1l);
        Plan plan = createPlan(subscriptionPlan);

        Plan requestPlan = new Plan();
        requestPlan.setId(plan.getId());
        apiContext.setMaskRequestId(true);

        Agreement agreement = new Agreement();
        agreement.setName("Premium shop payment");
        Date startDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String formattedStartDate = sdf.format(startDate);
        agreement.setStartDate(formattedStartDate);
        agreement.setPayer(new Payer().setPaymentMethod("paypal"));
        agreement.setPlan(requestPlan);
        agreement.setDescription(subscriptionPlan.getDescription());
        agreement = agreement.create(apiContext);

        Subscription subscription = new Subscription();
        subscription.setName(subscriptionPlan.getDescription());
        subscription.setStartDate(startDate);
        subscription.setToken(agreement.getToken());
        subscription.setStatus(SubscriptionStatus.CREATED);
        subscriptionRepository.save(subscription);

        return agreement;

    }

    @Override
    public boolean executeSubscription(String token) throws PayPalRESTException {
        Subscription subscription = subscriptionRepository.getSubscriptionByToken(token);
        boolean operationResult = false;
        if (subscription != null) {
            Agreement agreementAfterExecution = Agreement.execute(apiContext, token);
            subscription.setSubscriberId(agreementAfterExecution.getPayer().getPayerInfo().getPayerId());
            subscription.setSubscriberMail(agreementAfterExecution.getPayer().getPayerInfo().getEmail());
            subscription.setStatus(SubscriptionStatus.CONFIRMED);
            subscriptionRepository.save(subscription);
            operationResult=true;
        }
        return operationResult;

    }

    @Override
    public boolean cancelSubscription(String token) {
        Subscription subscription=subscriptionRepository.getSubscriptionByToken(token);
        boolean operationResult = false;
        if (subscription != null) {
            operationResult=true;
            subscription.setStatus(SubscriptionStatus.CANCELED);
            subscriptionRepository.save(subscription);
        }
        return operationResult;
    }

    private Plan createPlan(SubscriptionPlan subscriptionPlan) throws PayPalRESTException {
        Plan plan = new Plan();

        plan.setName(subscriptionPlan.getName());
        plan.setDescription(subscriptionPlan.getDescription());
        plan.setType("fixed");

        //payment_definitions
        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName("Regular Payments");
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency("MONTH");
        paymentDefinition.setFrequencyInterval("1");
        paymentDefinition.setCycles("12");

        //currency
        Currency currency = new Currency();
        currency.setCurrency(subscriptionPlan.getCurrency());
        currency.setValue(subscriptionPlan.getAmountValue());
        paymentDefinition.setAmount(currency);

        //charge_models
        ChargeModels chargeModels = new ChargeModels();
        chargeModels.setType("TAX");
        chargeModels.setAmount(new Currency().setCurrency("USD").setValue("0"));
        List<ChargeModels> chargeModelsList = new ArrayList<ChargeModels>();
        chargeModelsList.add(chargeModels);
        paymentDefinition.setChargeModels(chargeModelsList);

        //payment_definition
        List<PaymentDefinition> paymentDefinitionList = new ArrayList<PaymentDefinition>();
        paymentDefinitionList.add(paymentDefinition);
        plan.setPaymentDefinitions(paymentDefinitionList);

        //merchant_preferences
        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setSetupFee(currency);
        merchantPreferences.setCancelUrl("http://localhost:4200/cancel-subscription");
        merchantPreferences.setReturnUrl("http://localhost:4200/confirm-subscription");
        merchantPreferences.setMaxFailAttempts("0");
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");
        plan.setMerchantPreferences(merchantPreferences);

        plan = plan.create(apiContext);

        List<Patch> patchRequestList = new ArrayList<Patch>();
        Map<String, String> value = new HashMap<String, String>();
        value.put("state", "ACTIVE");

        Patch patch = new Patch();
        patch.setPath("/");
        patch.setValue(value);
        patch.setOp("replace");
        patchRequestList.add(patch);

        plan.update(apiContext, patchRequestList);
        return plan;
    }
}
