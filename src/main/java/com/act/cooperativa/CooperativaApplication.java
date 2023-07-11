package com.act.cooperativa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Cooperativa API", version = "1", description = "API desenvolvida para computar votação de pautas"))
@EnableFeignClients
public class CooperativaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CooperativaApplication.class, args);
    }

}
