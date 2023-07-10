package com.act.cooperativa.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VotingDto {

    @JsonProperty("idCampoNumerico")
    private Integer duration;

    @NotNull
    @JsonProperty("idCampoData")
    private LocalDateTime dateSession;
}
