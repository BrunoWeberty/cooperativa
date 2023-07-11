package com.act.cooperativa.services.feign;

import com.act.cooperativa.services.CheckCpfService;
import com.act.cooperativa.services.exception.ServiceException;
import com.act.cooperativa.services.feign.client.CheckCpfFeignClient;
import com.act.cooperativa.services.feign.config.FeignClientException;
import com.act.cooperativa.services.feign.response.CheckCpfResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckCpfFeignService implements CheckCpfService {

    @Autowired
    private final CheckCpfFeignClient checkCpfFeignClient;

    @Override
    public CheckCpfResponse execute(String cpf) {
        try {
            return checkCpfFeignClient.execute(cpf);
        } catch (FeignClientException e) {
            throw new RuntimeException(e);
        }
    }
}
