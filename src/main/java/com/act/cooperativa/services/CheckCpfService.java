package com.act.cooperativa.services;

import com.act.cooperativa.services.feign.config.FeignClientException;
import com.act.cooperativa.services.feign.response.CheckCpfResponse;

public interface CheckCpfService {
    CheckCpfResponse execute(String cpf) throws FeignClientException;
}
