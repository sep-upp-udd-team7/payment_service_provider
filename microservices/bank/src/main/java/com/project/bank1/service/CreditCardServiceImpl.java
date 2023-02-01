package com.project.bank1.service;

import com.project.bank1.dto.*;
import com.project.bank1.enums.TransactionStatus;
import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Transaction;
import com.project.bank1.service.interfaces.AcquirerService;
import com.project.bank1.service.interfaces.CreditCardService;
import com.project.bank1.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;

@Service
public class CreditCardServiceImpl implements CreditCardService {
    private LoggerService loggerService = new LoggerService(this.getClass());
    private static String validateIssuerEndpoint = "/accounts/validateAcquirer";
    @Autowired
    private Environment env;
    @Autowired
    private AcquirerService acquirerService;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private WebClient webClient;
    @Autowired
    private Environment environment;

    @Override
    public RequestDto validateAcquirer(OnboardingRequestDto dto) throws Exception {
        loggerService.infoLog(MessageFormat.format("Validating acquirer by shop ID: {0} for merchant order ID: {1}",
                dto.getShopId(), dto.getMerchantOrderId()));
        Acquirer acquirer = acquirerService.findByShopId(dto.getShopId());
        if(acquirer == null) {
            String message = MessageFormat.format("Merchant's credentials are incorrect (shop ID: {0}) or merchant is not registered", dto.getShopId());
            loggerService.errorLog(message);
            throw new Exception(message);
        }
        RequestDto request = new RequestDto();
        request.setAmount(dto.getAmount());
        request.setMerchantId(acquirer.getMerchantId());
        request.setMerchantPassword(acquirer.getMerchantPassword());
        request.setMerchantOrderId(dto.getMerchantOrderId());
        request.setMerchantTimestamp(dto.getMerchantTimestamp());
        WebShopUrl webShopUrl = getWebShopUrls(dto.getShopId());
        if (webShopUrl == null) {
            String message = MessageFormat.format("Error getting web shop URL for redirection (shop ID: {0})", dto.getShopId());
            loggerService.errorLog(message);
            throw new Exception(message);
        }
        // TODO: proveriti na koje stranice se redirektuju ovi linkovi
        request.setSuccessUrl(webShopUrl.getSuccessUrl());
        request.setFailedUrl(webShopUrl.getCancelUrl());
        request.setErrorUrl(webShopUrl.getReturnUrl());
        request.setQrCode(dto.getQrCode());
        loggerService.successLog(MessageFormat.format("Successfully validated acquirer (shop ID: {0}) ", dto.getShopId()));
        return request;
    }

    private WebShopUrl getWebShopUrls(String shopId) {
        String authServiceWebShopUrls = environment.getProperty("psp.auth-service");
        ResponseEntity<WebShopUrl> response = webClient.get()
                .uri( authServiceWebShopUrls + shopId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(WebShopUrl.class)
                .block();
        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        return response.getBody();
    }

    @Override
    public AcquirerResponseDto startPayment(OnboardingRequestDto dto) throws Exception {
        loggerService.infoLog(MessageFormat.format("Stating payment by shop ID: {0}", dto.getShopId()));
        RequestDto request = validateAcquirer(dto);
        Acquirer acquirer = acquirerService.findByShopId(dto.getShopId());
        Transaction transaction = transactionService.createTransaction(request, acquirer);
        try {
            loggerService.infoLog(MessageFormat.format("Sending POST request to bank application on URL: {0}",
                    acquirer.getBank().getBankUrl() + validateIssuerEndpoint));

            ResponseEntity<AcquirerResponseDto> response = webClient.post()
                    .uri(acquirer.getBank().getBankUrl() + validateIssuerEndpoint)
                    .body(BodyInserters.fromValue(request))
                    .headers(httpHeaders -> {
                        httpHeaders.set("Connection", "Bearer " + acquirer.getApiKey());
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(AcquirerResponseDto.class)
                    .block();

            if (!response.getStatusCode().is2xxSuccessful()) {
                loggerService.errorLog(MessageFormat.format("Error transaction! Response status code from bank application is {0}", response.getStatusCode()));
                transaction.setStatus(TransactionStatus.ERROR);
                transactionService.save(transaction);
                String errorPaymentUrl = env.getProperty("psp.frontend") + env.getProperty("psp.error-payment");
                throw new Exception(errorPaymentUrl);
            }
            loggerService.successLog(MessageFormat.format("Successfully started transaction with payment ID: {0}", response.getBody().getPaymentId()));
            transaction.setPaymentId(response.getBody().getPaymentId());
            transactionService.save(transaction);
            return getAcquirerResponseDtoWhenResponseIsSuccessful(response);
        } catch (Exception e) {
            loggerService.errorLog(MessageFormat.format("An error occurred while sending an HTTP request to bank application " +
                    "{}", acquirer.getBank().getBankUrl() + validateIssuerEndpoint));
            transaction.setStatus(TransactionStatus.ERROR);
            transactionService.save(transaction);
            String errorPaymentUrl = env.getProperty("psp.frontend") + env.getProperty("psp.error-payment");
            throw new Exception(errorPaymentUrl);
        }
    }

    @Override
    public String finishPayment(ResponseDto dto) throws Exception {
        loggerService.infoLog(MessageFormat.format("Starting finish payment with payment ID: {0}", dto.getPaymentId()));
        Transaction t = transactionService.findByPaymentId(dto.getPaymentId());
        if (t == null) {
            loggerService.errorLog(MessageFormat.format("Transaction with payment ID: {0} not found", dto.getPaymentId()));
            throw new Exception(env.getProperty("psp.frontend") + env.getProperty("psp.error-payment"));
        }
        t.setStatus(getTransactionStatusFromDto(dto.getTransactionStatus()));
        transactionService.save(t); //TODO:DODATO
        loggerService.infoLog(MessageFormat.format("Transaction is: {0} with payment ID: {1}", dto.getTransactionStatus(), dto.getPaymentId()));
        String redirectionUrl = getRedirectionUrl(dto.getTransactionStatus(), t);

        return ;
    }

    private String getRedirectionUrl(String transactionStatus, Transaction transaction) {
        if (transactionStatus.equals("FAILED")) {
            return transaction.getFailedURL();
        } else if (transactionStatus.equals("ERROR")) {
            return transaction.getErrorURL();
        }
        return transaction.getSuccessURL();
    }

    private TransactionStatus getTransactionStatusFromDto(String transactionStatus) {
        if (transactionStatus.equals("FAILED")) {
            return TransactionStatus.FAILED;
        } else if (transactionStatus.equals("ERROR")) {
            return TransactionStatus.ERROR;
        }
        return TransactionStatus.SUCCESS;
    }

    private AcquirerResponseDto getAcquirerResponseDtoWhenResponseIsSuccessful(ResponseEntity<AcquirerResponseDto> response) {
        AcquirerResponseDto responseDto = new AcquirerResponseDto();
        responseDto.setPaymentId(response.getBody().getPaymentId());
        responseDto.setPaymentUrl(response.getBody().getPaymentUrl());
        return responseDto;
    }

}
