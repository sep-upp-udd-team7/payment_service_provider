package com.project.paypal.controller;

import com.paypal.api.payments.Plan;
import com.paypal.base.rest.PayPalRESTException;
import com.project.paypal.service.interfaces.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/create-plan")
    public ResponseEntity<?> createSubscriptionPlan(){
        try{
            Plan plan=subscriptionService.createSubscriptionPlan();
            System.out.println(plan);
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
}
