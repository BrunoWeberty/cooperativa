package com.act.cooperativa.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class AssociateDto {

    @NotBlank
    private String name;

    @NotBlank
    @CPF
    private String cpf;
}
