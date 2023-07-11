package com.act.cooperativa.services.feign.config;

import lombok.Getter;

public class FeignClientException  extends Exception {

    @Getter
    private final Integer status;

    public FeignClientException(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public FeignClientException(String message, Throwable cause, Integer status) {
        super(message, cause);
        this.status = status;
    }

    public FeignClientException(Integer status, String message) {
        super(message);
        this.status = status;
    }
}