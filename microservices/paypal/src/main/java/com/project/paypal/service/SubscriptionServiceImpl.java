package com.project.paypal.service;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
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


    @Override
    public Plan createSubscriptionPlan() throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException {
        Plan plan = new Plan();
        plan.setName("T-Shirt of the Month Club Plan");
        plan.setDescription("Template creation.");
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
        currency.setCurrency("USD");
        currency.setValue("200");
        paymentDefinition.setAmount(currency);

        //charge_models
        ChargeModels chargeModels = new com.paypal.api.payments.ChargeModels();
        chargeModels.setType("TAX");
        chargeModels.setAmount(currency);
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
        merchantPreferences.setCancelUrl("http://localhost:4200");
        merchantPreferences.setReturnUrl("http://localhost:4200");
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

        Plan requestPlan=new Plan();
        requestPlan.setId(plan.getId());
        apiContext.setMaskRequestId(true);

        Agreement agreement=new Agreement();
        agreement.setName("Testno placanje");
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String formatted = sdf.format(d);
        agreement.setStartDate(formatted);
        agreement.setPayer(new Payer().setPaymentMethod("paypal"));
        agreement.setPlan(requestPlan);
        agreement.setDescription("Opis agreement-a");



        agreement=agreement.create(apiContext);
        System.out.println(agreement);

        Agreement executableAgreement=new Agreement();
        executableAgreement.setToken(agreement.getToken());

        Agreement agreementAfterExecution=Agreement.execute(apiContext,agreement.getToken());

        return new Plan();

    }
}
