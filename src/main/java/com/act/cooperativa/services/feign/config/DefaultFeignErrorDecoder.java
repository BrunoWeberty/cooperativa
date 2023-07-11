package com.act.cooperativa.services.feign.config;

import com.act.cooperativa.services.feign.model.ErrorResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class DefaultFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() == null) {
            log.error("resposta do feign com corpo vazio ou nulo");
            return new FeignClientException("erro interno", response.status());
        } else {
            try (final var inputStream = response.body().asInputStream()) {
                final String result = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                final ObjectMapper mapper = new ObjectMapper();

                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

                final ErrorResponse exceptionMessage = mapper.readValue(result, ErrorResponse.class);

                return new FeignClientException(exceptionMessage.message(), response.status());
            } catch (IOException e) {
                log.error("Erro ao decodificar resposta da feign", e);
                return new FeignClientException("Ocorreu um erro interno", response.status());
            }
        }
    }

}
