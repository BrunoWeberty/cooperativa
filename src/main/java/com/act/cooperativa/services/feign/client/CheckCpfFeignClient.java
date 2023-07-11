package com.act.cooperativa.services.feign.client;

import com.act.cooperativa.services.feign.config.DefaultFeignConfig;
import com.act.cooperativa.services.feign.config.FeignClientException;
import com.act.cooperativa.services.feign.response.CheckCpfResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CheckCpfFeignClient", url = "https://user-info.herokuapp.com", configuration = DefaultFeignConfig.class)
public interface CheckCpfFeignClient {

    @GetMapping("/users/{cpf}")
    CheckCpfResponse execute(@PathVariable(value = "cpf") String cpf) throws FeignClientException;
}
