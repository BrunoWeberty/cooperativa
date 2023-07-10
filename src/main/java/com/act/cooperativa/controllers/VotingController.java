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
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class VotingController {

    @Autowired
    VotingService votingService;

    @Autowired
    SessionService sessionService;

    @Autowired
    VoteService voteService;

    @PostMapping("/sessions/{sessionId}/voting")
    public ResponseEntity<Object> saveVoting(@PathVariable(value = "sessionId") UUID sessionId,
                                             @RequestBody @Valid VotingDto votingDto) throws SaveException, GetException {
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

        return ResponseEntity.status(HttpStatus.CREATED).body(votingService.save(votingModel, sessionModelOptional.get()));
    }

    @GetMapping("/sessions/voting/{sessionId}")
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

    @PostMapping("/sessions/{sessionId}/vote/{associateId}")
    public ResponseEntity<Object> saveVote(@PathVariable(value = "sessionId") UUID sessionId,
                                           @PathVariable(value = "associateId") UUID associateId,
                                           @RequestBody @Valid VoteDto voteDto) throws GetException, SaveException {
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
        var vote = votes.stream().filter(v -> v.getAssociateId().equals(associateId)).findAny();

        if (vote.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This associate has already voted in this session.");
        }

        var voteModel = new VoteModel();
        BeanUtils.copyProperties(voteDto, voteModel);
        voteModel.setAssociateId(associateId);
        voteModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        voteService.save(voteModel);

        votes.add(voteModel);

        voting.setVotes(votes);

        votingService.save(voting, sessionModelOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body("Vote has been computed!");
    }
}
