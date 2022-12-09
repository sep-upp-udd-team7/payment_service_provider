package com.project.bank1.controller;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.ws.rs.core.Request;
import java.time.LocalDateTime;

@RestController
public class BankController {
    private static String validateIssuerEndpoint = "/accounts/validateAcquirer";
    @Autowired
    private WebClient webClient;
    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.POST, value = "/credit-card")
    public String validateIssuer(@RequestBody OnboardingRequestDto dto){
        String bankBackendUrl = environment.getProperty("bank-application.backend");
        String bankFrontendUrl = environment.getProperty("bank-application.frontend");
        System.out.println(bankBackendUrl + " " + bankFrontendUrl);

        RequestDto request = new RequestDto();
        request.setAmount(dto.getAmount());
        request.setMerchantId(dto.getMerchantId());
        // TODO SD: na osnovu merchant id -> izvuci merchant pass
        request.setMerchantPassword("iwoznljjrgwfinrkylmwzaqpsijhutdailaylwhxfoucnoncxapigphrucedmolsicyurvntbnvmhsrcpzleilsjlkocwiitvyde");
        request.setMerchantOrderId(dto.getMerchantOrderId());
        request.setMerchantTimestamp(LocalDateTime.now());
        request.setSuccessUrl(bankFrontendUrl + "/success");
        request.setFailedUrl(bankFrontendUrl + "/failed");
        request.setErrorUrl(bankFrontendUrl + "/error");


        ResponseEntity<String> response = webClient.post()
                .uri( bankBackendUrl + validateIssuerEndpoint)
                .body(BodyInserters.fromValue(request))
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
