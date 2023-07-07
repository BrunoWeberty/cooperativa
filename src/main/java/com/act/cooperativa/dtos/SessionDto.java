package com.act.cooperativa.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionDto {

    @NotBlank
    private String name;
}
