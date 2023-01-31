package com.project.paypal.controller;

import com.paypal.api.payments.Agreement;
import com.paypal.api.payments.Links;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.dto.CreatePaypalSubscriptionDto;
import com.project.paypal.dto.PaypalRedirectUrlDto;
import com.project.paypal.service.interfaces.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@RestController()
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriptions/create")
    public ResponseEntity<?> createSubscription(@RequestBody CreatePaypalSubscriptionDto createPaypalSubscriptionDto) {
        try {
            Agreement agreement = subscriptionService.createSubscription(createPaypalSubscriptionDto.getPrice());
            for (Links link : agreement.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    PaypalRedirectUrlDto dto = new PaypalRedirectUrlDto();
                    dto.setUrl(link.getHref());
                    dto.setToken(agreement.getToken());
                    return new ResponseEntity<PaypalRedirectUrlDto>(dto, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PayPalRESTException e) {
            e.printStackTrace();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/subscriptions/execute/{token}")
    public ResponseEntity<?> executeSubscription(@PathVariable String token) {
        try {
            if (subscriptionService.executeSubscription(token)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/subscriptions/cancel/{token}")
    public ResponseEntity<?> cancelSubscription(@PathVariable String token){
        if (subscriptionService.cancelSubscription(token)) {
            return new ResponseEntity<Boolean>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
