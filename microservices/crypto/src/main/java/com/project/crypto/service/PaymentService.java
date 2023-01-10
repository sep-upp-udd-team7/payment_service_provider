package com.project.crypto.service;

import com.google.gson.Gson;
import com.project.crypto.dto.CancelPaymentResponseDto;
import com.project.crypto.dto.ConfirmPaymentResponseDto;
import com.project.crypto.dto.CreateOrderDto;
import com.project.crypto.model.CreateOrderResponse;
import com.project.crypto.model.CryptoOrder;
import com.project.crypto.model.CryptoOrderStatus;
import com.project.crypto.model.Merchant;
import com.project.crypto.repository.CryptoOrderRepository;
import com.project.crypto.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;


@Service
public class PaymentService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private CryptoOrderRepository cryptoOrderRepository;

    public CreateOrderResponse createOrder(CreateOrderDto createOrderDto) {

        String urlPSPCrypto = "http://localhost:8000/crypto-service/";
        String urlCoinGate = "https://api-sandbox.coingate.com/api/v2";
        //URLEncoder.encode("", StandardCharsets.UTF_8);

        Merchant merchant = merchantRepository.findByMerchantId(createOrderDto.getShopId());

        StringBuilder body = new StringBuilder();
        body.append("order_id=").append(createOrderDto.getTransactionId()).append("&");
        body.append("price_amount=").append(createOrderDto.getAmount()).append("&");
        body.append("price_currency=EUR").append("&");
        body.append("receive_currency=EUR").append("&");
        body.append("title=").append(createOrderDto.getShopId()).append("&");

        body.append("cancel_url=").append(URLEncoder.encode(urlPSPCrypto+"cancel-order/", StandardCharsets.UTF_8)).append(createOrderDto.getTransactionId()).append("&");
        body.append("success_url=").append(URLEncoder.encode(urlPSPCrypto+"confirm-order/", StandardCharsets.UTF_8)).append(createOrderDto.getTransactionId());


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCoinGate +"/orders"))
                .header("accept", "application/json")
                .header("content-type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + merchant.getToken())
                .method("POST", HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println(response.body());

        Gson gson = new Gson();
        CreateOrderResponse createOrderResponse = gson.fromJson(response.body(), CreateOrderResponse.class);

        //String orderId, double priceAmount, String priceCurrency, String title, CryptoOrderStatus status, Merchant merchant
        CryptoOrder cryptoOrder = new CryptoOrder(createOrderDto.getTransactionId(), createOrderResponse.getId(),Double.parseDouble(createOrderDto.getAmount()), "EUR", createOrderDto.getShopId(), CryptoOrderStatus.NEW, merchant);
        cryptoOrderRepository.saveAndFlush(cryptoOrder);

        return createOrderResponse;

    }

    public CancelPaymentResponseDto cancelOrder(String orderId) {

        CryptoOrder cryptoOrder = cryptoOrderRepository.findByOrderId(orderId);
        cryptoOrder.setStatus(CryptoOrderStatus.CANCELLED);
        cryptoOrderRepository.saveAndFlush(cryptoOrder);
        String merchantCallbackLink = "http://localhost:9000/api/orders/cancel/" + orderId;

        return new CancelPaymentResponseDto(true, merchantCallbackLink);
    }

    public ConfirmPaymentResponseDto confirmOrder(String orderId) {

        CryptoOrder cryptoOrder = cryptoOrderRepository.findByOrderId(orderId);
        cryptoOrder.setStatus(CryptoOrderStatus.PAID);
        cryptoOrderRepository.saveAndFlush(cryptoOrder);
        String merchantCallbackLink = "http://localhost:9000/api/orders/confirm/" + orderId;

        return new ConfirmPaymentResponseDto(true, merchantCallbackLink);
    }
}
