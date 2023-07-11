package com.act.cooperativa.services.feign.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

@SuppressWarnings("ALL")
public class DefaultFeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new DefaultFeignErrorDecoder();
    }

}
