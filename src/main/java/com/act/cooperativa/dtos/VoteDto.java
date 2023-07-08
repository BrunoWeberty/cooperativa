package com.act.cooperativa.dtos;

import com.act.cooperativa.enuns.Vote;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteDto {

    @NotNull
    private Vote vote;
}
