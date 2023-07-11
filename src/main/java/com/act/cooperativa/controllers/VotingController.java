package com.act.cooperativa.controllers;

import com.act.cooperativa.dtos.VoteDto;
import com.act.cooperativa.dtos.VotingDto;
import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.model.VoteModel;
import com.act.cooperativa.model.VotingModel;
import com.act.cooperativa.services.SessionService;
import com.act.cooperativa.services.VoteService;
import com.act.cooperativa.services.VotingService;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.SaveException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Voting Controller")
public class VotingController {

    @Autowired
    VotingService votingService;

    @Autowired
    SessionService sessionService;

    @Autowired
    VoteService voteService;

    @PostMapping(value = "/sessions/{sessionId}/voting", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Abre uma votação dentro da pauta salvando no banco de dados", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Votação criada"),
            @ApiResponse(responseCode = "404", description = "Pauta informada não encontrada"),
            @ApiResponse(responseCode = "409", description = "Já existe uma votação aberta para essa pauta")
    })
    public ResponseEntity<Object> saveVoting(@PathVariable(value = "sessionId") UUID sessionId,
                                             @RequestBody @Valid VotingDto votingDto) throws SaveException, GetException {
        log.debug("POST saveVoting votingDto received {}", votingDto.toString());
        Optional<SessionModel> sessionModelOptional = sessionService.findById(sessionId);
        if (sessionModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not Found");
        }

        if (Objects.nonNull(sessionModelOptional.get().getVotingId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a vote for this session.");
        }

        var votingModel = new VotingModel();
        BeanUtils.copyProperties(votingDto, votingModel);
        votingModel.setInitPeriod(votingDto.getDateSession());
        votingModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        if (votingDto.getDuration() != null) {
            votingModel.setEndPeriod(votingDto.getDateSession().plusMinutes(votingDto.getDuration()));
        } else {
            votingModel.setEndPeriod((votingDto.getDateSession().plusMinutes(1)));
        }

        log.debug("Post saveVoting votingModel saved {}", votingModel.toString());
        log.info("Voting saved successfully {}", votingModel.getVotingId());
        return ResponseEntity.status(HttpStatus.CREATED).body(votingService.save(votingModel, sessionModelOptional.get()));
    }

    @GetMapping("/sessions/voting/{sessionId}")
    @Operation(summary = "Busca uma votação associada a sessão selacionada", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Votação retornada"),
            @ApiResponse(responseCode = "404", description = "Pauta informada não encontrada"),
            @ApiResponse(responseCode = "404", description = "Pauta informada não contém votação"),
    })
    public ResponseEntity<Object> getOneVoting(@PathVariable(value = "sessionId") UUID sessionId) throws GetException {
        Optional<SessionModel> sessionModelOptional = sessionService.findById(sessionId);
        if (sessionModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not Found");
        }

        Optional<VotingModel> votingModelOptional = votingService.findById(sessionModelOptional.get().getVotingId());
        return votingModelOptional.<ResponseEntity<Object>>map(
                votingModel -> ResponseEntity.status(HttpStatus.OK).body(votingModel)
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("This Session Don't Has Voting"));
    }

    @PostMapping(value = "/sessions/{sessionId}/vote/{associateId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra o voto do associado dentro da votação na pauta informada", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto computado na pauta"),
            @ApiResponse(responseCode = "404", description = "Pauta informada não encontrada"),
            @ApiResponse(responseCode = "404", description = "Votação da pauta informada não encontrada"),
            @ApiResponse(responseCode = "409", description = "Votação aberta para essa pauta já encerrada"),
            @ApiResponse(responseCode = "409", description = "Votação aberta para essa pauta está fora do período de votação"),
            @ApiResponse(responseCode = "409", description = "Associado só pode votar se estiver registrado na pauta"),
            @ApiResponse(responseCode = "409", description = "Associado já votou nesta pauta"),
    })
    public ResponseEntity<Object> saveVote(@PathVariable(value = "sessionId") UUID sessionId,
                                           @PathVariable(value = "associateId") UUID associateId,
                                           @RequestBody @Valid VoteDto voteDto) throws GetException, SaveException {
        log.debug("POST saveVote votingDto received {}", voteDto.toString());
        Optional<SessionModel> sessionModelOptional = sessionService.findById(sessionId);
        if (sessionModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not Found");
        }

        Optional<VotingModel> votingModelOptional = votingService.findById(sessionModelOptional.get().getVotingId());
        if (votingModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voting Not Found");
        }

        var voting = votingModelOptional.get();

        if (voting.votingIsOver()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Voting for this session has ended.");
        }

        if (voting.votingIsAvailable()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Voting for this session starts " + voting.getInitPeriod() + ".");
        }

        var associates = sessionModelOptional.get().getAssociates();
        var associate = associates.stream().filter(a -> a.getAssociateId().equals(associateId)).findAny();

        if (associate.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The associate can only vote if he is registered in the session.");
        }

        var votes = votingModelOptional.get().getVotes();
        if (Objects.nonNull(votes)) {
            var vote = votes.stream().filter(v -> v.getAssociateId().equals(associateId)).findAny();

            if (vote.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This associate has already voted in this session.");
            }
        } else {
            votes = new ArrayList<>();
        }

        var voteModel = new VoteModel();
        BeanUtils.copyProperties(voteDto, voteModel);
        voteModel.setAssociateId(associateId);
        voteModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        voteService.save(voteModel);

        votes.add(voteModel);

        voting.setVotes(votes);

        votingService.save(voting, sessionModelOptional.get());

        log.debug("Post saveVote voteModel saved {}", voteModel.toString());
        log.info("Vote saved successfully {}", voteModel.getVoteId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Vote has been computed!");
    }
}
