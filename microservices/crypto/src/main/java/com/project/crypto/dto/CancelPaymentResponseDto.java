package com.project.crypto.dto;

public class CancelPaymentResponseDto {
    private boolean success;
    private String url;

    public CancelPaymentResponseDto() {}

    public CancelPaymentResponseDto(boolean success, String url) {
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
