package com.project.bank1.controller;

import com.netflix.discovery.converters.Auto;
import com.project.bank1.config.WebClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class TestController {
    private static String bankTestEndpoint = "/clients/getAll";
    @Autowired
    private WebClient webClient;
    @Autowired
    private Environment environment;

    @GetMapping("/test")
    public String test(){
        return "Welcome from bank 1";
    }

    @GetMapping("/test1")
    public String test1(){
        String bankBackendUl = environment.getProperty("bank-application.backend");
        System.out.println(bankBackendUl);
        ResponseEntity<String> response = webClient.get()
                .uri( bankBackendUl + bankTestEndpoint)
                .retrieve()
                .toEntity(String.class)
                .block();
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return "Error!";
        }
    }
}
