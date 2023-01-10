package com.project.crypto.dto;

import lombok.Data;


public class ConfirmPaymentResponseDto {
    private boolean success;
    private String url;

    public ConfirmPaymentResponseDto(){}

    public ConfirmPaymentResponseDto(boolean success, String url) {
        this.success = success;
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
