package com.project.paypal.configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfiguration {

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public OAuthTokenCredential oAuthTokenCredential(){
        return new OAuthTokenCredential(clientId,clientSecret,paypalSDKConfig());
    }

    @Bean
    public Map<String, String> paypalSDKConfig() {
        Map<String,String> config=new HashMap<>();
        config.put("mode",mode);
        return config;
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context=new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSDKConfig());
        return context;
    }


}
