package com.project.crypto.controller;

import com.project.crypto.dto.OrderResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello from crypto microservice";
    }

    @PostMapping("/callback")
    public String callback(@RequestBody OrderResponse body) {
        System.out.println(body.getStatus());
        return "Hello from crypto microservice";
    }
}
