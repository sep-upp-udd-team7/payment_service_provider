package com.project.crypto.controller;

import com.project.crypto.dto.CancelPaymentResponseDto;
import com.project.crypto.dto.ConfirmPaymentResponseDto;
import com.project.crypto.dto.CreateOrderDto;
import com.project.crypto.dto.OrderStatusDto;
import com.project.crypto.model.CreateOrderResponse;
import com.project.crypto.service.LoggerService;
import com.project.crypto.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private LoggerService loggerService = new LoggerService(this.getClass());

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        CreateOrderResponse response = paymentService.createOrder(createOrderDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cancel-order/{id}")
    public ResponseEntity<CancelPaymentResponseDto> cancelPayment(@PathVariable("id") String orderId){
        CancelPaymentResponseDto cancelPaymentResponse = paymentService.cancelOrder(orderId);
        return new ResponseEntity<CancelPaymentResponseDto>(cancelPaymentResponse, HttpStatus.OK);
    }

    @GetMapping("/confirm-order/{id}")
    public ResponseEntity<ConfirmPaymentResponseDto> confirmPayment(@PathVariable("id") String orderId){
        ConfirmPaymentResponseDto confirmPaymentResponse = paymentService.confirmOrder(orderId);
        return new ResponseEntity<ConfirmPaymentResponseDto>(confirmPaymentResponse, HttpStatus.OK);
    }

    @GetMapping("/get-order-status/{id}")
    public ResponseEntity<OrderStatusDto> getOrderStatus(@PathVariable("id") String orderId){
        OrderStatusDto orderStatusDto = paymentService.getOrderStatus(orderId);
        return new ResponseEntity<OrderStatusDto>(orderStatusDto, HttpStatus.OK);
    }
}
