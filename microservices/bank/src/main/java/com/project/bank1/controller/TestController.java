package com.project.bank1.controller;

import com.project.bank1.dto.ApiKeyDto;
import com.project.bank1.model.Acquirer;
import com.project.bank1.repository.AcquirerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
public class TestController {
    private static String bankTestEndpoint = "/clients/getAll";
    @Autowired
    private WebClient webClient;
    @Autowired
    private Environment environment;
    @Autowired
    private AcquirerRepository acquirerRepository;

    @GetMapping("/test")
    public String test(){
        return "Welcome from bank 1";
    }

    @PostMapping("/test1")
    public String test1(@RequestBody String api){
        List<Acquirer> acquirers = acquirerRepository.findAll();
        Acquirer a = acquirers.get(0); // test
        String apiKey =  a.getApiKey();
        String header = "Bearer " + apiKey;

        String bankBackendUl = environment.getProperty("bank1-application.backend");
        System.out.println(bankBackendUl);
        ResponseEntity<ApiKeyDto> response = webClient.post()
                .uri( bankBackendUl + "/api-keys")
                .headers(httpHeaders -> {
                    httpHeaders.set("Connection", header);
                })
                .body(BodyInserters.fromValue(api))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ApiKeyDto.class)
                .block();
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("response");
            System.out.println(response.getBody().getMerchantId() + "  => " + response.getBody().getBankName() );
            return response.getBody().getMerchantId();
        } else {
            return "Error!";
        }
    }
}
